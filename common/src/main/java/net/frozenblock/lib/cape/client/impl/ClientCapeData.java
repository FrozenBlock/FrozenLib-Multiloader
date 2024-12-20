/*
 * Copyright (C) 2024 FrozenBlock
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.lib.cape.client.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.cape.api.CapeUtil;
import net.frozenblock.lib.cape.impl.Cape;
import net.frozenblock.lib.cape.impl.networking.CapeCustomizePacket;
import net.frozenblock.lib.config.frozenlib_config.FrozenLibConfig;
import net.frozenblock.lib.event.event.client.ClientEntityEvents;
import net.frozenblock.lib.event.event.client.ClientLifecycleEvents;
import net.frozenblock.lib.event.event.client.ClientPlayConnectionEvents;
import net.frozenblock.lib.platform.api.PacketHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public class ClientCapeData {
	private static final Map<UUID, Cape> CAPES_IN_WORLD = new HashMap<>();

	/**
	 * Gets the texture of a player's cape in the world, if possible.
	 *
	 * @param uuid The player's UUID.
	 * @return the player's FrozenLib cape texture, if available.
	 */
	public static Optional<ResourceLocation> getCapeTexture(UUID uuid) {
		return Optional.ofNullable(CAPES_IN_WORLD.get(uuid)).map(Cape::texture);
	}

	public static void setCapeForUUID(UUID uuid, ResourceLocation capeId) {
		CapeUtil.getCape(capeId).ifPresentOrElse(cape -> setPlayerCape(uuid, Optional.of(cape)), () -> removeCapeForUUID(uuid));
	}

	public static void removeCapeForUUID(UUID uuid) {
		setPlayerCape(uuid, Optional.empty());
	}

	private static void setPlayerCape(UUID uuid, @NotNull Optional<Cape> cape) {
		cape.ifPresentOrElse(cape1 -> CAPES_IN_WORLD.put(uuid, cape1), () -> CAPES_IN_WORLD.remove(uuid));
		ClientLevel level = Minecraft.getInstance().level;
		if (level != null && level.getPlayerByUUID(uuid) instanceof AbstractClientPlayerCapeInterface capeInterface) {
			capeInterface.frozenLib$setCape(cape.map(Cape::texture).orElse(null));
		}
	}

	public static void init() {
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> CAPES_IN_WORLD.clear());
		ClientPlayConnectionEvents.DISCONNECT.register((clientPacketListener, minecraft) -> CAPES_IN_WORLD.clear());
		ClientPlayConnectionEvents.JOIN.register((clientPacketListener, packetSender, minecraft) ->
			PacketHelper.sendToServer(CapeCustomizePacket.createPacket(minecraft.getUser().getProfileId(), ResourceLocation.parse(FrozenLibConfig.get().cape))));
		ClientEntityEvents.ENTITY_LOAD.register((entity, clientLevel) -> {
			if (entity instanceof AbstractClientPlayerCapeInterface capeInterface) {
				getCapeTexture(entity.getUUID()).ifPresent(capeInterface::frozenLib$setCape);
			}
		});
	}
}
