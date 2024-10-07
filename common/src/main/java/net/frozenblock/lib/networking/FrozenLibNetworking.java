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

package net.frozenblock.lib.networking;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;
import net.frozenblock.lib.cape.api.CapeUtil;
import net.frozenblock.lib.cape.impl.ServerCapeData;
import net.frozenblock.lib.cape.impl.networking.CapeCustomizePacket;
import net.frozenblock.lib.cape.impl.networking.LoadCapeRepoPacket;
import net.frozenblock.lib.config.frozenlib_config.FrozenLibConfig;
import net.frozenblock.lib.config.impl.networking.ConfigSyncPacket;
import net.frozenblock.lib.debug.networking.GoalDebugRemovePayload;
import net.frozenblock.lib.debug.networking.ImprovedGameEventDebugPayload;
import net.frozenblock.lib.debug.networking.ImprovedGameEventListenerDebugPayload;
import net.frozenblock.lib.debug.networking.ImprovedGoalDebugPayload;
import net.frozenblock.lib.debug.networking.StructureDebugRequestPayload;
import net.frozenblock.lib.env.api.EnvType;
import net.frozenblock.lib.event.event.FrozenLibPacketRegistryEvents;
import net.frozenblock.lib.event.event.PlayerJoinEvents;
import net.frozenblock.lib.file.transfer.FileTransferPacket;
import net.frozenblock.lib.item.impl.network.CooldownChangePacket;
import net.frozenblock.lib.item.impl.network.CooldownTickCountPacket;
import net.frozenblock.lib.item.impl.network.ForcedCooldownPacket;
import net.frozenblock.lib.platform.FrozenLibPlatformHelper;
import net.frozenblock.lib.screenshake.impl.network.EntityScreenShakePacket;
import net.frozenblock.lib.screenshake.impl.network.RemoveEntityScreenShakePacket;
import net.frozenblock.lib.screenshake.impl.network.RemoveScreenShakePacket;
import net.frozenblock.lib.screenshake.impl.network.ScreenShakePacket;
import net.frozenblock.lib.sound.api.networking.FadingDistanceSwitchingSoundPacket;
import net.frozenblock.lib.sound.api.networking.FlyBySoundPacket;
import net.frozenblock.lib.sound.api.networking.LocalPlayerSoundPacket;
import net.frozenblock.lib.sound.api.networking.LocalSoundPacket;
import net.frozenblock.lib.sound.api.networking.MovingFadingDistanceSwitchingRestrictionSoundPacket;
import net.frozenblock.lib.sound.api.networking.MovingRestrictionSoundPacket;
import net.frozenblock.lib.sound.api.networking.StartingMovingRestrictionSoundLoopPacket;
import net.frozenblock.lib.spotting_icons.impl.SpottingIconPacket;
import net.frozenblock.lib.spotting_icons.impl.SpottingIconRemovePacket;
import net.frozenblock.lib.wind.api.WindManager;
import net.frozenblock.lib.wind.impl.networking.WindAccessPacket;
import net.frozenblock.lib.wind.impl.networking.WindDisturbancePacket;
import net.frozenblock.lib.wind.impl.networking.WindSyncPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.frozenblock.resource.loader.api.ResourceLoaderEvents;

public final class FrozenLibNetworking {
	private FrozenLibNetworking() {}

