package net.frozenblock.lib.resource.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.resource.impl.ServerReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.jetbrains.annotations.NotNull;

public class ResourceListenerAPI {

	@Environment(EnvType.CLIENT)
	public static @NotNull ReloadableResourceManager getClient() {
		return ((ReloadableResourceManager) Minecraft.getInstance().getResourceManager());
	}

	protected static final ServerReloadListener RELOAD_LISTENER = new ServerReloadListener();

	public static @NotNull ServerReloadListener getServer() {
		return RELOAD_LISTENER;
	}
}
