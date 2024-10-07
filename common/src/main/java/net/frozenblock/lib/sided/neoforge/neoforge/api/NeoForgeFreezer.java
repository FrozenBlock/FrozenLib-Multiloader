package net.frozenblock.lib.sided.neoforge.neoforge.api;

import java.lang.reflect.Method;
import net.frozenblock.lib.FrozenLibLogUtils;
import net.frozenblock.lib.platform.FrozenLibPlatformHelper;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;

public class NeoForgeFreezer {

	/**
	 * Unfreezes a registry, only if you're playing from NeoForge.<br>
	 * If the registry was frozen prior to calling this, you might want to use {@link #freeze(Registry)} to freeze it after calling this method.
	 * */
	public static void unfreeze(Registry<?> registry) {
		if (FrozenLibPlatformHelper.HELPER.isNeoForge()) {
			try {
				Method method = MappedRegistry.class.getMethod("unfreeze");
				method.invoke(registry);
			} catch (Exception e) {
				FrozenLibLogUtils.logError("Unable to unfreeze registry!", e);
			}
		}
	}

	public static void freeze(Registry<?> registry) {
		if (FrozenLibPlatformHelper.HELPER.isNeoForge()) {
			registry.freeze();
		}
	}
}
