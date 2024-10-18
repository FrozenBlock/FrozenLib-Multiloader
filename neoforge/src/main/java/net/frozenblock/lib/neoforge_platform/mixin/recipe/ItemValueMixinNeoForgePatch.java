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

package net.frozenblock.lib.neoforge_platform.mixin.recipe;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Function;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Ingredient.ItemValue.class)
public class ItemValueMixinNeoForgePatch {

	@WrapOperation(
		method = "<clinit>",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;mapCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;",
			ordinal = 0
		)
	)
	private static MapCodec<Ingredient.ItemValue> frozenLib$neoForge$newCodec(
		Function<RecordCodecBuilder.Instance<Ingredient.ItemValue>, ? extends App<RecordCodecBuilder.Mu<Ingredient.ItemValue>, Ingredient.ItemValue>> builder,
		Operation<MapCodec<Ingredient.ItemValue>> original
	) {
		return RecordCodecBuilder.mapCodec(instance ->
			instance.group(
				ItemStack.SIMPLE_ITEM_CODEC.fieldOf("item").forGetter(Ingredient.ItemValue::item),
				DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY)
					.forGetter(itemValue -> itemValue.item().getComponentsPatch())
			).apply(instance, (item, patch) -> {
				item.applyComponents(patch);
				return new Ingredient.ItemValue(item);
			})
		);
	}
}
