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

package net.frozenblock.lib.sound.impl.networking;

import lombok.experimental.UtilityClass;
import net.frozenblock.lib.platform.api.PacketHelper;
import net.frozenblock.lib.sound.impl.EntityLoopingFadingDistanceSoundInterface;
import net.frozenblock.lib.sound.impl.EntityLoopingSoundInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class FrozenLibSoundPackets {

	public static void createAndSendLocalSound(
		@NotNull Level level,
		BlockPos pos,
		Holder<SoundEvent> sound,
		SoundSource source,
		float volume,
		float pitch,
		boolean distanceDelay
	) {
		if (level instanceof ServerLevel serverLevel) {
			PacketHelper.sendToPlayersTrackingBlockPos(
				serverLevel,
				pos,
				new LocalSoundPacket(Vec3.atCenterOf(pos), sound, source, volume, pitch, distanceDelay)
			);
		}
	}

	public static void createAndSendLocalSound(
		@NotNull Level level,
		double x,
		double y,
		double z,
		Holder<SoundEvent> sound,
		SoundSource source,
		float volume,
		float pitch,
		boolean distanceDelay
	) {
		if (level instanceof ServerLevel serverLevel) {
			PacketHelper.sendToPlayersTrackingBlockPos(
				serverLevel,
				BlockPos.containing(x, y, z),
				new LocalSoundPacket(new Vec3(x, y, z), sound, source, volume, pitch, distanceDelay)
			);
		}
	}

	public static void createAndSendFlybySound(
		@NotNull Level level,
		Entity entity,
		Holder<SoundEvent> sound,
		SoundSource category,
		float volume,
		float pitch
	) {
		if (level instanceof ServerLevel serverLevel) {
			PacketHelper.sendToPlayersTrackingEntityAndSelf(
				entity,
				new FlyBySoundPacket(entity.getId(), sound, category, volume, pitch)
			);
		}
	}

    public static void createAndSendMovingRestrictionSound(
		@NotNull Level level,
		Entity entity,
		Holder<SoundEvent> sound,
		SoundSource category,
		float volume,
		float pitch,
		ResourceLocation predicate,
		boolean stopOnDeath
	) {
		if (level instanceof ServerLevel serverLevel) {
			PacketHelper.sendToPlayersTrackingEntityAndSelf(
				entity,
				new MovingRestrictionSoundPacket(
					entity.getId(),
					sound,
					category,
					volume,
					pitch,
					predicate,
					stopOnDeath,
					false
				)
			);
        }
    }

    public static void createAndSendMovingRestrictionLoopingSound(
		@NotNull Level level,
		Entity entity,
		Holder<SoundEvent> sound,
		SoundSource category,
		float volume,
		float pitch,
		ResourceLocation predicate,
		boolean stopOnDeath
	) {
		if (level instanceof ServerLevel serverLevel && entity instanceof EntityLoopingSoundInterface soundInterface) {
			PacketHelper.sendToPlayersTrackingEntity(
				entity,
				new MovingRestrictionSoundPacket(
					entity.getId(),
					sound,
					category,
					volume,
					pitch,
					predicate,
					stopOnDeath,
					true
				)
			);
			soundInterface.frozenLib$addSound(sound.unwrapKey().orElseThrow().location(), category, volume, pitch, predicate, stopOnDeath);
        }
    }

    public static void createAndSendMovingRestrictionLoopingSound(
		ServerPlayer player,
		@NotNull Entity entity,
		Holder<SoundEvent> sound,
		SoundSource category,
		float volume,
		float pitch,
		ResourceLocation id,
		boolean stopOnDeath
	) {
		PacketHelper.sendToPlayer(
			player,
			new MovingRestrictionSoundPacket(
				entity.getId(),
				sound,
				category,
				volume,
				pitch,
				id,
				stopOnDeath,
				true
			)
		);
    }

    public static void createAndSendMovingRestrictionLoopingFadingDistanceSound(
		@NotNull Level level,
		Entity entity,
		Holder<SoundEvent> sound,
		Holder<SoundEvent> sound2,
		SoundSource category,
		float volume,
		float pitch,
		ResourceLocation predicate,
		boolean stopOnDeath,
		float fadeDist,
		float maxDist
	) {
		if (level instanceof ServerLevel serverLevel && entity instanceof EntityLoopingFadingDistanceSoundInterface soundInterface) {
			PacketHelper.sendToPlayersTrackingEntityAndSelf(
				entity,
				new MovingFadingDistanceSwitchingRestrictionSoundPacket(
					entity.getId(),
					sound,
					sound2,
					category,
					volume,
					pitch,
					fadeDist,
					maxDist,
					predicate,
					stopOnDeath,
					true
				)
			);
			soundInterface.frozenLib$addFadingDistanceSound(
				sound.unwrapKey().orElseThrow().location(),
				sound2.unwrapKey().orElseThrow().location(),
				category,
				volume,
				pitch,
				predicate,
				stopOnDeath,
				fadeDist,
				maxDist
			);
        }
    }

    public static void createAndSendMovingRestrictionLoopingFadingDistanceSound(
		ServerPlayer player,
		@NotNull Entity entity,
		Holder<SoundEvent> sound,
		Holder<SoundEvent> sound2,
		SoundSource category,
		float volume,
		float pitch,
		ResourceLocation predicate,
		boolean stopOnDeath,
		float fadeDist,
		float maxDist
	) {
		PacketHelper.sendToPlayer(
			player,
			new MovingFadingDistanceSwitchingRestrictionSoundPacket(
				entity.getId(),
				sound,
				sound2,
				category,
				volume,
				pitch,
				fadeDist,
				maxDist,
				predicate,
				stopOnDeath,
				true
			)
		);
    }

    public static void createAndSendMovingRestrictionFadingDistanceSound(
		ServerPlayer player,
		@NotNull Entity entity,
		Holder<SoundEvent> sound,
		Holder<SoundEvent> sound2,
		SoundSource category,
		float volume,
		float pitch,
		ResourceLocation predicate,
		boolean stopOnDeath,
		float fadeDist,
		float maxDist
	) {
		PacketHelper.sendToPlayer(
			player,
			new MovingFadingDistanceSwitchingRestrictionSoundPacket(
				entity.getId(),
				sound,
				sound2,
				category,
				volume,
				pitch,
				fadeDist,
				maxDist,
				predicate,
				stopOnDeath,
				false
			)
		);
    }

    public static void createAndSendFadingDistanceSound(
		@NotNull Level level,
		Vec3 pos,
		Holder<SoundEvent> sound,
		Holder<SoundEvent> sound2,
		SoundSource category,
		float volume,
		float pitch,
		float fadeDist,
		float maxDist
	) {
		if (level instanceof ServerLevel serverLevel) {
			PacketHelper.sendToPlayersTrackingBlockPos(
				serverLevel,
				BlockPos.containing(pos),
				new FadingDistanceSwitchingSoundPacket(
					pos,
					sound,
					sound2,
					category,
					volume,
					pitch,
					fadeDist,
					maxDist
				)
			);
        }
    }

    public static void createAndSendStartingMovingRestrictionLoopingSound(
		@NotNull Level level,
		Entity entity,
		Holder<SoundEvent> startingSound,
		Holder<SoundEvent> sound,
		SoundSource category,
		float volume,
		float pitch,
		ResourceLocation predicate,
		boolean stopOnDeath
	) {
		if (level instanceof ServerLevel serverLevel && entity instanceof EntityLoopingSoundInterface soundInterface) {
			PacketHelper.sendToPlayersTrackingEntityAndSelf(
				entity,
				new StartingMovingRestrictionSoundLoopPacket(entity.getId(), startingSound, sound, category, volume, pitch, predicate, stopOnDeath)
			);
			soundInterface.frozenLib$addSound(sound.unwrapKey().orElseThrow().location(), category, volume, pitch, predicate, stopOnDeath);
        }
    }

    public static void createAndSendStartingMovingRestrictionLoopingSound(
		ServerPlayer player,
		@NotNull Entity entity,
		Holder<SoundEvent> startingSound,
		Holder<SoundEvent> sound,
		SoundSource category,
		float volume,
		float pitch,
		ResourceLocation predicate,
		boolean stopOnDeath
	) {
		PacketHelper.sendToPlayer(
			player,
			new StartingMovingRestrictionSoundLoopPacket(
				entity.getId(),
				startingSound,
				sound,
				category,
				volume,
				pitch,
				predicate,
				stopOnDeath
			)
		);
    }

	public static void createAndSendLocalPlayerSound(ServerPlayer player, Holder<SoundEvent> sound, float volume, float pitch) {
		PacketHelper.sendToPlayer(player, new LocalPlayerSoundPacket(sound, volume, pitch));
	}

}
