package net.frozenblock.lib.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.frozenblock.lib.FrozenLib;
import net.frozenblock.lib.event.event.RegisterCommandEvents;
import net.frozenblock.lib.event.event.ServerLevelEvents;
import net.frozenblock.lib.debug.item.FrozenLibDevItems;
import net.frozenblock.lib.platform.api.PlatformHelper;

public class FrozenLibFabric implements ModInitializer, DedicatedServerModInitializer {

	@Override
	public void onInitializeServer() {
		FrozenLib.onInitializeServer();
	}

	@Override
	public void onInitialize() {
		FrozenLib.onInitialize();

		if (PlatformHelper.isDevelopmentEnvironment()) {
			FrozenLibDevItems.register();
		}

		// Redirect Fabric -> FrozenLib events
		CommandRegistrationCallback.EVENT.register(
			(dispatcher, registryAccess, environment) -> RegisterCommandEvents.REGISTER.invoke(e -> e.register(dispatcher, registryAccess, environment))
		);

		ServerLifecycleEvents.SERVER_STARTING.register(
			server -> net.frozenblock.lib.event.event.ServerLifecycleEvents.SERVER_STARTING.invoke(e -> e.onServerStarting(server))
		);
		ServerLifecycleEvents.SERVER_STARTED.register(
			server -> net.frozenblock.lib.event.event.ServerLifecycleEvents.SERVER_STARTED.invoke(e -> e.onServerStarted(server))
		);
		ServerLifecycleEvents.SERVER_STOPPING.register(
			server -> net.frozenblock.lib.event.event.ServerLifecycleEvents.SERVER_STOPPING.invoke(e -> e.onServerStopping(server))
		);
		ServerLifecycleEvents.SERVER_STOPPED.register(
			server -> net.frozenblock.lib.event.event.ServerLifecycleEvents.SERVER_STOPPED.invoke(e -> e.onServerStopped(server))
		);
		ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(
			(player, joined) -> net.frozenblock.lib.event.event.ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.invoke(event -> event.onSyncDataPackContents(player, joined))
		);
		ServerLifecycleEvents.START_DATA_PACK_RELOAD.register(
			(server, manager) -> net.frozenblock.lib.event.event.ServerLifecycleEvents.START_DATA_PACK_RELOAD.invoke(event -> event.startDataPackReload(server, manager))
		);
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(
			(server, manager, success) -> net.frozenblock.lib.event.event.ServerLifecycleEvents.END_DATA_PACK_RELOAD.invoke(event -> event.endDataPackReload(server, manager, success))
		);
		ServerLifecycleEvents.BEFORE_SAVE.register(
			(server, flush, force) -> net.frozenblock.lib.event.event.ServerLifecycleEvents.BEFORE_SAVE.invoke(e -> e.onBeforeSave(server, flush, force))
		);
		ServerLifecycleEvents.AFTER_SAVE.register(
			(server, flush, force) -> net.frozenblock.lib.event.event.ServerLifecycleEvents.AFTER_SAVE.invoke(e -> e.onAfterSave(server, flush, force))
		);

		ServerWorldEvents.LOAD.register(
			(minecraftServer, serverLevel) -> ServerLevelEvents.LOAD.invoke(e -> e.onWorldLoad(minecraftServer, serverLevel))
		);
		ServerWorldEvents.UNLOAD.register(
			(minecraftServer, serverLevel) -> ServerLevelEvents.UNLOAD.invoke(e -> e.onWorldUnload(minecraftServer, serverLevel))
		);

		ServerTickEvents.START_SERVER_TICK.register(
			server -> net.frozenblock.lib.event.event.ServerTickEvents.START_SERVER_TICK.invoke(e -> e.onStartTick(server))
		);
		ServerTickEvents.END_SERVER_TICK.register(
			server -> net.frozenblock.lib.event.event.ServerTickEvents.END_SERVER_TICK.invoke(e -> e.onEndTick(server))
		);
		ServerTickEvents.START_WORLD_TICK.register(
			serverLevel -> net.frozenblock.lib.event.event.ServerTickEvents.START_LEVEL_TICK.invoke(e -> e.onStartTick(serverLevel))
		);
		ServerTickEvents.END_WORLD_TICK.register(
			serverLevel -> net.frozenblock.lib.event.event.ServerTickEvents.END_LEVEL_TICK.invoke(e -> e.onEndTick(serverLevel))
		);
	}
}
