package net.frozenblock.lib.platform.impl;

import net.frozenblock.lib.networking.impl.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.world.entity.player.Player;

public class ClientPayloadContext {
	private final Player player;
	private final PacketSender sender;

	public ClientPayloadContext(Player player, Connection connection) {
		this.player = player;
		this.sender = new PacketSender(connection);
	}

	public Player player() {
		return player;
	}

    public Minecraft client() {
		return Minecraft.getInstance();
	}

	public PacketSender responseSender() {
		return this.sender;
	}
}
