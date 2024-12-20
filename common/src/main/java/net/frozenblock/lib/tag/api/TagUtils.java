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

package net.frozenblock.lib.tag.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import net.frozenblock.lib.math.AdvancedMath;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Contains methods related to {@link TagKey}s.
 */
@UtilityClass
public class TagUtils {

	public static <T> @NotNull List<T> getAllEntries(@NotNull TagKey<T> tag) {
		Optional<? extends Registry<?>> maybeRegistry = BuiltInRegistries.REGISTRY.getOptional(tag.registry().location());
		Objects.requireNonNull(tag);

		if (maybeRegistry.isPresent()) {
			Registry<T> registry = (Registry<T>) maybeRegistry.get();
			if (tag.isFor(registry.key())) {
				ArrayList<T> entries = new ArrayList<>();
				for (Holder<T> entry : registry.getTagOrEmpty(tag)) {
					var optionalKey = entry.unwrapKey();
					if (optionalKey.isPresent()) {
						var key = optionalKey.get();
						registry.getOptional(key).ifPresent(entries::add);
					}
				}
				if (!entries.isEmpty()) {
					return entries;
				}
			}
		}
		return List.of();
	}

    @Nullable
    public static <T> T getRandomEntry(TagKey<T> tag) {
        return getRandomEntry(AdvancedMath.random(), tag);
    }

    @Nullable
    public static <T> T getRandomEntry(RandomSource random, @NotNull TagKey<T> tag) {
        Optional<? extends Registry<?>> maybeRegistry = BuiltInRegistries.REGISTRY.getOptional(tag.registry().location());
        Objects.requireNonNull(random);
        Objects.requireNonNull(tag);

        if (maybeRegistry.isPresent()) {
            Registry<T> registry = (Registry<T>) maybeRegistry.get();
            if (tag.isFor(registry.key())) {
                ArrayList<T> entries = new ArrayList<>();
                for (Holder<T> entry : registry.getTagOrEmpty(tag)) {
                    var optionalKey = entry.unwrapKey();
                    if (optionalKey.isPresent()) {
                        var key = optionalKey.get();
                        registry.getOptional(key).ifPresent(entries::add);
                    }
                }
                if (!entries.isEmpty()) {
                    return entries.get(random.nextInt(entries.size()));
                }
            }
        }
        return null;
    }

	public static <T> boolean isIn(TagKey<T> tagKey, T entry) {
		return isIn(null, tagKey, entry);
	}

	public static <T> boolean isIn(@Nullable RegistryAccess registryManager, TagKey<T> tagKey, T entry) {
		Optional<? extends Registry<?>> maybeRegistry;
		Objects.requireNonNull(tagKey);
		Objects.requireNonNull(entry);

		if (registryManager != null) {
			maybeRegistry = registryManager.registry(tagKey.registry());
		} else {
			maybeRegistry = BuiltInRegistries.REGISTRY.getOptional(tagKey.registry().location());
		}

		if (maybeRegistry.isPresent()) {
			if (tagKey.isFor(maybeRegistry.get().key())) {
				Registry<T> registry = (Registry<T>) maybeRegistry.get();

				Optional<ResourceKey<T>> maybeKey = registry.getResourceKey(entry);

				// Check synced tag
				if (maybeKey.isPresent()) {
					return registry.getHolderOrThrow(maybeKey.get()).is(tagKey);
				}
			}
		}

		return false;
	}
}
