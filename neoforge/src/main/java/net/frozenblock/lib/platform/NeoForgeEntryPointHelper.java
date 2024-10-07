package net.frozenblock.lib.platform;

import com.electronwill.nightconfig.core.AbstractConfig;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.frozenblock.lib.FrozenLibLogUtils;
import net.frozenblock.lib.platform.api.IEntryPointHelper;
import net.neoforged.fml.loading.LoadingModList;

public class NeoForgeEntryPointHelper implements IEntryPointHelper {
	@Override
	public <T> List<T> getEntrypoints(String namespace, Class<T> type) {
		final var modList = LoadingModList.get().getMods();
		final ArrayList<T> out = new ArrayList<>();
		for(var mod : modList) {
			var frozenlib = mod.getConfig().<HashMap<String, AbstractConfig>>getConfigElement("frozenlib");
			if(frozenlib.isPresent()) {
				if(frozenlib.get().containsKey("entrypoints")) {
					var entrypoints = frozenlib.get().get("entrypoints");
					if(entrypoints.contains(namespace)) {
						var eps = entrypoints.<ArrayList<String>>get(namespace);
						for(var ep : eps) {
							try {
								Class<?> clazz = Class.forName(ep);
								final T obj = type.cast(clazz.getDeclaredConstructor().newInstance());
								out.add(obj);
							} catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
								FrozenLibLogUtils.logError("Error during entrypoint collection", e);
							}
						}
					}
				}
			}
		}
		return out;
	}
}
