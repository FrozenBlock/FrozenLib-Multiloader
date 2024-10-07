package net.frozenblock.lib.platform.api;

import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface IPacketHelper {

	void sendToServer(CustomPacketPayload payload, CustomPacketPayload... payloads);

	void sendToPlayer(ServerPlayer player, CustomPacketPayload payload, CustomPacketPayload... payloads);

	void sendToAllPlayers(MinecraftServer server, CustomPacketPayload payload, CustomPacketPayload... payloads);

	void sendToPlayersInLevel(ServerLevel world, CustomPacketPayload payload, CustomPacketPayload... payloads);

	void sendToPlayersNear(ServerLevel level, @Nullable ServerPlayer excluded, double x, double y, double z, double radius, CustomPacketPayload payload, CustomPacketPayload... payloads);

	void sendToPlayersTrackingBlockPos(ServerLevel level, BlockPos blockPos, CustomPacketPayload payload, CustomPacketPayload... payloads);

	void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos chunkPos, CustomPacketPayload payload, CustomPacketPayload... payloads);

	void sendToPlayersTrackingEntity(Entity entity, CustomPacketPayload payload, CustomPacketPayload... payloads);

	void sendToPlayersTrackingEntityAndSelf(Entity entity, CustomPacketPayload payload, CustomPacketPayload... payloads);

	Collection<ServerPlayer> all(MinecraftServer server);

	Collection<ServerPlayer> world(ServerLevel world);

	Collection<ServerPlayer> tracking(ServerLevel world, ChunkPos pos);

	Collection<ServerPlayer> tracking(Entity entity);

	Collection<ServerPlayer> tracking(BlockEntity blockEntity);

	Collection<ServerPlayer> tracking(ServerLevel world, BlockPos pos);

	Collection<ServerPlayer> around(ServerLevel world, Vec3 pos, double radius);

	Collection<ServerPlayer> around(ServerLevel world, Vec3i pos, double radius);

}
