package net.frozenblock.lib.event.event.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.ClientEventEntrypoint;
import net.frozenblock.lib.networking.impl.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

@Environment(EnvType.CLIENT)
public class ClientPlayConnectionEvents {
	/**
	 * Fires when a player joins a world.
	 */
	public static final FrozenEvent<Join> JOIN = FrozenEvent.createEvent(Join.class);
	/**
	 * Fires when a player joins or disconnects from a world.
	 */
	public static final FrozenEvent<Disconnect> DISCONNECT = FrozenEvent.createEvent(Disconnect.class);

	@FunctionalInterface
	public interface Join extends ClientEventEntrypoint {
		void onPlayReady(ClientPacketListener handler, PacketSender sender, Minecraft client);
	}

	@FunctionalInterface
	public interface Disconnect extends ClientEventEntrypoint {
		void onPlayDisconnect(ClientPacketListener handler, Minecraft client);
	}
}
