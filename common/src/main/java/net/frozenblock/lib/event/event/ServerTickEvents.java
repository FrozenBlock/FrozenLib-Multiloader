package net.frozenblock.lib.event.event;

import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.CommonEventEntrypoint;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public class ServerTickEvents {
	/**
	 * Runs before the server ticks.
	 */
	public static final FrozenEvent<StartTick> START_SERVER_TICK = FrozenEvent.createEvent(StartTick.class);
	/**
	 * Runs once the server has finished its current tick.
	 */
	public static final FrozenEvent<EndTick> END_SERVER_TICK = FrozenEvent.createEvent(EndTick.class);
	/**
	 * Runs before a {@link ServerLevel} ticks.
	 */
	public static final FrozenEvent<StartServerLevelTick> START_LEVEL_TICK = FrozenEvent.createEvent(StartServerLevelTick.class);
	/**
	 * Runs once a {@link ServerLevel} has finished its current tick.
	 */
	public static final FrozenEvent<EndServerLevelTick> END_LEVEL_TICK = FrozenEvent.createEvent(EndServerLevelTick.class);

	@FunctionalInterface
	public interface StartTick extends CommonEventEntrypoint {
		void onStartTick(MinecraftServer server);
	}

	@FunctionalInterface
	public interface EndTick extends CommonEventEntrypoint {
		void onEndTick(MinecraftServer server);
	}

	@FunctionalInterface
	public interface StartServerLevelTick extends CommonEventEntrypoint {
		void onStartTick(ServerLevel world);
	}

	@FunctionalInterface
	public interface EndServerLevelTick extends CommonEventEntrypoint {
		void onEndTick(ServerLevel world);
	}
}
