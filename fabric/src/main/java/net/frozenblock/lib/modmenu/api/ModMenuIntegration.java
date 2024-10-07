package net.frozenblock.lib.modmenu.api;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.frozenblock.lib.FrozenLibConstants;
import net.frozenblock.lib.config.frozenlib_config.gui.FrozenLibConfigGui;
import net.frozenblock.lib.env.api.EnvType;
import net.frozenblock.lib.env.api.Environment;
import net.minecraft.client.gui.screens.Screen;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
		if (FrozenLibConstants.HAS_CLOTH_CONFIG) {
			return FrozenLibConfigGui::createScreen;
		}
		return (screen -> null);
	}
}
