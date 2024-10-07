package net.frozenblock.lib.event.event;

import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.CommonEventEntrypoint;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public class ServerLevelEvents {
	public static final FrozenEvent<Load> LOAD = FrozenEvent.createEvent(Load.class);
	public static final FrozenEvent<Unload> UNLOAD = FrozenEvent.createEvent(Unload.class);

	@FunctionalInterface
	public interface Load extends CommonEventEntrypoint{
		void onWorldLoad(MinecraftServer server, ServerLevel world);
	}

	@FunctionalInterface
	public interface Unload extends CommonEventEntrypoint {
		void onWorldUnload(MinecraftServer server, ServerLevel world);
	}

	private ServerLevelEvents() {
	}
}
