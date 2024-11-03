package net.frozenblock.lib.platform.api.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.LoadingModList;
import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.util.List;

public class PlatformHelperImpl {

	public static String getPlatformName() {
		return "NeoForge";
	}

	public static boolean isFabric() {
		return false;
	}

	public static boolean isNeoForge() {
		return true;
	}

	public static boolean isModLoaded(String modId) {
		for (var mod : LoadingModList.get().getMods()) {
			if (mod.getModId().equals(modId)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isDevelopmentEnvironment() {
		return !FMLLoader.isProduction();
	}

	public static boolean isDatagen() {
		var runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		List<String> jvmArgs = runtimeMXBean.getInputArguments();
		jvmArgs.forEach(System.out::println);
		return jvmArgs.stream().anyMatch(string -> string.contains("--output"));
	}

	public static Path getGameDir() {
		return FMLLoader.getGamePath();
	}

	public static Path getConfigDir() {
		return FMLLoader.getGamePath().resolve("config");
	}

	public static Dist envType() {
		return FMLLoader.getDist();
	}
}
