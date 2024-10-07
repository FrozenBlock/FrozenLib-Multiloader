package net.frozenblock.lib.env.api;

public enum EnvType {
	/**
	 * The current environment being used is a client, such as the instance created when you launch Minecraft.
	 */
	CLIENT,
	/**
	 * The current environment being used is a server. This will not have access to client classes.
	 */
	SERVER;

	public boolean isClient() {
		return this == CLIENT;
	}
}
