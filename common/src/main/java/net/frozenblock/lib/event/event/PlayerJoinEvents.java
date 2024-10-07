package net.frozenblock.lib.event.event;

import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.CommonEventEntrypoint;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;

/**
 * A class representing the player join event.
 */
public class PlayerJoinEvents {

	/**
	 * The event that is triggered before a player joins the server.
	 */
	public static final FrozenEvent<BeforePlayerJoin> BEFORE_JOIN_SERVER = FrozenEvent.createEvent(BeforePlayerJoin.class);

	/**
	 * The event that is triggered after a player joins the server.
	 */
	public static final FrozenEvent<PlayerJoin> AFTER_JOIN_SERVER = FrozenEvent.createEvent(PlayerJoin.class);

	/**
	 * The event that is triggered before a player joins a world.
	 */
	public static final FrozenEvent<BeforePlayerAddedToLevel> BEFORE_PLAYER_ADDED_TO_LEVEL = FrozenEvent.createEvent(BeforePlayerAddedToLevel.class);

	/**
	 * The event that is triggered after a player joins a world.
	 */
	public static final FrozenEvent<PlayerAddedToLevel> AFTER_PLAYER_ADDED_TO_LEVEL = FrozenEvent.createEvent(PlayerAddedToLevel.class);

	/**
	 * A functional interface representing a player join event.
	 */
	@FunctionalInterface
	public interface BeforePlayerJoin extends CommonEventEntrypoint {
		/**
		 * Triggers the event when a player joins the server.
		 *
		 * @param server the Minecraft server instance
		 * @param player the player joining the server
		 */
		void beforePlayerJoin(MinecraftServer server, ServerPlayer player, Connection connection, CommonListenerCookie cookie);
	}

	/**
	 * A functional interface representing a player join event.
	 */
	@FunctionalInterface
	public interface PlayerJoin extends CommonEventEntrypoint {
		/**
		 * Triggers the event when a player joins the server.
		 *
		 * @param server the Minecraft server instance
		 * @param player the player joining the server
		 */
		void onPlayerJoin(MinecraftServer server, ServerPlayer player, Connection connection, CommonListenerCookie cookie);
	}

	/**
	 * A functional interface representing a player added to level event.
	 */
	@FunctionalInterface
	public interface BeforePlayerAddedToLevel extends CommonEventEntrypoint {
		/**
		 * Triggers the event when a player is added to a level.
		 *
		 * @param server      the Minecraft server instance
		 * @param serverLevel the server level the player has been added to
		 * @param player      the player added to the level
		 */
		void beforePlayerAddedToLevel(MinecraftServer server, ServerLevel serverLevel, ServerPlayer player);
	}

	/**
	 * A functional interface representing a player added to level event.
	 */
	@FunctionalInterface
	public interface PlayerAddedToLevel extends CommonEventEntrypoint {
		/**
		 * Triggers the event when a player is added to a level.
		 *
		 * @param server      the Minecraft server instance
		 * @param serverLevel the server level the player has been added to
		 * @param player      the player added to the level
		 */
		void onPlayerAddedToLevel(MinecraftServer server, ServerLevel serverLevel, ServerPlayer player);
	}
}
