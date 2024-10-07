package net.frozenblock.lib.event.impl;

import net.minecraft.server.network.ConfigurationTask;

public interface FrozenServerConfigurationNetworkHandler {
	void addTask(ConfigurationTask task);
	void completeTask(ConfigurationTask.Type key);
}
