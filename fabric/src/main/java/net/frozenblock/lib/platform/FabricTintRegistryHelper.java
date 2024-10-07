package net.frozenblock.lib.platform;

import java.util.Objects;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.frozenblock.lib.platform.api.ITintRegistryHelper;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public class FabricTintRegistryHelper implements ITintRegistryHelper {
	@Override
	public void registerDefaultFoliageColorForItem(ItemLike itemLike) {
		ColorProviderRegistry.ITEM.register(
			(stack, tintIndex) -> FoliageColor.getDefaultColor(),
			itemLike
		);
	}

	@Override
	public void registerAverageFoliageColorForBlock(Block block) {
		ColorProviderRegistry.BLOCK.register(
			(state, level, pos, tintIndex) -> BiomeColors.getAverageFoliageColor(Objects.requireNonNull(level), Objects.requireNonNull(pos)),
			block
		);
	}

	@Override
	public void registerBirchFoliageColorForItem(ItemLike itemLike) {
		ColorProviderRegistry.ITEM.register(
			(stack, tintIndex) -> FoliageColor.getBirchColor(),
			itemLike
		);
	}

	@Override
	public void registerBirchFoliageColorForBlock(Block block) {
		ColorProviderRegistry.BLOCK.register(
			(state, level, pos, tintIndex) -> FoliageColor.getBirchColor(),
			block
		);
	}

	@Override
	public void registerEvergreenFoliageColorForItem(ItemLike itemLike) {
		ColorProviderRegistry.ITEM.register(
			(stack, tintIndex) -> FoliageColor.getEvergreenColor(),
			itemLike
		);
	}

	@Override
	public void registerEvergreenFoliageColorForBlock(Block block) {
		ColorProviderRegistry.BLOCK.register(
			(state, level, pos, tintIndex) -> FoliageColor.getEvergreenColor(),
			block
		);
	}

	@Override
	public void registerMangroveFoliageColorForItem(ItemLike itemLike) {
		ColorProviderRegistry.ITEM.register(
			(stack, tintIndex) -> FoliageColor.getMangroveColor(),
			itemLike
		);
	}

	@Override
	public void registerMangroveFoliageColorForBlock(Block block) {
		ColorProviderRegistry.BLOCK.register(
			(state, level, pos, tintIndex) -> FoliageColor.getMangroveColor(),
			block
		);
	}

	@Override
	public void registerColorForItem(ItemLike itemLike, int color) {
		ColorProviderRegistry.ITEM.register(
			(stack, tintIndex) -> color,
			itemLike
		);
	}

	@Override
	public void registerColorForBlock(Block block, int color) {
		ColorProviderRegistry.BLOCK.register(
			(state, level, pos, tintIndex) -> color,
			block
		);
	}
}
