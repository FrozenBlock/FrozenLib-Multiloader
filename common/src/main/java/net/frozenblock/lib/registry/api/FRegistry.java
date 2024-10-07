package net.frozenblock.lib.registry.api;

import lombok.experimental.UtilityClass;
import net.frozenblock.lib.sided.neoforge.neoforge.api.NeoForgeFreezer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;


/**
 * FRegistry makes sure that your registries are not frozen when adding stuff.
 * */
@UtilityClass
public class FRegistry {
	public static <V, T extends V> T register(Registry<? super T> registry, String name, T value) {
		NeoForgeFreezer.unfreeze(registry);
		return Registry.register(registry, name, value);
	}

	public static <V, T extends V> T register(Registry<V> registry, ResourceKey<V> key, T value) {
		NeoForgeFreezer.unfreeze(registry);
		return Registry.register(registry, key, value);
	}

	public static <V, T extends V> T register(Registry<V> registry, ResourceLocation name, T value) {
		NeoForgeFreezer.unfreeze(registry);
		return Registry.register(registry, name, value);
	}
}
