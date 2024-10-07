package net.frozenblock.lib;

import net.frozenblock.lib.cape.client.impl.ClientCapeData;
import net.frozenblock.lib.config.frozenlib_config.FrozenLibConfig;
import net.frozenblock.lib.debug.client.impl.DebugRenderManager;
import net.frozenblock.lib.debug.networking.StructureDebugRequestPayload;
import net.frozenblock.lib.env.api.EnvType;
import net.frozenblock.lib.env.api.Environment;
import net.frozenblock.lib.event.event.client.ClientChunkEvents;
import net.frozenblock.lib.event.event.client.ClientPlayConnectionEvents;
import net.frozenblock.lib.event.event.client.ClientTickEvents;
import net.frozenblock.lib.integration.api.ModIntegrations;
import net.frozenblock.lib.menu.api.Panoramas;
import net.frozenblock.lib.registry.api.client.FrozenLibClientRegistries;
import net.frozenblock.lib.resource.api.ResourceListenerAPI;
import net.frozenblock.lib.screenshake.api.client.ScreenShaker;
import net.frozenblock.lib.sound.api.FlyBySoundHub;
import net.frozenblock.lib.sound.impl.block_sound_group.BlockSoundGroupManager;
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

		// PARTICLES
		//TODO: REPLACE PARTICLE FACTORY
		//ParticleFactoryRegistry particleRegistry = ParticleFactoryRegistry.getInstance();
		//particleRegistry.register(FrozenParticleTypes.DEBUG_POS, DebugPosParticle.Provider::new);

		Panoramas.addPanorama(ResourceLocation.withDefaultNamespace("textures/gui/title/background/panorama"));
		ClientCapeData.init();

		ResourceListenerAPI.getClient().registerReloadListener(BlockSoundGroupManager.INSTANCE);

		//TODO: Pov: Treetrain got high and wrote this
		//FrozenClientEntrypoint.EVENT.invoker().init(); // also includes dev init
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
