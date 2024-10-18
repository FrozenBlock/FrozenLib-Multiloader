package net.frozenblock.lib.resource.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

public interface SimpleResourceReloadListener<T> extends PreparableReloadListener {

	@Override
	default @NotNull CompletableFuture<Void> reload(
		@NotNull PreparationBarrier barrier,
		@NotNull ResourceManager manager,
		@NotNull ProfilerFiller getProfiler,
		@NotNull ProfilerFiller setProfiler,
		@NotNull Executor backgroundExecutor,
		@NotNull Executor gameExecutor
	) {
		return load(manager, getProfiler, backgroundExecutor).thenCompose(barrier::wait).thenCompose(
			o -> apply(o, manager, setProfiler, backgroundExecutor)
		);
	}

	CompletableFuture<T> load(ResourceManager manager, ProfilerFiller profiler, Executor executor);

	CompletableFuture<Void> apply(T data, ResourceManager manager, ProfilerFiller profiler, Executor executor);
}
