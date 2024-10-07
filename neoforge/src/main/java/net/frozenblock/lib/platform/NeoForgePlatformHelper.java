package net.frozenblock.lib.platform;

import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.util.List;
import net.frozenblock.lib.env.api.EnvType;
import net.frozenblock.lib.platform.api.IFPlatformHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.LoadingModList;

public class NeoForgePlatformHelper implements IFPlatformHelper {

	@Override
	public String getPlatformName() {
		return "NeoForge";
	}

	@Override
	public boolean isFabric() {
		return false;
	}

	@Override
	public boolean isNeoForge() {
		return true;
	}

	@Override
	public boolean isModLoaded(String modId) {
		for (var mod : LoadingModList.get().getMods()) {
			if (mod.getModId().equals(modId)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return !FMLLoader.isProduction();
	}

	@Override
	public boolean isDatagen() {
		var runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		List<String> jvmArgs = runtimeMXBean.getInputArguments();
		jvmArgs.forEach(System.out::println);
		return jvmArgs.stream().anyMatch(string -> string.contains("--output"));
	}

	@Override
	public Path getGameDir() {
		return FMLLoader.getGamePath();
	}

	@Override
	public Path getConfigDir() {
		return FMLLoader.getGamePath().resolve("config");
	}

	@Override
	public EnvType envType() {
		return FMLLoader.getDist() == Dist.CLIENT ? EnvType.CLIENT : EnvType.SERVER;
	}
}
