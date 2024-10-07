package net.frozenblock.lib.platform;

import com.electronwill.nightconfig.core.AbstractConfig;
import java.util.HashMap;
import net.frozenblock.lib.platform.api.IModProtocol;
import net.neoforged.fml.loading.LoadingModList;

public class NeoForgeModProtocol implements IModProtocol {
	@Override
	public void loadVersions() {
		for (var mod : LoadingModList.get().getMods()) {
			var frozenlib = mod.getConfig().<HashMap<String, AbstractConfig>>getConfigElement("frozenlib");
			if (frozenlib.isEmpty()) continue;
			var registry = frozenlib.get().get("registry");
			if (registry == null) continue;
			//TODO: [Liuk] [Treetrain1] understand the quiltmc protocol system
		}
	}
}
