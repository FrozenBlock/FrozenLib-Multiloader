package net.frozenblock.lib.platform.api;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class ModProtocolHelper {
	@ExpectPlatform
	public static void loadVersions() {
		throw new AssertionError();
	}
}
