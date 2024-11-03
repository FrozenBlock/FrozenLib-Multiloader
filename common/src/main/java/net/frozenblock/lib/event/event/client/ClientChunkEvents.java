package net.frozenblock.lib.event.event.client;

import lombok.experimental.UtilityClass;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.ClientEventEntrypoint;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.chunk.LevelChunk;

@UtilityClass
@Environment(EnvType.CLIENT)
public class ClientChunkEvents {
	/**
	 * Runs upon a chunk loading on the client.
	 */
	public static final FrozenEvent<Load> CHUNK_LOAD = FrozenEvent.createEvent(Load.class);
	/**
	 * Runs upon a chunk being unloaded on the client.
	 */
	public static final FrozenEvent<Unload> CHUNK_UNLOAD = FrozenEvent.createEvent(Unload.class);

	@FunctionalInterface
	public interface Load extends ClientEventEntrypoint {
		void onChunkLoad(ClientLevel world, LevelChunk chunk);
	}

	@FunctionalInterface
	public interface Unload extends ClientEventEntrypoint {
		void onChunkUnload(ClientLevel world, LevelChunk chunk);
	}
}
