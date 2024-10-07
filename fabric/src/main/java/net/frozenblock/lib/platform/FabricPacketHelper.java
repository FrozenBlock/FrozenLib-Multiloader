package net.frozenblock.lib.platform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.frozenblock.lib.platform.api.IPacketHelper;
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

public class FabricPacketHelper implements IPacketHelper {

	@Override
	public void sendToServer(CustomPacketPayload payload, CustomPacketPayload @NotNull ... payloads) {
		ClientPlayNetworking.send(payload);
		for (var p : payloads) {
			ClientPlayNetworking.send(p);
		}
	}

	@Override
	public void sendToPlayer(ServerPlayer player, CustomPacketPayload payload, CustomPacketPayload @NotNull ... payloads) {
		ServerPlayNetworking.send(player, payload);
		for (var p : payloads) {
			ServerPlayNetworking.send(player, p);
		}
	}

	@Override
	public void sendToAllPlayers(MinecraftServer server, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		List<Packet<?>> packetsToSend = new ArrayList<>();
		packetsToSend.add(new ClientboundCustomPayloadPacket(payload));
		Arrays.stream(payloads).toList().forEach(payload1 -> packetsToSend.add(new ClientboundCustomPayloadPacket(payload1)));

		this.all(server).forEach(player -> {
			packetsToSend.forEach(packet -> player.connection.send(packet));
		});
	}

	@Override
	public void sendToPlayersInLevel(ServerLevel world, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		List<Packet<?>> packetsToSend = new ArrayList<>();
		packetsToSend.add(new ClientboundCustomPayloadPacket(payload));
		Arrays.stream(payloads).toList().forEach(payload1 -> packetsToSend.add(new ClientboundCustomPayloadPacket(payload1)));

		this.world(world).forEach(player -> {
			packetsToSend.forEach(packet -> player.connection.send(packet));
		});
	}

	@Override
	public void sendToPlayersNear(
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

		this.around(level, new Vec3(x, y, z), radius).forEach(player -> {
			packetsToSend.forEach(packet -> player.connection.send(packet));
		});
	}

	@Override
	public void sendToPlayersTrackingBlockPos(ServerLevel level, BlockPos blockPos, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		List<Packet<?>> packetsToSend = new ArrayList<>();
		packetsToSend.add(new ClientboundCustomPayloadPacket(payload));
		Arrays.stream(payloads).toList().forEach(payload1 -> packetsToSend.add(new ClientboundCustomPayloadPacket(payload1)));

		this.tracking(level, blockPos).forEach(player -> {
			packetsToSend.forEach(packet -> player.connection.send(packet));
		});
	}

	@Override
	public void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos chunkPos, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		List<Packet<?>> packetsToSend = new ArrayList<>();
		packetsToSend.add(new ClientboundCustomPayloadPacket(payload));
		Arrays.stream(payloads).toList().forEach(payload1 -> packetsToSend.add(new ClientboundCustomPayloadPacket(payload1)));

		this.tracking(level, chunkPos).forEach(player -> {
			packetsToSend.forEach(packet -> player.connection.send(packet));
		});
	}

	@Override
	public void sendToPlayersTrackingEntity(Entity entity, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		List<Packet<?>> packetsToSend = new ArrayList<>();
		packetsToSend.add(new ClientboundCustomPayloadPacket(payload));
		Arrays.stream(payloads).toList().forEach(payload1 -> packetsToSend.add(new ClientboundCustomPayloadPacket(payload1)));

		this.tracking(entity).forEach(player -> {
			packetsToSend.forEach(packet -> player.connection.send(packet));
		});
	}

	@Override
	public void sendToPlayersTrackingEntityAndSelf(Entity entity, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		List<Packet<?>> packetsToSend = new ArrayList<>();
		packetsToSend.add(new ClientboundCustomPayloadPacket(payload));
		Arrays.stream(payloads).toList().forEach(payload1 -> packetsToSend.add(new ClientboundCustomPayloadPacket(payload1)));

		this.tracking(entity).forEach(player -> {
			packetsToSend.forEach(packet -> player.connection.send(packet));
		});

		if (entity instanceof ServerPlayer player) {
			packetsToSend.forEach(packet -> player.connection.send(packet));
		}
	}

	@Override
	public Collection<ServerPlayer> all(MinecraftServer server) {
		return PlayerLookup.all(server);
	}

	@Override
	public Collection<ServerPlayer> world(ServerLevel world) {
		return PlayerLookup.world(world);
	}

	@Override
	public Collection<ServerPlayer> tracking(ServerLevel world, ChunkPos pos) {
		return PlayerLookup.tracking(world, pos);
	}

	@Override
	public Collection<ServerPlayer> tracking(Entity entity) {
		return PlayerLookup.tracking(entity);
	}

	@Override
	public Collection<ServerPlayer> tracking(BlockEntity blockEntity) {
		return PlayerLookup.tracking(blockEntity);
	}

	@Override
	public Collection<ServerPlayer> tracking(ServerLevel world, BlockPos pos) {
		return PlayerLookup.tracking(world, pos);
	}

	@Override
	public Collection<ServerPlayer> around(ServerLevel world, Vec3 pos, double radius) {
		return PlayerLookup.around(world, pos, radius);
	}

	@Override
	public Collection<ServerPlayer> around(ServerLevel world, Vec3i pos, double radius) {
		return PlayerLookup.around(world, pos, radius);
	}
}
