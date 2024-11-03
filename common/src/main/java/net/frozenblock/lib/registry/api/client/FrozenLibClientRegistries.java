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

package net.frozenblock.lib.registry.api.client;


import net.frozenblock.lib.FrozenLibConstants;
import net.frozenblock.lib.entity.api.rendering.EntityTextureOverride;
import net.frozenblock.lib.environment.api.EnvType;
import net.frozenblock.lib.environment.api.Environment;
import net.frozenblock.lib.platform.api.RegistryHelper;
import net.frozenblock.lib.platform.api.NeoForgeFreezer;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public class FrozenLibClientRegistries {
	public static final Registry<EntityTextureOverride> ENTITY_TEXTURE_OVERRIDE = create(FrozenLibConstants.id("entity_texture_override"), true);

	public static void register() {
		register(ENTITY_TEXTURE_OVERRIDE);
	}

	private static <T> Registry<T> create(ResourceLocation id, boolean sync) {
		final ResourceKey<Registry<T>> key = ResourceKey.createRegistryKey(id);
		return RegistryHelper.createBuilder(key, sync);
	}

	private static void register(Registry<?> registry) {
		NeoForgeFreezer.unfreeze(BuiltInRegistries.REGISTRY);
		((WritableRegistry) BuiltInRegistries.REGISTRY).register(registry.key(), registry, RegistrationInfo.BUILT_IN);
		NeoForgeFreezer.freeze(BuiltInRegistries.REGISTRY);
	}
}
