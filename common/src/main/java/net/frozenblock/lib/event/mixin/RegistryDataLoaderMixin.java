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

package net.frozenblock.lib.event.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.frozenblock.lib.event.event.RegistryDataLoaderEvents;
import net.frozenblock.lib.worldgen.biome.api.FrozenGrassColorModifiers;
import net.frozenblock.lib.worldgen.biome.impl.BiomeInterface;
import net.frozenblock.lib.worldgen.biome.impl.FrozenGrassColorModifier;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(value = RegistryDataLoader.class, priority = 50)
public class RegistryDataLoaderMixin {

	@WrapOperation(
		method = "loadContentsFromNetwork",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/WritableRegistry;register(Lnet/minecraft/resources/ResourceKey;Ljava/lang/Object;Lnet/minecraft/core/RegistrationInfo;)Lnet/minecraft/core/Holder$Reference;"
		)
	)
	private static Holder.Reference frozenLib$loadFromNetwork(
		WritableRegistry instance, ResourceKey resourceKey, Object object, RegistrationInfo registrationInfo, Operation<Holder.Reference> original
	) {
		RegistryDataLoaderEvents.REGISTER_RESOURCE.invoke(registerResource -> registerResource.onRegisterResource(object, resourceKey, true));
		return original.call(instance, resourceKey, object, registrationInfo);
	}

	@WrapOperation(
		method = "loadElementFromResource",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/WritableRegistry;register(Lnet/minecraft/resources/ResourceKey;Ljava/lang/Object;Lnet/minecraft/core/RegistrationInfo;)Lnet/minecraft/core/Holder$Reference;"
		)
	)
	private static Holder.Reference frozenLib$loadFromResource(
		WritableRegistry instance, ResourceKey resourceKey, Object object, RegistrationInfo registrationInfo, Operation<Holder.Reference> original
	) {
		RegistryDataLoaderEvents.REGISTER_RESOURCE.invoke(registerResource -> registerResource.onRegisterResource(object, resourceKey, false));
		return original.call(instance, resourceKey, object, registrationInfo);
	}
}
