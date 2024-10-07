package net.frozenblock.lib.platform.api;

import java.util.List;

public interface IEntryPointHelper {
	/**
	 * Allows invoking custom entry points, which Fabric handles with his own api, Neo with our custom entrypoint system
	 * */
	<T> List<T> getEntrypoints(String namespace, Class<T> type);
}
