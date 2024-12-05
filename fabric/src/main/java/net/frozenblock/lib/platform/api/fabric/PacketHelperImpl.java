package net.frozenblock.lib.platform.api.fabric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PacketHelperImpl {

	public static void sendToServer(CustomPacketPayload payload, CustomPacketPayload @NotNull ... payloads) {
		ClientPlayNetworking.send(payload);
		for (var p : payloads) {
			ClientPlayNetworking.send(p);
		}
	}

	public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload, CustomPacketPayload @NotNull ... payloads) {
		ServerPlayNetworking.send(player, payload);
		for (var p : payloads) {
			ServerPlayNetworking.send(player, p);
		}
	}

	public static void sendToAllPlayers(MinecraftServer server, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		List<Packet<?>> packetsToSend = new ArrayList<>();
		packetsToSend.add(new ClientboundCustomPayloadPacket(payload));
		Arrays.stream(payloads).toList().forEach(payload1 -> packetsToSend.add(new ClientboundCustomPayloadPacket(payload1)));

		all(server).forEach(player -> {
			packetsToSend.forEach(packet -> player.connection.send(packet));
		});
	}

	public static void sendToPlayersInLevel(ServerLevel world, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		List<Packet<?>> packetsToSend = new ArrayList<>();
		packetsToSend.add(new ClientboundCustomPayloadPacket(payload));
		Arrays.stream(payloads).toList().forEach(payload1 -> packetsToSend.add(new ClientboundCustomPayloadPacket(payload1)));

		world(world).forEach(player -> {
			packetsToSend.forEach(packet -> player.connection.send(packet));
		});
	}

	public static void sendToPlayersNear(
		ServerLevel level,
		@Nullable ServerPlayer excluded,
		double x,
		double y,
		double z,
		double radius,
		CustomPacketPayload payload,
		CustomPacketPayload... payloads
	) {
		List<Packet<?>> packetsToSend = new ArrayList<>();
		packetsToSend.add(new ClientboundCustomPayloadPacket(payload));
		Arrays.stream(payloads).toList().forEach(payload1 -> packetsToSend.add(new ClientboundCustomPayloadPacket(payload1)));

		around(level, new Vec3(x, y, z), radius).forEach(player -> {
			packetsToSend.forEach(packet -> player.connection.send(packet));
		});
	}

	public static void sendToPlayersTrackingBlockPos(ServerLevel level, BlockPos blockPos, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		List<Packet<?>> packetsToSend = new ArrayList<>();
		packetsToSend.add(new ClientboundCustomPayloadPacket(payload));
		Arrays.stream(payloads).toList().forEach(payload1 -> packetsToSend.add(new ClientboundCustomPayloadPacket(payload1)));

		tracking(level, blockPos).forEach(player -> {
			packetsToSend.forEach(packet -> player.connection.send(packet));
		});
	}

	public static void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos chunkPos, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		List<Packet<?>> packetsToSend = new ArrayList<>();
		packetsToSend.add(new ClientboundCustomPayloadPacket(payload));
		Arrays.stream(payloads).toList().forEach(payload1 -> packetsToSend.add(new ClientboundCustomPayloadPacket(payload1)));

		tracking(level, chunkPos).forEach(player -> {
			packetsToSend.forEach(packet -> player.connection.send(packet));
		});
	}

	public static void sendToPlayersTrackingEntity(Entity entity, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		List<Packet<?>> packetsToSend = new ArrayList<>();
		packetsToSend.add(new ClientboundCustomPayloadPacket(payload));
		Arrays.stream(payloads).toList().forEach(payload1 -> packetsToSend.add(new ClientboundCustomPayloadPacket(payload1)));

		tracking(entity).forEach(player -> {
			packetsToSend.forEach(packet -> player.connection.send(packet));
		});
	}

	public static void sendToPlayersTrackingEntityAndSelf(Entity entity, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		List<Packet<?>> packetsToSend = new ArrayList<>();
		packetsToSend.add(new ClientboundCustomPayloadPacket(payload));
		Arrays.stream(payloads).toList().forEach(payload1 -> packetsToSend.add(new ClientboundCustomPayloadPacket(payload1)));

		tracking(entity).forEach(player -> {
			packetsToSend.forEach(packet -> player.connection.send(packet));
		});

		if (entity instanceof ServerPlayer player) {
			packetsToSend.forEach(packet -> player.connection.send(packet));
		}
	}

	public static Collection<ServerPlayer> all(MinecraftServer server) {
		return PlayerLookup.all(server);
	}

	public static Collection<ServerPlayer> world(ServerLevel world) {
		return PlayerLookup.world(world);
	}

	public static Collection<ServerPlayer> tracking(ServerLevel world, ChunkPos pos) {
		return PlayerLookup.tracking(world, pos);
	}

	public static Collection<ServerPlayer> tracking(Entity entity) {
		return PlayerLookup.tracking(entity);
	}

	public static Collection<ServerPlayer> tracking(BlockEntity blockEntity) {
		return PlayerLookup.tracking(blockEntity);
	}

	public static Collection<ServerPlayer> tracking(ServerLevel world, BlockPos pos) {
		return PlayerLookup.tracking(world, pos);
	}

	public static Collection<ServerPlayer> around(ServerLevel world, Vec3 pos, double radius) {
		return PlayerLookup.around(world, pos, radius);
	}

	public static Collection<ServerPlayer> around(ServerLevel world, Vec3i pos, double radius) {
		return PlayerLookup.around(world, pos, radius);
	}
}
