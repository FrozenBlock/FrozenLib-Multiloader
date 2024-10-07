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

package net.frozenblock.lib.item.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.frozenblock.lib.item.api.tag.FrozenLibItemTags;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@WrapWithCondition(
		method = "startUsingItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;gameEvent(Lnet/minecraft/core/Holder;)V"
		)
	)
	private boolean frozenLib$preventStartingGameEvent(LivingEntity entity, Holder<GameEvent> event) {
		return !entity.getUseItem().is(FrozenLibItemTags.NO_USE_GAME_EVENTS);
	}

	@WrapWithCondition(
		method = "stopUsingItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;gameEvent(Lnet/minecraft/core/Holder;)V"
		)
	)
	private boolean frozenLib$preventStoppingGameEvent(LivingEntity entity, Holder<GameEvent> event) {
		return !entity.getUseItem().is(FrozenLibItemTags.NO_USE_GAME_EVENTS);
	}

}
