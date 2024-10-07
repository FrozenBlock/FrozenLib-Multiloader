package net.frozenblock.lib.platform;

import java.nio.file.Path;
import java.util.Arrays;
import net.fabricmc.loader.api.FabricLoader;
import net.frozenblock.lib.env.api.EnvType;
import net.frozenblock.lib.platform.api.IFPlatformHelper;

public class FabricPlatformHelper implements IFPlatformHelper {

	@Override
	public String getPlatformName() {
		return "Fabric";
	}

	@Override
	public boolean isFabric() {
		return true;
	}

	@Override
	public boolean isNeoForge() {
		return false;
	}

	@Override
	public boolean isModLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@Override
	public boolean isDatagen() {
		return Arrays.stream(
			FabricLoader.getInstance().getLaunchArguments(true)
		).toList().stream().anyMatch(string -> string.contains("datagen"));
	}

	@Override
	public Path getGameDir() {
		return FabricLoader.getInstance().getGameDir();
	}

	@Override
	public Path getConfigDir() {
		return FabricLoader.getInstance().getConfigDir();
	}

	@Override
	public EnvType envType() {
		return FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT ? EnvType.CLIENT : EnvType.SERVER;
	}
}
