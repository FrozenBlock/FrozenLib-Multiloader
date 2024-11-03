package net.frozenblock.lib.platform.api;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public class PlatformHelper {
	/**
	 * Gets the name of the current platform
	 *
	 * @return The name of the current platform.
	 */
	@ExpectPlatform
	public static String getPlatformName() {
		throw new AssertionError();
	}

	/**
	 * @return whether the current platform is Fabric.
	 */
	@ExpectPlatform
	public static boolean isFabric() {
		throw new AssertionError();
	}

	/**
	 * @return whether the current platform is NeoForge.
	 */
	@ExpectPlatform
	public static boolean isNeoForge() {
		throw new AssertionError();
	}

	/**
	 * Checks if a mod with the given id is loaded.
	 *
	 * @param modId The mod to check if it is loaded.
	 * @return True if the mod is loaded, false otherwise.
	 */
	@ExpectPlatform
	public static boolean isModLoaded(String modId) {
		throw new AssertionError();
	}

	/**
	 * Check if the game is currently in a development environment.
	 *
	 * @return True if in a development environment, false otherwise.
	 */
	@ExpectPlatform
	public static boolean isDevelopmentEnvironment() {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static boolean isDatagen() {
		throw new AssertionError();
	}

	/**
	 * Gets the directory of the current Minecraft instance.
	 *
	 * @return the directory of the current Minecraft instance.
	 */
	@ExpectPlatform
	public static Path getGameDir() {
		throw new AssertionError();
	}

	/**
	 * Gets the config directory of the current Minecraft instance.
	 *
	 * @return the config directory of the current Minecraft instance.
	 */
	@ExpectPlatform
	public static Path getConfigDir() {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static EnvType envType() {
		throw new AssertionError();
	}

	/**
	 * Gets the name of the environment type as a string.
	 *
	 * @return The name of the environment type.
	 */
	public static @NotNull String getEnvironmentName() {
		return isDevelopmentEnvironment() ? "development" : "production";
	}
}
