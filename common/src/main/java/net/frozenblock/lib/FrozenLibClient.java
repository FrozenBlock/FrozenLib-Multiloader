package net.frozenblock.lib;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.cape.client.impl.ClientCapeData;
import net.frozenblock.lib.config.frozenlib_config.FrozenLibConfig;
import net.frozenblock.lib.debug.client.impl.DebugRenderManager;
import net.frozenblock.lib.debug.networking.StructureDebugRequestPayload;
import net.frozenblock.lib.event.event.client.ClientChunkEvents;
import net.frozenblock.lib.event.event.client.ClientPlayConnectionEvents;
import net.frozenblock.lib.event.event.client.ClientTickEvents;
import net.frozenblock.lib.menu.api.PanoramaAPI;
import net.frozenblock.lib.mod_integration.api.ModIntegrations;
import net.frozenblock.lib.registry.api.client.FrozenLibClientRegistries;
import net.frozenblock.lib.screenshake.api.client.ScreenShaker;
import net.frozenblock.lib.sound.api.FlyBySoundHub;
import net.frozenblock.lib.wind.api.ClientWindManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.impl.client.ClientFreezer;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public class FrozenLibClient {

	public static void onInitialize() {
		FrozenLibClientRegistries.register();
		ModIntegrations.initializePreFreeze(); // Mod integrations must run after normal mod initialization

		// QUILT INIT
		ClientFreezer.onInitializeClient();

		// CONTINUE FROZENLIB INIT
		registerClientEvents();
		DebugRenderManager.init();

		PanoramaAPI.addPanorama(ResourceLocation.withDefaultNamespace("textures/gui/title/background/panorama"));
		ClientCapeData.init();

		// TODO: Proper Resource Reload API
		//ResourceListenerAPI.getClient().registerReloadListener(BlockSoundTypeManager.INSTANCE);
	}

	private static void registerClientEvents() {
		ClientTickEvents.START_LEVEL_TICK.register(
			world -> {
				ClientWindManager.tick(world);
				ScreenShaker.tick(world);
				FlyBySoundHub.update(Minecraft.getInstance(), Minecraft.getInstance().getCameraEntity(), true);
			}
		);
		ClientTickEvents.START_CLIENT_TICK.register(client -> ClientWindManager.clearAndSwitchWindDisturbances());
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			ScreenShaker.clear();
			ClientWindManager.clearAllWindDisturbances();
		});
		ClientChunkEvents.CHUNK_LOAD.register(
			(world, chunk) -> {
				if (FrozenLibConfig.IS_DEBUG) world.sendPacketToServer(new ServerboundCustomPayloadPacket(new StructureDebugRequestPayload(chunk.getPos())));
			}
		);
	}
}