	public static void registerNetworking() {
		FrozenLibPacketRegistryEvents.REGISTER.invoke(FrozenLibPacketRegistryEvents.Register::registerPackets);

		PlayerJoinEvents.AFTER_PLAYER_ADDED_TO_LEVEL.register(((server, serverLevel, player) -> {
			WindManager windManager = WindManager.getWindManager(serverLevel);
			windManager.sendSyncToPlayer(windManager.createSyncPacket(), player);
			ServerCapeData.sendAllCapesToPlayer(player);
		}));

		PlayerJoinEvents.AFTER_JOIN_SERVER.register((server, player, connection, cookie) -> {
			ConfigSyncPacket.sendS2C(player);
		});

		ResourceLoaderEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, error) -> {
			if (error != null || server == null) return;
			for (ServerPlayer player : FrozenLibPlatformHelper.PACKET.all(server)) {
				ConfigSyncPacket.sendS2C(player);
			}
		});

		final var registryHelper = FrozenLibPlatformHelper.REGISTRY;

		registryHelper.registerS2C(LocalPlayerSoundPacket.PACKET_TYPE, LocalPlayerSoundPacket.CODEC);
		registryHelper.registerS2C(LocalSoundPacket.PACKET_TYPE, LocalSoundPacket.CODEC);
		registryHelper.registerS2C(StartingMovingRestrictionSoundLoopPacket.PACKET_TYPE, StartingMovingRestrictionSoundLoopPacket.CODEC);
		registryHelper.registerS2C(MovingRestrictionSoundPacket.PACKET_TYPE, MovingRestrictionSoundPacket.CODEC);
		registryHelper.registerS2C(FadingDistanceSwitchingSoundPacket.PACKET_TYPE, FadingDistanceSwitchingSoundPacket.CODEC);
		registryHelper.registerS2C(MovingFadingDistanceSwitchingRestrictionSoundPacket.PACKET_TYPE, MovingFadingDistanceSwitchingRestrictionSoundPacket.CODEC);
		registryHelper.registerS2C(FlyBySoundPacket.PACKET_TYPE, FlyBySoundPacket.CODEC);
		registryHelper.registerS2C(CooldownChangePacket.PACKET_TYPE, CooldownChangePacket.CODEC);
		registryHelper.registerS2C(ForcedCooldownPacket.PACKET_TYPE, ForcedCooldownPacket.CODEC);
		registryHelper.registerS2C(CooldownTickCountPacket.PACKET_TYPE, CooldownTickCountPacket.CODEC);
		registryHelper.registerS2C(ScreenShakePacket.PACKET_TYPE, ScreenShakePacket.CODEC);
		registryHelper.registerS2C(EntityScreenShakePacket.PACKET_TYPE, EntityScreenShakePacket.CODEC);
		registryHelper.registerS2C(RemoveScreenShakePacket.PACKET_TYPE, RemoveScreenShakePacket.CODEC);
		registryHelper.registerS2C(RemoveEntityScreenShakePacket.PACKET_TYPE, RemoveEntityScreenShakePacket.CODEC);
		registryHelper.registerS2C(SpottingIconPacket.PACKET_TYPE, SpottingIconPacket.CODEC);
		registryHelper.registerS2C(SpottingIconRemovePacket.PACKET_TYPE, SpottingIconRemovePacket.CODEC);
		registryHelper.registerS2C(WindSyncPacket.PACKET_TYPE, WindSyncPacket.CODEC);
		registryHelper.registerS2C(WindDisturbancePacket.PACKET_TYPE, WindDisturbancePacket.CODEC);

		// File Transfer & Capes
		registryHelper.registerBidirectional(FileTransferPacket.PACKET_TYPE, FileTransferPacket.STREAM_CODEC);
		registryHelper.registerServerReceiver(FileTransferPacket.PACKET_TYPE, (packet, ctx) -> {
			if (packet.request()) {
				Path path = ctx.server().getServerDirectory().resolve(packet.transferPath()).resolve(packet.fileName());
				try {
					FileTransferPacket fileTransferPacket = FileTransferPacket.create(packet.transferPath(), path.toFile());
					FrozenLibPlatformHelper.PACKET.sendToPlayer(ctx.player(), fileTransferPacket);
				} catch (IOException ignored) {
				}
			} else {
				if (!FrozenLibConfig.FILE_TRANSFER_SERVER) return;
				try {
					Path path = ctx.server().getServerDirectory().resolve(packet.transferPath()).resolve(packet.fileName());
					FileUtils.copyInputStreamToFile(new ByteArrayInputStream(packet.bytes()), path.toFile());
				} catch (IOException ignored) {
				}
			}
		});

		registryHelper.registerBidirectional(CapeCustomizePacket.PACKET_TYPE, CapeCustomizePacket.CODEC);
		registryHelper.registerServerReceiver(CapeCustomizePacket.PACKET_TYPE, (packet, ctx) -> {
			UUID uuid = ctx.player().getUUID();
			ResourceLocation capeId = packet.getCapeId();
			if (capeId == null || CapeUtil.canPlayerUserCape(uuid, capeId)) {
				CapeCustomizePacket.sendCapeToAll(ctx.server(), uuid, capeId);
			}
		});

		registryHelper.registerS2C(LoadCapeRepoPacket.PACKET_TYPE, LoadCapeRepoPacket.STREAM_CODEC);

		registryHelper.registerBidirectional(ConfigSyncPacket.PACKET_TYPE, ConfigSyncPacket.CODEC);

		registryHelper.registerServerReceiver(ConfigSyncPacket.PACKET_TYPE, (packet, ctx) -> {
			if (ConfigSyncPacket.hasPermissionsToSendSync(ctx.player(), true))
				ConfigSyncPacket.receive(packet, ctx.player().getServer());
		});

		// DEBUG
		registryHelper.registerS2C(ImprovedGoalDebugPayload.PACKET_TYPE, ImprovedGoalDebugPayload.STREAM_CODEC);
		registryHelper.registerS2C(GoalDebugRemovePayload.PACKET_TYPE, GoalDebugRemovePayload.STREAM_CODEC);
		registryHelper.registerS2C(ImprovedGameEventListenerDebugPayload.PACKET_TYPE, ImprovedGameEventListenerDebugPayload.STREAM_CODEC);
		registryHelper.registerS2C(ImprovedGameEventDebugPayload.PACKET_TYPE, ImprovedGameEventDebugPayload.STREAM_CODEC);
		registryHelper.registerS2C(WindAccessPacket.PACKET_TYPE, WindAccessPacket.STREAM_CODEC);

		registryHelper.registerC2S(StructureDebugRequestPayload.PACKET_TYPE, StructureDebugRequestPayload.STREAM_CODEC);
		registryHelper.registerServerReceiver(StructureDebugRequestPayload.PACKET_TYPE,
			(packet, ctx) -> StructureDebugRequestPayload.sendBack(ctx.player(), ctx.player().serverLevel(), packet.chunkPos())
		);

		FrozenLibPlatformHelper.REGISTRY.finalizeAndBuildPackets();
	}

	public static void sendPacketToAllPlayers(@NotNull ServerLevel world, CustomPacketPayload payload) {
		Packet<?> packet = new ClientboundCustomPayloadPacket(payload);

		for (ServerPlayer serverPlayer : world.players()) {
			serverPlayer.connection.send(packet);
		}
	}

	public static boolean isLocalPlayer(Player player) {
		if (FrozenLibPlatformHelper.HELPER.envType() == EnvType.SERVER) return false;

		return Minecraft.getInstance().isLocalPlayer(player.getGameProfile().getId());
	}

	public static boolean connectedToIntegratedServer() {
		if (FrozenLibPlatformHelper.HELPER.envType() == EnvType.SERVER) return false;
		Minecraft minecraft = Minecraft.getInstance();
		return minecraft.hasSingleplayerServer();
	}

	/**
	 * @return if the client is connected to any server
	 */
	public static boolean connectedToServer() {
		if (FrozenLibPlatformHelper.HELPER.envType() == EnvType.SERVER) return false;
		Minecraft minecraft = Minecraft.getInstance();
		ClientPacketListener listener = minecraft.getConnection();
		if (listener == null) return false;
		return listener.getConnection().isConnected();
	}

	/**
	 * @return if the current server is multiplayer (LAN/dedicated) or not (singleplayer)
	 */
	public static boolean isMultiplayer() {
		if (FrozenLibPlatformHelper.HELPER.envType() == EnvType.SERVER) return true;
		return !Minecraft.getInstance().isSingleplayer();
	}

}
