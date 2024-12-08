package net.frozenblock.lib;

import net.frozenblock.lib.platform.api.PlatformHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.helpers.NOPLogger;

public class FrozenLibConstants {
	@ApiStatus.Internal
	public static final String PROJECT_ID = "FrozenLib";
	@ApiStatus.Internal
	public static final String MOD_ID = "frozenlib";
	/**
	 * NeoForge's packet version
	 */
	@ApiStatus.Internal
	public static final String PACKET_VERSION = "1";
	@ApiStatus.Internal
	public static final NOPLogger LOGGER4 = NOPLogger.NOP_LOGGER;
	@ApiStatus.Internal
	public static final int DATA_VERSION = 2;
	/**
	 * Whether the current instance is running in datagen mode.
	 */
	public static final boolean IS_DATAGEN = isDatagen();
	// MOD LOADERS
	public static final boolean IS_FABRIC = hasMod("fabricloader") && !hasMod("quilt_loader") && !hasMod("connector");
	public static final boolean IS_QUILT = hasMod("quilt_loader");
	public static final boolean IS_FORGE = PlatformHelper.isNeoForge() || hasMod("connector");
	// EXTERNAL MODS
	public static final boolean HAS_ARCHITECTURY = hasMod("architectury");
	public static final boolean HAS_C2ME = hasMod("c2me");
	public static final boolean HAS_CLOTH_CONFIG = hasMod("cloth-config") || hasMod("cloth_config"); // Cloth's id uses an underscore on Forge/NeoForge
	public static final boolean HAS_INDIUM = hasMod("indium");
	public static final boolean HAS_IRIS = hasMod("iris");
	public static final boolean HAS_MOONLIGHT_LIB = hasMod("moonlight");
	public static final boolean HAS_SIMPLE_COPPER_PIPES = hasMod("copper_pipe");
	public static final boolean HAS_SODIUM = hasMod("sodium");
	public static final boolean HAS_TERRABLENDER = hasMod("terrablender");
	public static final boolean HAS_TERRALITH = hasMod("terralith");
	public static final boolean HAS_WILDER_WILD = hasMod("wilderwild");
	public static final boolean HAS_TRAILIER_TALES = hasMod("trailiertales");
	/**
	 * Used for features that may be unstable and crash in public builds.
	 * <p>
	 * It's smart to use this for at least registries.
	 */
	@ApiStatus.Internal
	public static boolean UNSTABLE_LOGGING = PlatformHelper.isDevelopmentEnvironment();
	/**
	 * This is set to true when {@link Bootstrap#bootStrap()} is finished.
	 */
	public static boolean isInitialized;
	public static boolean useNewDripstoneLiquid = false;

	@ApiStatus.Internal
	private static boolean isDatagen() {
		return PlatformHelper.isDatagen();
	}

	public static boolean hasMod(String modID) {
		return PlatformHelper.isModLoaded(modID);
	}

	@ApiStatus.Internal
	@Contract("_ -> new")
	@NotNull
	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(FrozenLibConstants.MOD_ID, path);
	}

	@ApiStatus.Internal
	@NotNull
	public static String string(String path) {
		return id(path).toString();
	}
}
