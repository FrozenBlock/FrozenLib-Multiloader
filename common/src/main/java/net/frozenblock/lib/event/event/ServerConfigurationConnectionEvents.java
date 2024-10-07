package net.frozenblock.lib.event.event;

import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.CommonEventEntrypoint;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;

public class ServerConfigurationConnectionEvents {
	public static final FrozenEvent<BeforeConfigure> BEFORE_CONFIGURE = FrozenEvent.createEvent(BeforeConfigure.class);
	// TODO: [Liuk] implement
	public static final FrozenEvent<Configure> CONFIGURE = FrozenEvent.createEvent(Configure.class);
	// TODO: [Liuk] implement
	public static final FrozenEvent<Disconnect> DISCONNECT = FrozenEvent.createEvent(Disconnect.class);

	private ServerConfigurationConnectionEvents() {
	}

	@FunctionalInterface
	public interface BeforeConfigure extends CommonEventEntrypoint {
		void beforeSendConfiguration(ServerConfigurationPacketListenerImpl handler, MinecraftServer server);
	}

	@FunctionalInterface
	public interface Configure extends CommonEventEntrypoint {
		void onSendConfiguration(ServerConfigurationPacketListenerImpl handler, MinecraftServer server);
	}

	@FunctionalInterface
	public interface Disconnect extends CommonEventEntrypoint {
		void onConfigureDisconnect(ServerConfigurationPacketListenerImpl handler, MinecraftServer server);
	}
}
