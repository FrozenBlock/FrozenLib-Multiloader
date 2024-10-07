package net.frozenblock.lib.platform.impl;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

@FunctionalInterface
public interface ICPlayPayloadHandler<T extends CustomPacketPayload> {
	void handle(T packet, ClientPayloadContext context);
}
