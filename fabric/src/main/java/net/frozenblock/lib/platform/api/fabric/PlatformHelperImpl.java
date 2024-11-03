package net.frozenblock.lib.platform.api.fabric;

import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.Path;
import java.util.Arrays;

public class PlatformHelperImpl {

	public static String getPlatformName() {
		return "Fabric";
	}

	public static boolean isFabric() {
		return true;
	}

	public static boolean isNeoForge() {
		return false;
	}

	public static boolean isModLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	public static boolean isDevelopmentEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	public static boolean isDatagen() {
		return Arrays.stream(
			FabricLoader.getInstance().getLaunchArguments(true)
		).toList().stream().anyMatch(string -> string.contains("datagen"));
	}

	public static Path getGameDir() {
		return FabricLoader.getInstance().getGameDir();
	}

	public static Path getConfigDir() {
		return FabricLoader.getInstance().getConfigDir();
	}

	public static Object envType() {
		return FabricLoader.getInstance().getEnvironmentType();
	}
}
