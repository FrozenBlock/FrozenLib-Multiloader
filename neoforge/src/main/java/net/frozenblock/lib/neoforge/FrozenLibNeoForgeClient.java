package net.frozenblock.lib.neoforge;

import net.frozenblock.lib.FrozenLibClient;
import net.frozenblock.lib.FrozenLibConstants;
import net.frozenblock.lib.config.frozenlib_config.gui.FrozenLibConfigGui;
import net.frozenblock.lib.neoforge.event.impl.FlibClientNeoEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;

@Mod(value = FrozenLibConstants.MOD_ID, dist = Dist.CLIENT)
public class FrozenLibNeoForgeClient {

	public FrozenLibNeoForgeClient(@NotNull IEventBus eventBus, ModContainer container) {
		NeoForge.EVENT_BUS.register(FlibClientNeoEvents.class);
		createConfigScreen(container);
		FrozenLibClient.onInitialize();
	}

	private static void createConfigScreen(ModContainer container) {
		if (FrozenLibConstants.HAS_CLOTH_CONFIG) {
			container.registerExtensionPoint(IConfigScreenFactory.class, (modContainer, screen) -> FrozenLibConfigGui.createScreen(screen));
		}
	}
}
