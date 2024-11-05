package net.frozenblock.lib.neoforge.mixin.event.client;

import com.llamalad7.mixinextras.sugar.Local;
import java.util.function.Consumer;
import net.frozenblock.lib.event.event.client.ClientChunkEvents;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientChunkCache.class)
public class ClientChunkCacheMixinNeoForgePatch {
	@Final
	@Shadow
	ClientLevel level;

	@Inject(method = "replaceWithPacketData", at = @At("TAIL"))
	private void frozenLib$neoForge$chunkLoadEvent(
		int x, int z, FriendlyByteBuf buffer, CompoundTag tag, Consumer<ClientboundLevelChunkPacketData.BlockEntityTagOutput> consumer, CallbackInfoReturnable<LevelChunk> info
	) {
		ClientChunkEvents.CHUNK_LOAD.invoke(e->e.onChunkLoad(this.level, info.getReturnValue()));
	}

	@Inject(
		method = "replaceWithPacketData",
		at = @At(
			value = "NEW",
			target = "net/minecraft/world/level/chunk/LevelChunk",
			shift = At.Shift.BEFORE
		)
	)
	private void frozenLib$neoForge$chunkUnloadEvent(
		int x, int z, FriendlyByteBuf buffer, CompoundTag tag, Consumer<ClientboundLevelChunkPacketData.BlockEntityTagOutput> consumer, CallbackInfoReturnable<LevelChunk> info,
		@Local LevelChunk levelchunk
	) {
		if (levelchunk != null) ClientChunkEvents.CHUNK_UNLOAD.invoke(e->e.onChunkUnload(level, levelchunk));
	}

	@Inject(
		method = "drop",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/multiplayer/ClientChunkCache$Storage;replace(ILnet/minecraft/world/level/chunk/LevelChunk;Lnet/minecraft/world/level/chunk/LevelChunk;)Lnet/minecraft/world/level/chunk/LevelChunk;"
		)
	)
	private void frozenLib$neoForge$chunkUnloadEventB(ChunkPos chunkPos, CallbackInfo ci, @Local LevelChunk levelChunk) {
		ClientChunkEvents.CHUNK_UNLOAD.invoke(e->e.onChunkUnload(level, levelChunk));
	}

	@Inject(
		method = "updateViewRadius",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/multiplayer/ClientChunkCache$Storage;inRange(II)Z"
		)
	)
	private void frozenLib$neoForge$chunkUnloadEventC(
		int viewDistance, CallbackInfo info, @Local ClientChunkCache.Storage clientchunkcache$storage, @Local LevelChunk levelchunk, @Local ChunkPos chunkpos
	) {
		if (!clientchunkcache$storage.inRange(chunkpos.x, chunkpos.z)) {
			ClientChunkEvents.CHUNK_UNLOAD.invoke(e->e.onChunkUnload(level, levelchunk));
		}
	}
}
