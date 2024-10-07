package net.frozenblock.lib.platform;


import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import net.frozenblock.lib.neoforge.mixin.ChunkMapAccessor;
import net.frozenblock.lib.neoforge.mixin.EntityTrackerAccessor;
import net.frozenblock.lib.platform.api.IPacketHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NeoForgePacketHelper implements IPacketHelper {
	@Override
	public void sendToServer(CustomPacketPayload payload, CustomPacketPayload... payloads) {
		PacketDistributor.sendToServer(payload, payloads);
	}

	@Override
	public void sendToPlayer(ServerPlayer player, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		PacketDistributor.sendToPlayer(player, payload, payloads);
	}

	@Override
	public void sendToAllPlayers(MinecraftServer server, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		PacketDistributor.sendToAllPlayers(payload, payloads);
	}

	@Override
	public void sendToPlayersInLevel(ServerLevel world, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		PacketDistributor.sendToPlayersInDimension(world, payload, payloads);
	}

	@Override
	public void sendToPlayersNear(ServerLevel level, @Nullable ServerPlayer excluded, double x, double y, double z, double radius, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		PacketDistributor.sendToPlayersNear(level, excluded, x, y, z, radius, payload, payloads);
	}

	@Override
	public void sendToPlayersTrackingBlockPos(ServerLevel level, BlockPos blockPos, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		PacketDistributor.sendToPlayersTrackingChunk(level, new ChunkPos(blockPos), payload, payloads);
	}

	@Override
	public void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos chunkPos, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		PacketDistributor.sendToPlayersTrackingChunk(level, chunkPos, payload, payloads);
	}

	@Override
	public void sendToPlayersTrackingEntity(Entity entity, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		PacketDistributor.sendToPlayersTrackingEntity(entity, payload, payloads);
	}

	@Override
	public void sendToPlayersTrackingEntityAndSelf(Entity entity, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, payload, payloads);
	}

	@Override
	public Collection<ServerPlayer> all(@NotNull MinecraftServer server) {
		return Collections.unmodifiableCollection(server.getPlayerList().getPlayers());
	}

	@Override
	public Collection<ServerPlayer> world(@NotNull ServerLevel world) {
		return Collections.unmodifiableCollection(world.players());
	}

	@Override
	public Collection<ServerPlayer> tracking(@NotNull ServerLevel world, ChunkPos pos) {
		return world.getChunkSource().chunkMap.getPlayers(pos, false);
	}

	@Override
	public Collection<ServerPlayer> tracking(@NotNull Entity entity) {
		ChunkSource manager = entity.level().getChunkSource();

		if (manager instanceof ServerChunkCache) {
			ChunkMap chunkLoadingManager = ((ServerChunkCache) manager).chunkMap;
			EntityTrackerAccessor tracker = ChunkMapAccessor.class.cast(chunkLoadingManager).frozenLib$getEntityTrackers().get(entity.getId());
			if (tracker != null) {
				return tracker.frozenLib$getPlayersTracking()
					.stream().map(ServerPlayerConnection::getPlayer).collect(Collectors.toUnmodifiableSet());
			}

			return Collections.emptySet();
		}
		throw new IllegalArgumentException("Must be on dedicated server");
	}

	@Override
	public Collection<ServerPlayer> tracking(@NotNull BlockEntity blockEntity) {
		if (!blockEntity.hasLevel() || blockEntity.getLevel().isClientSide()) {
			throw new IllegalArgumentException("Must be on dedicated server");
		}

		return tracking((ServerLevel) blockEntity.getLevel(), blockEntity.getBlockPos());
	}

	@Override
	public Collection<ServerPlayer> tracking(ServerLevel world, BlockPos pos) {
		return tracking(world, new ChunkPos(pos));
	}

	@Override
	public Collection<ServerPlayer> around(ServerLevel world, Vec3 pos, double radius) {
		double radiusSq = radius * radius;

		return world(world)
			.stream()
			.filter((p) -> p.distanceToSqr(pos) <= radiusSq)
			.collect(Collectors.toList());
	}

	@Override
	public Collection<ServerPlayer> around(ServerLevel world, Vec3i pos, double radius) {
		double radiusSq = radius * radius;

		return world(world)
			.stream()
			.filter((p) -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) <= radiusSq)
			.collect(Collectors.toList());
	}
}
