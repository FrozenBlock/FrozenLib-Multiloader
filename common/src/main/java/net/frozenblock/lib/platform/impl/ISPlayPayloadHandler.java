package net.frozenblock.lib.platform.impl;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

@FunctionalInterface
public interface ISPlayPayloadHandler<T extends CustomPacketPayload> {
	void handle(T packet, ServerPayloadContext context);
}
