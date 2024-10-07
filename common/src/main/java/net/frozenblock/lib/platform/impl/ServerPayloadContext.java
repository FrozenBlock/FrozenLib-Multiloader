package net.frozenblock.lib.platform.impl;

import net.frozenblock.lib.networking.impl.PacketSender;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class ServerPayloadContext {
	private final ServerPlayer player;
	private final Connection connection;
	private final PacketSender sender;

	public ServerPayloadContext(ServerPlayer player, Connection connection) {
		this.player = player;
		this.connection = connection;
		this.sender = new PacketSender(connection);
	}

	public ServerPlayer player() {
		return player;
	}

	public MinecraftServer server() {
		return player.server;
	}

    public Connection connection() {
		return this.connection;
	}

	public PacketSender responseSender() {
		return this.sender;
	}
}
