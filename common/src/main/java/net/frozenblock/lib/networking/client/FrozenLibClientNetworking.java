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

package net.frozenblock.lib.networking.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import net.frozenblock.lib.FrozenLibLogUtils;
import net.frozenblock.lib.cape.api.CapeUtil;
import net.frozenblock.lib.cape.client.impl.ClientCapeData;
import net.frozenblock.lib.cape.impl.networking.CapeCustomizePacket;
import net.frozenblock.lib.cape.impl.networking.LoadCapeRepoPacket;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.frozenblock.lib.config.impl.networking.ConfigSyncPacket;
import net.frozenblock.lib.debug.client.impl.DebugRenderManager;
import net.frozenblock.lib.debug.networking.GoalDebugRemovePayload;
import net.frozenblock.lib.debug.networking.ImprovedGameEventDebugPayload;
import net.frozenblock.lib.debug.networking.ImprovedGameEventListenerDebugPayload;
import net.frozenblock.lib.debug.networking.ImprovedGoalDebugPayload;
import net.frozenblock.lib.env.api.EnvType;
import net.frozenblock.lib.env.api.Environment;
import net.frozenblock.lib.file.transfer.FileTransferPacket;
import net.frozenblock.lib.file.transfer.client.ServerTexture;
import net.frozenblock.lib.item.impl.CooldownInterface;
import net.frozenblock.lib.item.impl.network.CooldownChangePacket;
import net.frozenblock.lib.item.impl.network.CooldownTickCountPacket;
import net.frozenblock.lib.item.impl.network.ForcedCooldownPacket;
import net.frozenblock.lib.platform.api.PacketHelper;
import net.frozenblock.lib.platform.api.RegistryHelper;
import net.frozenblock.lib.platform.impl.ClientPayloadContext;
import net.frozenblock.lib.screenshake.api.client.ScreenShaker;
import net.frozenblock.lib.screenshake.impl.network.EntityScreenShakePacket;
import net.frozenblock.lib.screenshake.impl.network.RemoveEntityScreenShakePacket;
import net.frozenblock.lib.screenshake.impl.network.RemoveScreenShakePacket;
import net.frozenblock.lib.screenshake.impl.network.ScreenShakePacket;
import net.frozenblock.lib.sound.api.FlyBySoundHub;
import net.frozenblock.lib.sound.api.instances.RestrictedMovingSound;
import net.frozenblock.lib.sound.api.instances.RestrictedMovingSoundLoop;
import net.frozenblock.lib.sound.api.instances.RestrictedStartingSound;
import net.frozenblock.lib.sound.api.instances.distance_based.FadingDistanceSwitchingSound;
import net.frozenblock.lib.sound.api.instances.distance_based.RestrictedMovingFadingDistanceSwitchingSound;
import net.frozenblock.lib.sound.api.instances.distance_based.RestrictedMovingFadingDistanceSwitchingSoundLoop;
import net.frozenblock.lib.sound.api.networking.FadingDistanceSwitchingSoundPacket;
import net.frozenblock.lib.sound.api.networking.FlyBySoundPacket;
import net.frozenblock.lib.sound.api.networking.LocalPlayerSoundPacket;
import net.frozenblock.lib.sound.api.networking.LocalSoundPacket;
import net.frozenblock.lib.sound.api.networking.MovingFadingDistanceSwitchingRestrictionSoundPacket;
import net.frozenblock.lib.sound.api.networking.MovingRestrictionSoundPacket;
import net.frozenblock.lib.sound.api.networking.StartingMovingRestrictionSoundLoopPacket;
import net.frozenblock.lib.sound.api.predicate.SoundPredicate;
import net.frozenblock.lib.spotting_icons.impl.EntitySpottingIconInterface;
import net.frozenblock.lib.spotting_icons.impl.SpottingIconPacket;
import net.frozenblock.lib.spotting_icons.impl.SpottingIconRemovePacket;
import net.frozenblock.lib.wind.api.ClientWindManager;
import net.frozenblock.lib.wind.api.WindDisturbance;
import net.frozenblock.lib.wind.api.WindDisturbanceLogic;
import net.frozenblock.lib.wind.impl.networking.WindAccessPacket;
import net.frozenblock.lib.wind.impl.networking.WindDisturbancePacket;
import net.frozenblock.lib.wind.impl.networking.WindSyncPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public final class FrozenLibClientNetworking {

	@ApiStatus.Internal
	public static void registerClientReceivers() {
		for (Config<?> config : ConfigRegistry.getAllConfigs()) {
			ConfigRegistry.setSyncData(config, null);
			config.setSynced(false);
		}

		RegistryHelper.registerClientReceiver(LocalPlayerSoundPacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveLocalPlayerSoundPacket);
		RegistryHelper.registerClientReceiver(LocalSoundPacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveLocalSoundPacket);
		RegistryHelper.registerClientReceiver(StartingMovingRestrictionSoundLoopPacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveStartingMovingRestrictionSoundLoopPacket);
		RegistryHelper.registerClientReceiver(MovingRestrictionSoundPacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveMovingRestrictionSoundPacket);
		RegistryHelper.registerClientReceiver(FadingDistanceSwitchingSoundPacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveFadingDistanceSwitchingSoundPacket);
		RegistryHelper.registerClientReceiver(MovingFadingDistanceSwitchingRestrictionSoundPacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveMovingFadingDistanceSwitchingSoundPacket);
		RegistryHelper.registerClientReceiver(FlyBySoundPacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveFlyBySoundPacket);
		RegistryHelper.registerClientReceiver(CooldownChangePacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveCooldownChangePacket);
		RegistryHelper.registerClientReceiver(ForcedCooldownPacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveForcedCooldownPacket);
		RegistryHelper.registerClientReceiver(CooldownTickCountPacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveCooldownTickCountPacket);
		RegistryHelper.registerClientReceiver(ScreenShakePacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveScreenShakePacket);
		RegistryHelper.registerClientReceiver(EntityScreenShakePacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveScreenShakeFromEntityPacket);
		RegistryHelper.registerClientReceiver(RemoveScreenShakePacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveRemoveScreenShakePacket);
		RegistryHelper.registerClientReceiver(RemoveEntityScreenShakePacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveRemoveScreenShakeFromEntityPacket);
		RegistryHelper.registerClientReceiver(SpottingIconPacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveIconPacket);
		RegistryHelper.registerClientReceiver(SpottingIconRemovePacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveIconRemovePacket);
		RegistryHelper.registerClientReceiver(WindSyncPacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveWindSyncPacket);
		RegistryHelper.registerClientReceiver(WindDisturbancePacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveWindDisturbancePacket);

		RegistryHelper.registerClientReceiver(FileTransferPacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveFileTransferPacket);
		RegistryHelper.registerClientReceiver(CapeCustomizePacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveCapeCustomizePacket);

		RegistryHelper.registerClientReceiver(LoadCapeRepoPacket.PACKET_TYPE, FrozenLibClientNetworking::onReceiveLoadCapeRepoPacket);

		RegistryHelper.registerClientReceiver(ConfigSyncPacket.PACKET_TYPE, (packet, ctx) -> ConfigSyncPacket.receive(packet, null));

		// DEBUG
		RegistryHelper.registerClientReceiver(ImprovedGoalDebugPayload.PACKET_TYPE, DebugRenderManager::onReceiveGoalDebugPacket);
		RegistryHelper.registerClientReceiver(GoalDebugRemovePayload.PACKET_TYPE, DebugRenderManager::onReceiveGoalDebugRemovePacket);
		RegistryHelper.registerClientReceiver(ImprovedGameEventListenerDebugPayload.PACKET_TYPE, DebugRenderManager::onReceiveGameEventListenerDebugPacket);
		RegistryHelper.registerClientReceiver(ImprovedGameEventDebugPayload.PACKET_TYPE, DebugRenderManager::onReceiveGameEventDebugPacket);
		RegistryHelper.registerClientReceiver(WindAccessPacket.PACKET_TYPE, DebugRenderManager::onReceiveWindAccessPacket);
	}

	@ApiStatus.Internal
	public static void onReceiveLocalPlayerSoundPacket(@NotNull LocalPlayerSoundPacket packet, @NotNull ClientPayloadContext ctx) {
		LocalPlayer player = Minecraft.getInstance().player;
		Minecraft.getInstance().getSoundManager().play(
			new EntityBoundSoundInstance(packet.sound().value(), SoundSource.PLAYERS, packet.volume(), packet.pitch(), player, ctx.client().level.random.nextLong())
		);
	}

	@ApiStatus.Internal
	public static void onReceiveLocalSoundPacket(@NotNull LocalSoundPacket packet, @NotNull ClientPayloadContext ctx) {
		ClientLevel level = ctx.client().level;
		Vec3 pos = packet.pos();
		level.playLocalSound(pos.x, pos.y, pos.z, packet.sound().value(), packet.category(), packet.volume(), packet.pitch(), packet.distanceDelay());
	}

	@ApiStatus.Internal
	public static void onReceiveStartingMovingRestrictionSoundLoopPacket(@NotNull StartingMovingRestrictionSoundLoopPacket packet, @NotNull ClientPayloadContext ctx) {
		ClientLevel level = ctx.client().level;
		Entity entity = level.getEntity(packet.id());
		if (entity != null) {
			SoundPredicate.LoopPredicate<Entity> predicate = SoundPredicate.getPredicate(packet.predicateId());
			Minecraft.getInstance().getSoundManager().play(new RestrictedStartingSound<>(
				entity, packet.startingSound().value(), packet.category(), packet.volume(), packet.pitch(), predicate, packet.stopOnDeath(),
				new RestrictedMovingSoundLoop<>(
					entity, packet.sound().value(), packet.category(), packet.volume(), packet.pitch(), predicate, packet.stopOnDeath()
				)
			));
		}
	}

	@ApiStatus.Internal
	public static void onReceiveMovingRestrictionSoundPacket(@NotNull MovingRestrictionSoundPacket packet, @NotNull ClientPayloadContext ctx) {
		ClientLevel level = ctx.client().level;
		Entity entity = level.getEntity(packet.id());
		if (entity != null) {
			SoundPredicate.LoopPredicate<Entity> predicate = SoundPredicate.getPredicate(packet.predicateId());
			if (packet.looping()) {
				Minecraft.getInstance().getSoundManager().play(
					new RestrictedMovingSoundLoop<>(entity, packet.sound().value(), packet.category(), packet.volume(), packet.pitch(), predicate, packet.stopOnDeath())
				);
			} else {
				Minecraft.getInstance().getSoundManager().play(
					new RestrictedMovingSound<>(entity, packet.sound().value(), packet.category(), packet.volume(), packet.pitch(), predicate, packet.stopOnDeath())
				);
			}
		}
	}

	@ApiStatus.Internal
	public static void onReceiveFadingDistanceSwitchingSoundPacket(@NotNull FadingDistanceSwitchingSoundPacket packet, @NotNull ClientPayloadContext ctx) {
		ctx.client().getSoundManager().play(
			new FadingDistanceSwitchingSound(
				packet.closeSound().value(),
				packet.category(),
				packet.volume(),
				packet.pitch(),
				packet.fadeDist(),
				packet.maxDist(),
				packet.volume(),
				false,
				packet.pos()
			)
		);
		ctx.client().getSoundManager().play(
			new FadingDistanceSwitchingSound(
				packet.farSound().value(),
				packet.category(),
				packet.volume(),
				packet.pitch(),
				packet.fadeDist(),
				packet.maxDist(),
				packet.volume(),
				true,
				packet.pos()
			)
		);
	}

	@ApiStatus.Internal
	public static void onReceiveMovingFadingDistanceSwitchingSoundPacket(@NotNull MovingFadingDistanceSwitchingRestrictionSoundPacket packet, @NotNull ClientPayloadContext ctx) {
		SoundManager soundManager = ctx.client().getSoundManager();
		ClientLevel level = ctx.client().level;
		Entity entity = level.getEntity(packet.id());
		if (entity != null) {
			SoundPredicate.LoopPredicate<Entity> predicate = SoundPredicate.getPredicate(packet.predicateId());
			if (packet.looping()) {
				soundManager.play(
					new RestrictedMovingFadingDistanceSwitchingSoundLoop<>(entity, packet.closeSound().value(), packet.category(), packet.volume(), packet.pitch(), predicate, packet.stopOnDeath(), packet.fadeDist(), packet.maxDist(), packet.volume(), false));
				soundManager.play(
					new RestrictedMovingFadingDistanceSwitchingSoundLoop<>(entity, packet.farSound().value(), packet.category(), packet.volume(), packet.pitch(), predicate, packet.stopOnDeath(), packet.fadeDist(), packet.maxDist(), packet.volume(), true));
			} else {
				soundManager.play(
					new RestrictedMovingFadingDistanceSwitchingSound<>(
						entity,
						packet.closeSound().value(),
						packet.category(),
						packet.volume(),
						packet.pitch(),
						predicate,
						packet.stopOnDeath(),
						packet.fadeDist(),
						packet.maxDist(),
						packet.volume(),
						false
					)
				);
				soundManager.play(
					new RestrictedMovingFadingDistanceSwitchingSound<>(
						entity,
						packet.farSound().value(),
						packet.category(),
						packet.volume(),
						packet.pitch(),
						predicate,
						packet.stopOnDeath(),
						packet.fadeDist(),
						packet.maxDist(),
						packet.volume(),
						true
					)
				);
			}
		}
	}

	@ApiStatus.Internal
	public static void onReceiveFlyBySoundPacket(@NotNull FlyBySoundPacket packet, @NotNull ClientPayloadContext ctx) {
		ClientLevel level = (ClientLevel) ctx.player().level();
		Entity entity = level.getEntity(packet.id());
		if (entity != null) {
			FlyBySoundHub.FlyBySound flyBySound = new FlyBySoundHub.FlyBySound(packet.pitch(), packet.volume(), packet.category(), packet.sound().value());
			FlyBySoundHub.addEntity(entity, flyBySound);
		}
	}

	@ApiStatus.Internal
	public static void onReceiveCooldownChangePacket(@NotNull CooldownChangePacket packet, ClientPayloadContext ctx) {
		LocalPlayer player = Minecraft.getInstance().player;
		Item item = packet.item();
		int additional = packet.additional();
		((CooldownInterface) player.getCooldowns()).frozenLib$changeCooldown(item, additional);
	}

	@ApiStatus.Internal
	public static void onReceiveForcedCooldownPacket(@NotNull ForcedCooldownPacket packet, ClientPayloadContext ctx) {
		LocalPlayer player = Minecraft.getInstance().player;
		Item item = packet.item();
		int startTime = packet.startTime();
		int endTime = packet.endTime();
		player.getCooldowns().cooldowns.put(item, new ItemCooldowns.CooldownInstance(startTime, endTime));
	}

	@ApiStatus.Internal
	public static void onReceiveCooldownTickCountPacket(CooldownTickCountPacket packet, ClientPayloadContext ctx) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player != null) player.getCooldowns().tickCount = packet.count();
	}

	@ApiStatus.Internal
	public static void onReceiveScreenShakePacket(@NotNull ScreenShakePacket packet, @NotNull ClientPayloadContext ctx) {
		float intensity = packet.intensity();
		int duration = packet.duration();
		int fallOffStart = packet.falloffStart();
		Vec3 pos = packet.pos();
		float maxDistance = packet.maxDistance();
		int ticks = packet.ticks();

		ClientLevel level = ctx.client().level;
		ScreenShaker.addShake(level, intensity, duration, fallOffStart, pos, maxDistance, ticks);
	}

	@ApiStatus.Internal
	public static void onReceiveScreenShakeFromEntityPacket(@NotNull EntityScreenShakePacket packet, @NotNull ClientPayloadContext ctx) {
		int id = packet.entityId();
		float intensity = packet.intensity();
		int duration = packet.duration();
		int fallOffStart = packet.falloffStart();
		float maxDistance = packet.maxDistance();
		int ticks = packet.ticks();

		Entity entity = ctx.client().level.getEntity(id);
		if (entity != null) {
			ScreenShaker.addShake(entity, intensity, duration, fallOffStart, maxDistance, ticks);
		}
		// TODO: [Liuk] look! Networking isn't working... this never runs.
	}

	@ApiStatus.Internal
	public static void onReceiveRemoveScreenShakePacket(RemoveScreenShakePacket packet, ClientPayloadContext ctx) {
		ScreenShaker.SCREEN_SHAKES.removeIf(clientScreenShake -> !(clientScreenShake instanceof ScreenShaker.ClientEntityScreenShake));
	}

	@ApiStatus.Internal
	public static void onReceiveRemoveScreenShakeFromEntityPacket(@NotNull RemoveEntityScreenShakePacket packet, @NotNull ClientPayloadContext ctx) {
		int id = packet.entityId();
		ClientLevel level = ctx.client().level;
		Entity entity = level.getEntity(id);
		if (entity != null) {
			ScreenShaker.SCREEN_SHAKES.removeIf(
				clientScreenShake -> clientScreenShake instanceof ScreenShaker.ClientEntityScreenShake entityScreenShake && entityScreenShake.getEntity() == entity
			);
		}
	}

	@ApiStatus.Internal
	public static void onReceiveIconPacket(@NotNull SpottingIconPacket packet, @NotNull ClientPayloadContext ctx) {
		int id = packet.entityId();
		ResourceLocation texture = packet.texture();
		float startFade = packet.startFade();
		float endFade = packet.endFade();
		ResourceLocation predicate = packet.restrictionID();

		Entity entity = ctx.client().level.getEntity(id);
		if (entity instanceof EntitySpottingIconInterface livingEntity) {
			livingEntity.frozenLib$getSpottingIconManager().setIcon(texture, startFade, endFade, predicate);
		}
	}

	@ApiStatus.Internal
	public static void onReceiveIconRemovePacket(@NotNull SpottingIconRemovePacket packet, @NotNull ClientPayloadContext ctx) {
		int id = packet.entityId();
		Entity entity = ctx.client().level.getEntity(id);
		if (entity instanceof EntitySpottingIconInterface livingEntity) {
			livingEntity.frozenLib$getSpottingIconManager().icon = null;
		}
	}

	@ApiStatus.Internal
	public static void onReceiveWindSyncPacket(@NotNull WindSyncPacket packet, ClientPayloadContext ctx) {
		ClientWindManager.time = packet.windTime();
		ClientWindManager.setSeed(packet.seed());
		ClientWindManager.overrideWind = packet.override();
		ClientWindManager.commandWind = packet.commandWind();
		ClientWindManager.hasInitialized = true;
	}

	@ApiStatus.Internal
	public static void onReceiveWindDisturbancePacket(@NotNull WindDisturbancePacket packet, @NotNull ClientPayloadContext ctx) {
		ClientLevel level = ctx.client().level;
		long posOrID = packet.posOrID();
		Optional<WindDisturbanceLogic> disturbanceLogic = WindDisturbanceLogic.getWindDisturbanceLogic(packet.id());
		if (disturbanceLogic.isPresent()) {
			WindDisturbanceLogic.SourceType sourceType = packet.disturbanceSourceType();
			Optional source = Optional.empty();
			if (sourceType == WindDisturbanceLogic.SourceType.ENTITY) {
				source = Optional.ofNullable(level.getEntity((int) posOrID));
			} else if (sourceType == WindDisturbanceLogic.SourceType.BLOCK_ENTITY) {
				source = Optional.ofNullable(level.getBlockEntity(BlockPos.of(posOrID)));
			}

			ClientWindManager.addWindDisturbance(
				new WindDisturbance(
					source,
					packet.origin(),
					packet.affectedArea(),
					disturbanceLogic.get()
				)
			);
		}
	}

	@ApiStatus.Internal
	public static void onReceiveFileTransferPacket(@NotNull FileTransferPacket packet, ClientPayloadContext ctx) {
		if (packet.request()) {
			Path path = ctx.client().gameDirectory.toPath().resolve(packet.transferPath()).resolve(packet.fileName());
			try {
				FileTransferPacket fileTransferPacket = FileTransferPacket.create(packet.transferPath(), path.toFile());
				PacketHelper.sendToServer(fileTransferPacket);
			} catch (IOException ignored) {
				FrozenLibLogUtils.LOGGER.error("Unable to create and send transfer packet for file {}!", packet.fileName());
			}
		} else {
			try {
				Path path = ctx.client().gameDirectory.toPath().resolve(packet.transferPath()).resolve(packet.fileName());
				FileUtils.copyInputStreamToFile(new ByteArrayInputStream(packet.bytes()), path.toFile());
				ServerTexture serverTexture = ServerTexture.WAITING_TEXTURES.get(packet.transferPath() + "/" + packet.fileName());
				if (serverTexture != null) {
					serverTexture.runFutureForTexture();
				}
			} catch (IOException ignored) {
				FrozenLibLogUtils.LOGGER.error("Unable save transferred file {}!", packet.fileName());
			}
		}
	}

	@ApiStatus.Internal
	public static void onReceiveCapeCustomizePacket(@NotNull CapeCustomizePacket packet, ClientPayloadContext ctx) {
		UUID uuid = packet.getPlayerUUID();
		if (packet.isEnabled()) {
			ClientCapeData.setCapeForUUID(uuid, packet.getCapeId());
		} else {
			ClientCapeData.removeCapeForUUID(uuid);
		}
	}

	@ApiStatus.Internal
	public static void onReceiveLoadCapeRepoPacket(@NotNull LoadCapeRepoPacket packet, ClientPayloadContext ctx) {
		CapeUtil.registerCapesFromURL(packet.capeRepo());
	}

	public static boolean notConnected() {
		Minecraft minecraft = Minecraft.getInstance();
		ClientPacketListener listener = minecraft.getConnection();
		if (listener == null) return true;

		LocalPlayer player = Minecraft.getInstance().player;
		return player == null;
	}

	public static boolean connectedToLan() {
		if (notConnected()) return false;
		ServerData serverData = Minecraft.getInstance().getCurrentServer();
		if (serverData == null) return false;
		return serverData.isLan();
	}

}
