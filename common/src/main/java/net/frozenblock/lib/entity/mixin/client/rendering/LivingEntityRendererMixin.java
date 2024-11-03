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

package net.frozenblock.lib.entity.mixin.client.rendering;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.frozenblock.lib.entity.api.rendering.EntityTextureOverride;
import net.frozenblock.lib.environment.api.EnvType;
import net.frozenblock.lib.environment.api.Environment;
import net.frozenblock.lib.registry.api.client.FrozenLibClientRegistries;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {

	@Shadow
	protected M model;

	protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
		super(context);
	}


	@WrapOperation(
		method = "getRenderType",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/resources/ResourceLocation;"
		)
	)
	private ResourceLocation frozenLib$getEasterEgg(
		LivingEntityRenderer<T, M> instance, Entity entity, Operation<ResourceLocation> original,
		T livingEntity
	) {
		for (EntityTextureOverride override : FrozenLibClientRegistries.ENTITY_TEXTURE_OVERRIDE) {
			if (override.type() == entity.getType()) {
				if (override.condition().condition(livingEntity)) {
					return override.texture();
				}
			}
		}
		return original.call(instance, entity);
	}

}
