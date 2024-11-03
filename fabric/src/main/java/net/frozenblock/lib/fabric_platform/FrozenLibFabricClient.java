package net.frozenblock.lib.fabric_platform;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.frozenblock.lib.FrozenLibClient;
import net.frozenblock.lib.networking.client.FrozenLibClientNetworking;
import net.frozenblock.lib.networking.impl.PacketSender;

public class FrozenLibFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		FrozenLibClient.onInitialize();
		FrozenLibClientNetworking.registerClientReceivers();

		// Redirect Fabric -> FrozenLib events
		ClientChunkEvents.CHUNK_LOAD.register(
			(clientLevel, levelChunk) -> net.frozenblock.lib.event.event.client.ClientChunkEvents.CHUNK_LOAD.invoke(e -> e.onChunkLoad(clientLevel, levelChunk))
		);
		ClientChunkEvents.CHUNK_UNLOAD.register(
			(clientLevel, levelChunk) -> net.frozenblock.lib.event.event.client.ClientChunkEvents.CHUNK_UNLOAD.invoke(e -> e.onChunkUnload(clientLevel, levelChunk))
		);

		ClientEntityEvents.ENTITY_LOAD.register(
			(entity, world) -> net.frozenblock.lib.event.event.client.ClientEntityEvents.ENTITY_LOAD.invoke(e -> e.onLoad(entity, world))
		);
		ClientEntityEvents.ENTITY_UNLOAD.register(
			(entity, world) -> net.frozenblock.lib.event.event.client.ClientEntityEvents.ENTITY_UNLOAD.invoke(e -> e.onUnload(entity, world))
		);

		ClientTickEvents.START_CLIENT_TICK.register(
			minecraft -> net.frozenblock.lib.event.event.client.ClientTickEvents.START_CLIENT_TICK.invoke(e -> e.onStartTick(minecraft))
		);
		ClientTickEvents.END_CLIENT_TICK.register(
			minecraft -> net.frozenblock.lib.event.event.client.ClientTickEvents.END_CLIENT_TICK.invoke(e -> e.onEndTick(minecraft))
		);
		ClientTickEvents.START_WORLD_TICK.register(
			clientLevel -> net.frozenblock.lib.event.event.client.ClientTickEvents.START_LEVEL_TICK.invoke(e -> e.onStartTick(clientLevel))
		);
		ClientTickEvents.END_WORLD_TICK.register(
			clientLevel -> net.frozenblock.lib.event.event.client.ClientTickEvents.END_LEVEL_TICK.invoke(e -> e.onEndTick(clientLevel))
		);

		ClientLifecycleEvents.CLIENT_STARTED.register(
			minecraft -> net.frozenblock.lib.event.event.client.ClientLifecycleEvents.CLIENT_STARTED.invoke(e -> e.onClientStarted(minecraft))
		);
		ClientLifecycleEvents.CLIENT_STOPPING.register(
			minecraft -> net.frozenblock.lib.event.event.client.ClientLifecycleEvents.CLIENT_STOPPING.invoke(e -> e.onClientStopping(minecraft))
		);

		ClientPlayConnectionEvents.JOIN.register(
			(clientPacketListener, packetSender, minecraft) ->
				net.frozenblock.lib.event.event.client.ClientPlayConnectionEvents.JOIN.invoke(
					e -> e.onPlayReady(clientPacketListener, new PacketSender(clientPacketListener.getConnection()), minecraft)
				)
		);
		ClientPlayConnectionEvents.DISCONNECT.register(
			(clientPacketListener, minecraft) ->
				net.frozenblock.lib.event.event.client.ClientPlayConnectionEvents.DISCONNECT.invoke(e -> e.onPlayDisconnect(clientPacketListener, minecraft))
		);
	}
}
