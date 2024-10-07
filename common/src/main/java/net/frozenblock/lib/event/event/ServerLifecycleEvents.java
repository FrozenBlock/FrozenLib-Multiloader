package net.frozenblock.lib.event.event;

import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.CommonEventEntrypoint;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.CloseableResourceManager;

public final class ServerLifecycleEvents {
	/**
	 * Runs while the server is starting.
	 */
	public static final FrozenEvent<ServerStarting> SERVER_STARTING = FrozenEvent.createEvent(ServerStarting.class);
	/**
	 * Runs when the server starts.
	 */
	public static final FrozenEvent<ServerStarted> SERVER_STARTED = FrozenEvent.createEvent(ServerStarted.class);
	/**
	 * Runs while the server is stopping.
	 */
	public static final FrozenEvent<ServerStopping> SERVER_STOPPING = FrozenEvent.createEvent(ServerStopping.class);
	/**
	 * Runs when the server stops.
	 */
	public static final FrozenEvent<ServerStopped> SERVER_STOPPED = FrozenEvent.createEvent(ServerStopped.class);
	/**
	 * Runs when the server syncs data pack contents with the client.
	 */
	public static final FrozenEvent<SyncDataPackContents> SYNC_DATA_PACK_CONTENTS = FrozenEvent.createEvent(SyncDataPackContents.class);
	/**
	 * Runs when the server starts reloading data pack contents.
	 */
	public static final FrozenEvent<StartDataPackReload> START_DATA_PACK_RELOAD = FrozenEvent.createEvent(StartDataPackReload.class);
	/**
	 * Runs when the server is finished reloading data pack contents.
	 */
	public static final FrozenEvent<EndDataPackReload> END_DATA_PACK_RELOAD = FrozenEvent.createEvent(EndDataPackReload.class);
	/**
	 * Runs before a server saves.
	 */
	public static final FrozenEvent<BeforeSave> BEFORE_SAVE = FrozenEvent.createEvent(BeforeSave.class);
	/**
	 * Runs after a server finishes saving.
	 */
	public static final FrozenEvent<AfterSave> AFTER_SAVE = FrozenEvent.createEvent(AfterSave.class);

	@FunctionalInterface
	public interface ServerStarting extends CommonEventEntrypoint {
		void onServerStarting(MinecraftServer server);
	}

	@FunctionalInterface
	public interface ServerStarted extends CommonEventEntrypoint {
		void onServerStarted(MinecraftServer server);
	}

	@FunctionalInterface
	public interface ServerStopping extends CommonEventEntrypoint {
		void onServerStopping(MinecraftServer server);
	}

	@FunctionalInterface
	public interface ServerStopped extends CommonEventEntrypoint {
		void onServerStopped(MinecraftServer server);
	}

	@FunctionalInterface
	public interface SyncDataPackContents extends CommonEventEntrypoint {
		void onSyncDataPackContents(ServerPlayer player, boolean joined);
	}

	@FunctionalInterface
	public interface StartDataPackReload extends CommonEventEntrypoint {
		void startDataPackReload(MinecraftServer server, CloseableResourceManager resourceManager);
	}

	@FunctionalInterface
	public interface EndDataPackReload extends CommonEventEntrypoint {
		void endDataPackReload(MinecraftServer server, CloseableResourceManager resourceManager, boolean success);
	}

	@FunctionalInterface
	public interface BeforeSave extends CommonEventEntrypoint {
		void onBeforeSave(MinecraftServer server, boolean flush, boolean force);
	}

	@FunctionalInterface
	public interface AfterSave extends CommonEventEntrypoint {
		void onAfterSave(MinecraftServer server, boolean flush, boolean force);
	}
}
