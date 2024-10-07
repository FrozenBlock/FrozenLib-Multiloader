package net.frozenblock.lib.platform;

import java.util.ServiceLoader;
import net.frozenblock.lib.FrozenLibLogUtils;
import net.frozenblock.lib.platform.api.IEntryPointHelper;
import net.frozenblock.lib.platform.api.IFPlatformHelper;
import net.frozenblock.lib.platform.api.IModProtocol;
import net.frozenblock.lib.platform.api.IPacketHelper;
import net.frozenblock.lib.platform.api.IRegistryHelper;
import net.frozenblock.lib.platform.api.ITintRegistryHelper;

/**
 * FrozenLib provides some integrated platform helpers for your MultiLoader mods that can be used to invoke specific methods based on the loader you're in.
 * See each helper for further information
 */
public class FrozenLibPlatformHelper {
	/**
	 * The basic Helper.
	 * From here, you can get mostly loader information, like the loader name, the environment (Client/Server),
	 * if a mod is loaded, and so on
	 */
	public static final IFPlatformHelper HELPER = load(IFPlatformHelper.class);
	/**
	 * A helper for SENDING packets.
	 * If you want to REGISTER packets, you should use {@link #REGISTRY}
	 */
	public static final IPacketHelper PACKET = load(IPacketHelper.class);

	/**
	 * A Helper for registration (Most stuff can be done on the common side, but some can't
	 */
	public static final IRegistryHelper REGISTRY = load(IRegistryHelper.class);

	/**
	 * A simple helper to register block/item tints
	 */
	public static final ITintRegistryHelper TINT_REGISTRY = load(ITintRegistryHelper.class);
	/**
	 * A helper for mod protocols.
	 * */
	public static final IModProtocol PROTOCOL = load(IModProtocol.class);
	/**
	 * A helper to get entrypoints
	 * */
	public static final IEntryPointHelper ENTRY_POINT = load(IEntryPointHelper.class);

	/**
	 * A method to create a custom helper.
	 * Feel free to create your interface wherever you want, and load it with this.
	 * Keep in mind to put a {@code src/main/resources/META-INF/services/your.helper.interface.package.IMyHelper}
	 * in your Fabric/Neo environments and then put inside the package + class for your fabric instance of that loader.
	 */
	public static <T> T load(Class<T> clazz) {
		final T loadedService = ServiceLoader.load(clazz)
			.findFirst()
			.orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
		FrozenLibLogUtils.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
		return loadedService;
	}
}
