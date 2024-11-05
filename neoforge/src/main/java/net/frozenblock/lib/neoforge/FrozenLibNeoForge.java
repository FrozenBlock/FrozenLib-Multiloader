package net.frozenblock.lib.neoforge;

import net.frozenblock.lib.FrozenLib;
import net.frozenblock.lib.FrozenLibConstants;
import net.frozenblock.lib.event.event.FrozenLibPacketRegistryEvents;
import net.frozenblock.lib.neoforge.event.api.FrozenLibNeoForgePacketRegisterEvent;
import net.frozenblock.lib.neoforge.event.impl.FlibCommonNeoEvents;
import net.frozenblock.lib.neoforge.event.impl.FrozenLibModEvents;
import net.frozenblock.lib.worldgen.surface.impl.terrablender.FrozenTerraBlenderCompat;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;

@Mod(FrozenLibConstants.MOD_ID)
public class FrozenLibNeoForge {

	public FrozenLibNeoForge(@NotNull IEventBus eventBus, ModContainer container) {
		/* NEOFORGE SPECIFIC STUFF */
		NeoForge.EVENT_BUS.register(FlibCommonNeoEvents.class);
		eventBus.register(FrozenLibModEvents.class);
		if (FrozenLibConstants.HAS_TERRABLENDER) {
			FrozenTerraBlenderCompat.onTerraBlenderInitialized();
		}

		FrozenLibPacketRegistryEvents.REGISTER.register(() -> ModLoader.postEvent(new FrozenLibNeoForgePacketRegisterEvent()));

		/* FROZEN LIB INIT */
		FrozenLib.onInitialize();
	}
}
