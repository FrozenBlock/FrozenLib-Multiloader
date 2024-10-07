package net.frozenblock.lib.platform.api;

import net.frozenblock.lib.env.api.EnvType;
import net.frozenblock.lib.env.api.Environment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

@Environment(EnvType.CLIENT)
public interface ITintRegistryHelper {
	void registerDefaultFoliageColorForItem(ItemLike itemLike);

	void registerAverageFoliageColorForBlock(Block block);

	void registerBirchFoliageColorForItem(ItemLike itemLike);

	void registerBirchFoliageColorForBlock(Block block);

	void registerEvergreenFoliageColorForItem(ItemLike itemLike);

	void registerEvergreenFoliageColorForBlock(Block block);

	void registerMangroveFoliageColorForItem(ItemLike itemLike);

	void registerMangroveFoliageColorForBlock(Block block);

	void registerColorForItem(ItemLike itemLike, int color);

	void registerColorForBlock(Block block, int color);
}
