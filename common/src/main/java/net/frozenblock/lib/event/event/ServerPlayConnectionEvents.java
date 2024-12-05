package net.frozenblock.lib.event.event;

import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.CommonEventEntrypoint;
import net.minecraft.world.entity.player.Player;

public class ServerPlayConnectionEvents {
	public static final FrozenEvent<Join> JOIN = FrozenEvent.createEvent(Join.class);
	public static final FrozenEvent<Disconnect> DISCONNECT =FrozenEvent.createEvent(Disconnect.class);

	private ServerPlayConnectionEvents() {
	}

	@FunctionalInterface
	public interface Join extends CommonEventEntrypoint {
		void onPlayReady(Player player);
	}

	@FunctionalInterface
	public interface Disconnect extends CommonEventEntrypoint {
		void onPlayDisconnect(Player player);
	}
}
