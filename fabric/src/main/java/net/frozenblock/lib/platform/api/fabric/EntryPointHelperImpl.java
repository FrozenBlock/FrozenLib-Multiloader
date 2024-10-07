package net.frozenblock.lib.platform.api.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.frozenblock.lib.FrozenLibLogUtils;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class EntryPointHelperImpl {
	public static <T> @NotNull List<T> getEntrypoints(String namespace, Class<T> type) {
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
