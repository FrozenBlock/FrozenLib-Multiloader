package net.frozenblock.lib.platform.api;

import dev.architectury.injectables.annotations.ExpectPlatform;
import java.util.List;

public class EntryPointHelper {
	/**
	 * Allows invoking custom entry points, which Fabric handles with its own api, and NeoForge with our custom entrypoint system.
	 * */
	@ExpectPlatform
	public static <T> List<T> getEntrypoints(String namespace, Class<T> type) {
		throw new AssertionError();
	}
}
