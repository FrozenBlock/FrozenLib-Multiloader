package net.frozenblock.lib.event.event.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.ClientEventEntrypoint;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class ClientLifecycleEvents {
	/**
	 * Runs as the client boots.
	 */
	public static final FrozenEvent<ClientStarted> CLIENT_STARTED = FrozenEvent.createEvent(ClientStarted.class);
	/**
	 * Runs as the client stops.
	 */
	public static final FrozenEvent<ClientStopping> CLIENT_STOPPING = FrozenEvent.createEvent(ClientStopping.class);

	@FunctionalInterface
	public interface ClientStarted extends ClientEventEntrypoint {
		void onClientStarted(Minecraft client);
	}

	@FunctionalInterface
	public interface ClientStopping extends ClientEventEntrypoint {
		void onClientStopping(Minecraft client);
	}
}
