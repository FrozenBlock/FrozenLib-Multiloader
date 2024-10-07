package net.frozenblock.lib.platform.api;

import dev.architectury.injectables.annotations.ExpectPlatform;
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
import java.util.Collection;

public class PacketHelper {

	@ExpectPlatform
	public static void sendToServer(CustomPacketPayload payload, CustomPacketPayload... payloads) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void sendToAllPlayers(MinecraftServer server, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void sendToPlayersInLevel(ServerLevel world, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void sendToPlayersNear(
		ServerLevel level, @Nullable ServerPlayer excluded, double x, double y, double z, double radius, CustomPacketPayload payload, CustomPacketPayload... payloads
	) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void sendToPlayersTrackingBlockPos(ServerLevel level, BlockPos blockPos, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos chunkPos, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void sendToPlayersTrackingEntity(Entity entity, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void sendToPlayersTrackingEntityAndSelf(Entity entity, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static Collection<ServerPlayer> all(MinecraftServer server) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static Collection<ServerPlayer> world(ServerLevel world) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static Collection<ServerPlayer> tracking(ServerLevel world, ChunkPos pos) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static Collection<ServerPlayer> tracking(Entity entity) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static Collection<ServerPlayer> tracking(BlockEntity blockEntity) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static Collection<ServerPlayer> tracking(ServerLevel world, BlockPos pos) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static Collection<ServerPlayer> around(ServerLevel world, Vec3 pos, double radius) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static Collection<ServerPlayer> around(ServerLevel world, Vec3i pos, double radius) {
		throw new AssertionError();
	}

}
