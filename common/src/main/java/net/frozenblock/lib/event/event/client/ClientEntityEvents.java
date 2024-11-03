package net.frozenblock.lib.event.event.client;

import net.frozenblock.lib.environment.api.EnvType;
import net.frozenblock.lib.environment.api.Environment;
import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.ClientEventEntrypoint;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;

@Environment(EnvType.CLIENT)
public class ClientEntityEvents {
	/**
	 * Runs upon an entity being tracked on the client.
	 */
	public static final FrozenEvent<Load> ENTITY_LOAD = FrozenEvent.createEvent(Load.class);
	/**
	 * Runs upon an entity being untracked on the client.
	 */
	public static final FrozenEvent<Unload> ENTITY_UNLOAD = FrozenEvent.createEvent(Unload.class);

	@FunctionalInterface
	public interface Load extends ClientEventEntrypoint {
		void onLoad(Entity entity, ClientLevel world);
	}

	@FunctionalInterface
	public interface Unload extends ClientEventEntrypoint {
		void onUnload(Entity entity, ClientLevel world);
	}
}
