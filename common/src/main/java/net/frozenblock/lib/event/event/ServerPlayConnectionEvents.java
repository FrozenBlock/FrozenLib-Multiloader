package net.frozenblock.lib.event.event;

import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.CommonEventEntrypoint;
import net.frozenblock.lib.networking.impl.PacketSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class ServerPlayConnectionEvents {
	// TODO: [Liuk] implement
	public static final FrozenEvent<Init> INIT = FrozenEvent.createEvent(Init.class);
	// TODO: [Liuk] implement
	public static final FrozenEvent<Join> JOIN = FrozenEvent.createEvent(Join.class);
	// TODO: [Liuk] implement
	public static final FrozenEvent<Disconnect> DISCONNECT =FrozenEvent.createEvent(Disconnect.class);

	private ServerPlayConnectionEvents() {
	}

	@FunctionalInterface
	public interface Init extends CommonEventEntrypoint {
		void onPlayInit(ServerGamePacketListenerImpl handler, MinecraftServer server);
	}

	@FunctionalInterface
	public interface Join extends CommonEventEntrypoint {
		void onPlayReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server);
	}

	@FunctionalInterface
	public interface Disconnect extends CommonEventEntrypoint {
		void onPlayDisconnect(ServerGamePacketListenerImpl handler, MinecraftServer server);
	}
}
