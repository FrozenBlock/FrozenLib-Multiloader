package net.frozenblock.lib.resource.impl;

import java.util.ArrayList;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public class ServerReloadListener {
	private final ArrayList<PreparableReloadListener> REGISTERED = new ArrayList<>();

	public void registerReloadListener(PreparableReloadListener listener) {

	}
}
