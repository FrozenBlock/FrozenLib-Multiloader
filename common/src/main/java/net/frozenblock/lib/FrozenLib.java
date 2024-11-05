package net.frozenblock.lib;

import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.frozenblock.lib.config.frozenlib_config.FrozenLibConfig;
import net.frozenblock.lib.config.impl.ConfigCommand;
import net.frozenblock.lib.core.impl.DataPackReloadMarker;
import net.frozenblock.lib.entity.api.EntityUtils;
import net.frozenblock.lib.entity.api.command.ScaleEntityCommand;
import net.frozenblock.lib.event.event.PlayerJoinEvents;
import net.frozenblock.lib.event.event.RegisterCommandEvents;
import net.frozenblock.lib.event.event.RegistryFreezeEvents;
import net.frozenblock.lib.event.event.ServerLevelEvents;
import net.frozenblock.lib.event.event.ServerTickEvents;
import net.frozenblock.lib.integration.api.ModIntegrations;
import net.frozenblock.lib.platform.api.NeoForgeFreezer;
import net.frozenblock.lib.networking.FrozenLibNetworking;
import net.frozenblock.lib.platform.api.PlatformHelper;
import net.frozenblock.lib.registry.api.FrozenLibRegistries;
import net.frozenblock.lib.screenshake.api.ScreenShakeManager;
import net.frozenblock.lib.screenshake.api.command.ScreenShakeCommand;
import net.frozenblock.lib.screenshake.impl.ScreenShakeStorage;
import net.frozenblock.lib.sound.api.predicate.SoundPredicate;
import net.frozenblock.lib.spotting_icons.api.SpottingIconPredicate;
import net.frozenblock.lib.tag.api.TagKeyArgument;
import net.frozenblock.lib.tag.api.TagListCommand;
import net.frozenblock.lib.wind.api.WindDisturbanceLogic;
import net.frozenblock.lib.wind.api.WindManager;
import net.frozenblock.lib.wind.api.command.WindCommand;
import net.frozenblock.lib.wind.impl.WindStorage;
import net.frozenblock.lib.worldgen.feature.api.FrozenFeatures;
import net.frozenblock.lib.worldgen.feature.api.placementmodifier.FrozenPlacementModifiers;
import net.frozenblock.lib.worldgen.structure.impl.FrozenRuleBlockEntityModifiers;
import net.frozenblock.lib.worldgen.structure.impl.FrozenStructureProcessorTypes;
import net.frozenblock.lib.worldgen.structure.impl.StructureUpgradeCommand;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.commands.WardenSpawnTrackerCommand;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.ApiStatus;
import org.quiltmc.qsl.frozenblock.core.registry.api.sync.ModProtocol;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.impl.ServerFreezer;

@ApiStatus.Internal
public class FrozenLib {

	public static void onInitialize() {
		FrozenLibRegistries.initRegistry();

		ServerFreezer.onInitialize();
		ModProtocol.loadVersions();
		//TODO: ServerRegistrySync.registerHandlers();

		// CONTINUE FROZENLIB INIT

		//TODO: ClientRegistrySync.registerHandlers();

		FrozenRuleBlockEntityModifiers.init();
		FrozenStructureProcessorTypes.init();
		SoundPredicate.init();
		SpottingIconPredicate.init();
		WindDisturbanceLogic.init();
		FrozenFeatures.init();
		FrozenPlacementModifiers.init();
		DataPackReloadMarker.init();

		RegisterCommandEvents.REGISTER.register((dispatcher, ctx, selection) -> {
			WindCommand.register(dispatcher);
			ScreenShakeCommand.register(dispatcher);
			ConfigCommand.register(dispatcher);
			TagListCommand.register(dispatcher);
			ScaleEntityCommand.register(dispatcher);
			if (PlatformHelper.isDevelopmentEnvironment()) {
				StructureUpgradeCommand.register(dispatcher);
			}
		});

		if (FrozenLibConfig.get().wardenSpawnTrackerCommand)
			RegisterCommandEvents.REGISTER.register(((dispatcher, registryAccess, environment) -> WardenSpawnTrackerCommand.register(dispatcher)));

		FrozenLibNetworking.registerNetworking();

		NeoForgeFreezer.unfreeze(BuiltInRegistries.COMMAND_ARGUMENT_TYPE);
		ArgumentTypeInfos.register(
			BuiltInRegistries.COMMAND_ARGUMENT_TYPE,
			FrozenLibConstants.string("tag_key"),
			ArgumentTypeInfos.fixClassType(TagKeyArgument.class),
			new TagKeyArgument.Info<>()
		);
		NeoForgeFreezer.freeze(BuiltInRegistries.COMMAND_ARGUMENT_TYPE);

		ServerLevelEvents.LOAD.register((server, level) -> {
			DimensionDataStorage dimensionDataStorage = level.getDataStorage();
			WindManager windManager = WindManager.getWindManager(level);
			dimensionDataStorage.computeIfAbsent(windManager.createData(), WindStorage.WIND_FILE_ID);
			ScreenShakeManager screenShakeManager = ScreenShakeManager.getScreenShakeManager(level);
			dimensionDataStorage.computeIfAbsent(screenShakeManager.createData(), ScreenShakeStorage.SCREEN_SHAKE_FILE_ID);
		});

		ServerLevelEvents.UNLOAD.register((server, serverLevel) -> {
			EntityUtils.clearEntitiesPerLevel(serverLevel);
			WindManager.getWindManager(serverLevel).clearAllWindDisturbances();
		});

		ServerTickEvents.START_LEVEL_TICK.register(serverLevel -> {
			WindManager.getWindManager(serverLevel).clearAndSwitchWindDisturbances();
			WindManager.getWindManager(serverLevel).tick(serverLevel);
			ScreenShakeManager.getScreenShakeManager(serverLevel).tick(serverLevel);
			EntityUtils.populateEntitiesPerLevel(serverLevel);
		});

		PlayerJoinEvents.BEFORE_PLAYER_ADDED_TO_LEVEL.register(((server, serverLevel, player) -> {
			WindManager windManager = WindManager.getWindManager(serverLevel);
			windManager.sendSyncToPlayer(windManager.createSyncPacket(), player);
		}));

		RegistryFreezeEvents.START_REGISTRY_FREEZE.register((registry, allRegistries) -> {
			if (!allRegistries) return;

			ModIntegrations.initialize();
		});

		RegistryFreezeEvents.END_REGISTRY_FREEZE.register((registry, allRegistries) -> {
			if (!allRegistries) return;

			for (Config<?> config : ConfigRegistry.getAllConfigs()) {
				config.save();
			}
		});

		// TODO: Proper Resource Reload API
		// Fabric has an API and Forge has events for this, keep that in mind.
		//ResourceListenerAPI.getServer().registerReloadListener(BlockSoundTypeManager.INSTANCE);
	}

	public static void onInitializeServer() {

	}
}
