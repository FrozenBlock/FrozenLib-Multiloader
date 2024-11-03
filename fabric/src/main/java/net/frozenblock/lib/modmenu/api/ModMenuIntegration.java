package net.frozenblock.lib.modmenu.api;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.FrozenLibConstants;
import net.frozenblock.lib.config.frozenlib_config.gui.FrozenLibConfigGui;
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
