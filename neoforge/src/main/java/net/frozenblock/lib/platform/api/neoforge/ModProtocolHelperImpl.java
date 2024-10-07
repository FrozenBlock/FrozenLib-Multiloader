package net.frozenblock.lib.platform.api.neoforge;

import com.electronwill.nightconfig.core.AbstractConfig;
import net.neoforged.fml.loading.LoadingModList;
import java.util.HashMap;

public class ModProtocolHelperImpl {

	public static void loadVersions() {
		for (var mod : LoadingModList.get().getMods()) {
			var frozenlib = mod.getConfig().<HashMap<String, AbstractConfig>>getConfigElement("frozenlib");
			if (frozenlib.isEmpty()) continue;
			var registry = frozenlib.get().get("registry");
			if (registry == null) continue;
			//TODO: [Liuk] [Treetrain1] understand the quiltmc protocol system
		}
	}
}
