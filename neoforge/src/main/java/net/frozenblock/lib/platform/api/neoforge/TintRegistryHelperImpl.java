package net.frozenblock.lib.platform.api.neoforge;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import java.util.Map;

public class TintRegistryHelperImpl {
	private static final Map<Block, BlockColor> BLOCK_COLORS = new Object2ObjectLinkedOpenHashMap<>();
	private static final Map<ItemLike, ItemColor> ITEM_COLORS = new Object2ObjectLinkedOpenHashMap<>();

	public static void gatherBlocks(RegisterColorHandlersEvent.Block event) {
		BLOCK_COLORS.forEach((block, color) -> event.register(color, block));
	}

	public static void gatherItems(RegisterColorHandlersEvent.Item event) {
		ITEM_COLORS.forEach((itemLike, color) -> event.register(color, itemLike));
	}

	public static void registerDefaultFoliageColorForItem(ItemLike itemLike) {
		ITEM_COLORS.put(itemLike, (arg, i) -> FoliageColor.getDefaultColor());
	}

	public static void registerAverageFoliageColorForBlock(Block block) {
		BLOCK_COLORS.put(block, (state, tintGetter, pos, tintIndex) -> {
			if (tintGetter != null && pos != null) {
				return BiomeColors.getAverageFoliageColor(tintGetter, pos);
			}
			return FoliageColor.getDefaultColor();
		});
	}

	public static void registerBirchFoliageColorForItem(ItemLike itemLike) {
		ITEM_COLORS.put(itemLike, (arg, i) -> FoliageColor.getBirchColor());
	}

	public static void registerBirchFoliageColorForBlock(Block block) {
		BLOCK_COLORS.put(block, (state, tintGetter, pos, tintIndex) -> FoliageColor.getBirchColor());
	}

	public static void registerEvergreenFoliageColorForItem(ItemLike itemLike) {
		ITEM_COLORS.put(itemLike, (arg, i) -> FoliageColor.getEvergreenColor());
	}


	public static void registerEvergreenFoliageColorForBlock(Block block) {
		BLOCK_COLORS.put(block, (state, tintGetter, pos, tintIndex) -> FoliageColor.getEvergreenColor());
	}

	public static void registerMangroveFoliageColorForItem(ItemLike itemLike) {
		ITEM_COLORS.put(itemLike, (arg, i) -> FoliageColor.getMangroveColor());
	}

	public static void registerMangroveFoliageColorForBlock(Block block) {
		BLOCK_COLORS.put(block, (state, tintGetter, pos, tintIndex) -> FoliageColor.getMangroveColor());
	}

	public static void registerColorForItem(ItemLike itemLike, int color) {
		ITEM_COLORS.put(itemLike, (arg, i) -> color);
	}

	public static void registerColorForBlock(Block block, int color) {
		BLOCK_COLORS.put(block, (state, tintGetter, pos, tintIndex) -> color);
	}
}
