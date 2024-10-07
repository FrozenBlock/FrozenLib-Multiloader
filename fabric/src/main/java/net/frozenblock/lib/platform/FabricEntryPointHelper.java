package net.frozenblock.lib.platform;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.loader.api.FabricLoader;
import net.frozenblock.lib.FrozenLibLogUtils;
import net.frozenblock.lib.platform.api.IEntryPointHelper;

public class FabricEntryPointHelper implements IEntryPointHelper {
	@Override
	public <T> List<T> getEntrypoints(String namespace, Class<T> type) {
		final ArrayList<T> entries = new ArrayList<>();
		FabricLoader.getInstance().getEntrypointContainers(namespace, type).forEach(entry -> {
			try {
				T element = entry.getEntrypoint();
				entries.add(element);
			} catch (Throwable throwable) {
				FrozenLibLogUtils.logError("Error during entrypoint collection", throwable);
			}
		});
		return entries;
	}
}
