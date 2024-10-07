package net.frozenblock.lib.platform.api;

import java.nio.file.Path;
import net.frozenblock.lib.env.api.EnvType;

public interface IFPlatformHelper {
	/**
	 * Gets the name of the current platform
	 *
	 * @return The name of the current platform.
	 */
	String getPlatformName();

	/**
	 * @return whether the current platform is Fabric.
	 */
	boolean isFabric();

	/**
	 * @return whether the current platform is NeoForge.
	 */
	boolean isNeoForge();

	/**
	 * Checks if a mod with the given id is loaded.
	 *
	 * @param modId The mod to check if it is loaded.
	 * @return True if the mod is loaded, false otherwise.
	 */
	boolean isModLoaded(String modId);

	/**
	 * Check if the game is currently in a development environment.
	 *
	 * @return True if in a development environment, false otherwise.
	 */
	boolean isDevelopmentEnvironment();

	boolean isDatagen();

	/**
	 * Gets the directory of the current Minecraft instance.
	 *
	 * @return the directory of the current Minecraft instance.
	 */
	Path getGameDir();

	/**
	 * Gets the config directory of the current Minecraft instance.
	 *
	 * @return the config directory of the current Minecraft instance.
	 */
	Path getConfigDir();

	EnvType envType();

	/**
	 * Gets the name of the environment type as a string.
	 *
	 * @return The name of the environment type.
	 */
	default String getEnvironmentName() {
		return isDevelopmentEnvironment() ? "development" : "production";
	}
}
