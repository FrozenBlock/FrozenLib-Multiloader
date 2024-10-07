package net.frozenblock.lib;

import net.frozenblock.lib.worldgen.surface.impl.terrablender.FrozenTerraBlenderCompat;
import terrablender.api.TerraBlenderApi;

public class FabricTerraBlenderCompat implements TerraBlenderApi {
	@Override
	public void onTerraBlenderInitialized() {
		FrozenTerraBlenderCompat.onTerraBlenderInitialized();
	}
}
