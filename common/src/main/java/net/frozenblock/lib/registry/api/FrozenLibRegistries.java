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

package net.frozenblock.lib.registry.api;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Lifecycle;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import net.frozenblock.lib.FrozenLibConstants;
import net.frozenblock.lib.integration.api.ModIntegration;
import net.frozenblock.lib.integration.api.ModIntegrationSupplier;
import net.frozenblock.lib.platform.api.RegistryHelper;
import net.frozenblock.lib.sound.api.predicate.SoundPredicate;
import net.frozenblock.lib.spotting_icons.api.SpottingIconPredicate;
import net.frozenblock.lib.wind.api.WindDisturbanceLogic;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class FrozenLibRegistries {
	private static final List<Registry<?>> FROZENLIB_REGISTRIES = new ArrayList<>();
	public static final ResourceKey<Registry<ModIntegrationSupplier<?>>> MOD_INTEGRATION_REGISTRY = ResourceKey.createRegistryKey(FrozenLibConstants.id("mod_integration"));
	public static final ResourceKey<Registry<SoundPredicate<?>>> SOUND_PREDICATE_REGISTRY = ResourceKey.createRegistryKey(FrozenLibConstants.id("sound_predicate"));
	public static final ResourceKey<Registry<SoundPredicate<?>>> SOUND_PREDICATE_UNSYNCED_REGISTRY = ResourceKey.createRegistryKey(FrozenLibConstants.id("sound_predicate_unsynced"));
	public static final ResourceKey<Registry<SpottingIconPredicate<?>>> SPOTTING_ICON_PREDICATE_REGISTRY = ResourceKey.createRegistryKey(FrozenLibConstants.id("spotting_icon_predicate"));
	public static final ResourceKey<Registry<WindDisturbanceLogic<?>>> WIND_DISTURBANCE_LOGIC_REGISTRY = ResourceKey.createRegistryKey(FrozenLibConstants.id("wind_disturbance_logic"));
	public static final ResourceKey<Registry<WindDisturbanceLogic<?>>> WIND_DISTURBANCE_LOGIC_UNSYNCED_REGISTRY = ResourceKey.createRegistryKey(FrozenLibConstants.id("wind_disturbance_logic_unsynced"));

	public static final MappedRegistry<ModIntegrationSupplier<?>> MOD_INTEGRATION = createSimple(MOD_INTEGRATION_REGISTRY, Lifecycle.stable(), null,
		registry -> Registry.register(registry, FrozenLibConstants.id("frozenlib_dummy"), new ModIntegrationSupplier<>(() -> new ModIntegration("frozenlib_dummy") {
			@Override
			public void init() {
			}
		},
			"frozenlib_dummy"
		))
	);

	public static final MappedRegistry<SoundPredicate<?>> SOUND_PREDICATE = createSimple(SOUND_PREDICATE_REGISTRY, Lifecycle.stable(), FrozenLibRegistryAttribute.SYNCED,
		registry -> Registry.register(registry, FrozenLibConstants.id("dummy"), new SoundPredicate<>(() -> entity -> false))
	);

	public static final MappedRegistry<SoundPredicate<?>> SOUND_PREDICATE_UNSYNCED = createSimple(SOUND_PREDICATE_UNSYNCED_REGISTRY, Lifecycle.stable(), null,
		registry -> Registry.register(registry, FrozenLibConstants.id("dummy"), new SoundPredicate<>(() -> entity -> false))
	);

	public static final MappedRegistry<SpottingIconPredicate<?>> SPOTTING_ICON_PREDICATE = createSimple(SPOTTING_ICON_PREDICATE_REGISTRY, Lifecycle.stable(), FrozenLibRegistryAttribute.SYNCED,
		registry -> Registry.register(registry, FrozenLibConstants.id("dummy"), new SpottingIconPredicate<>(entity -> false))
	);

	public static final MappedRegistry<WindDisturbanceLogic<?>> WIND_DISTURBANCE_LOGIC = createSimple(WIND_DISTURBANCE_LOGIC_REGISTRY, Lifecycle.stable(), FrozenLibRegistryAttribute.SYNCED,
		registry -> Registry.register(registry, FrozenLibConstants.id("dummy"), new WindDisturbanceLogic(WindDisturbanceLogic.defaultPredicate()))
	);

	public static final MappedRegistry<WindDisturbanceLogic<?>> WIND_DISTURBANCE_LOGIC_UNSYNCED = createSimple(WIND_DISTURBANCE_LOGIC_UNSYNCED_REGISTRY, Lifecycle.stable(), null,
		registry -> Registry.register(registry, FrozenLibConstants.id("dummy"), new WindDisturbanceLogic(WindDisturbanceLogic.defaultPredicate()))
	);

	@NotNull
	public static HolderLookup.Provider vanillaRegistries() {
		return VanillaRegistries.createLookup();
	}

	public static void initRegistry() {
	}

	public static @NotNull ImmutableList<Registry<?>> getRegistries() {
		return ImmutableList.copyOf(FROZENLIB_REGISTRIES);
	}

	public static <T> MappedRegistry<T> createSimple(ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle) {
		return createSimple(key, lifecycle, null);
	}

	public static <T> MappedRegistry<T> createSimple(ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle, FrozenLibRegistryAttribute attribute) {
		return createSimple(key, lifecycle, attribute, null);
	}

	public static <T> MappedRegistry<T> createSimple(
		ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle, FrozenLibRegistryAttribute attribute, BuiltInRegistries.RegistryBootstrap<T> bootstrap
	) {
		MappedRegistry<T> newRegistry = RegistryHelper.createBuildAndRegister(key, lifecycle, attribute, bootstrap);
		FROZENLIB_REGISTRIES.add(newRegistry);
		return newRegistry;
	}
}
