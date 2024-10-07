package net.frozenblock.lib.networking.impl;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public class PacketSender {

	private final Connection connection;

	public PacketSender(Connection connection) {
		this.connection = connection;
	}

	public Packet<?> createPacket(CustomPacketPayload payload) {
		return new ServerboundCustomPayloadPacket(payload);
	}

	public void sendPacket(Packet<?> packet) {
		sendPacket(packet, null);
	}

	public void sendPacket(CustomPacketPayload payload) {
		sendPacket(createPacket(payload));
	}

	public void sendPacket(Packet<?> packet, @Nullable PacketSendListener callback) {
		this.connection.send(packet, callback);
	}

	public void sendPacket(CustomPacketPayload payload, @Nullable PacketSendListener callback) {
		sendPacket(createPacket(payload), callback);
	}

	public void disconnect(Component disconnectReason) {
		this.connection.disconnect(disconnectReason);
	}
}
