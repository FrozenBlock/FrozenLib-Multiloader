package net.frozenblock.lib.platform.api.fabric;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public class TintRegistryHelperImpl {
	public static void registerDefaultFoliageColorForItem(ItemLike itemLike) {
		ColorProviderRegistry.ITEM.register(
			(stack, tintIndex) -> FoliageColor.getDefaultColor(),
			itemLike
		);
	}

	public static void registerAverageFoliageColorForBlock(Block block) {
		ColorProviderRegistry.BLOCK.register(
			(state, level, pos, tintIndex) -> {
				if (level != null && pos != null) {
					return BiomeColors.getAverageFoliageColor(level, pos);
				}
				return FoliageColor.getDefaultColor();
			},
			block
		);
	}

	public static void registerBirchFoliageColorForItem(ItemLike itemLike) {
		ColorProviderRegistry.ITEM.register(
			(stack, tintIndex) -> FoliageColor.getBirchColor(),
			itemLike
		);
	}

	public static void registerBirchFoliageColorForBlock(Block block) {
		ColorProviderRegistry.BLOCK.register(
			(state, level, pos, tintIndex) -> FoliageColor.getBirchColor(),
			block
		);
	}

	public static void registerEvergreenFoliageColorForItem(ItemLike itemLike) {
		ColorProviderRegistry.ITEM.register(
			(stack, tintIndex) -> FoliageColor.getEvergreenColor(),
			itemLike
		);
	}

	public static void registerEvergreenFoliageColorForBlock(Block block) {
		ColorProviderRegistry.BLOCK.register(
			(state, level, pos, tintIndex) -> FoliageColor.getEvergreenColor(),
			block
		);
	}

	public static void registerMangroveFoliageColorForItem(ItemLike itemLike) {
		ColorProviderRegistry.ITEM.register(
			(stack, tintIndex) -> FoliageColor.getMangroveColor(),
			itemLike
		);
	}

	public static void registerMangroveFoliageColorForBlock(Block block) {
		ColorProviderRegistry.BLOCK.register(
			(state, level, pos, tintIndex) -> FoliageColor.getMangroveColor(),
			block
		);
	}

	public static void registerColorForItem(ItemLike itemLike, int color) {
		ColorProviderRegistry.ITEM.register(
			(stack, tintIndex) -> color,
			itemLike
		);
	}

	public static void registerColorForBlock(Block block, int color) {
		ColorProviderRegistry.BLOCK.register(
			(state, level, pos, tintIndex) -> color,
			block
		);
	}
}
