package net.frozenblock.lib;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod(value = FrozenLibConstants.MOD_ID, dist = Dist.DEDICATED_SERVER)
public class FrozenLibNeoForgeServer {
	public FrozenLibNeoForgeServer(@NotNull IEventBus eventBus, ModContainer container) {
		FrozenLib.onInitializeServer();
	}
}
