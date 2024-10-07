package net.frozenblock.lib.event.event.client;

import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.ClientEventEntrypoint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

public class ClientTickEvents {
	/**
	 * Runs upon the start of the client tick.
	 */
	public static FrozenEvent<StartTick> START_CLIENT_TICK = FrozenEvent.createEvent(StartTick.class);
	/**
	 * Runs upon the end of the client tick.
	 */
	public static FrozenEvent<EndTick> END_CLIENT_TICK = FrozenEvent.createEvent(EndTick.class);
	/**
	 * Runs upon the end of the client's world being ticked.
	 */
	public static FrozenEvent<StartLevelTick> START_LEVEL_TICK = FrozenEvent.createEvent(StartLevelTick.class);
	/**
	 * Runs upon the end of the client's world being ticked.
	 */
	public static FrozenEvent<EndLevelTick> END_LEVEL_TICK = FrozenEvent.createEvent(EndLevelTick.class);

	@FunctionalInterface
	public interface StartTick extends ClientEventEntrypoint {
		void onStartTick(Minecraft client);
	}

	@FunctionalInterface
	public interface EndTick extends ClientEventEntrypoint {
		void onEndTick(Minecraft client);
	}

	@FunctionalInterface
	public interface StartLevelTick extends ClientEventEntrypoint {
		void onStartTick(ClientLevel level);
	}

	@FunctionalInterface
	public interface EndLevelTick extends ClientEventEntrypoint {
		void onEndTick(ClientLevel level);
	}
}
