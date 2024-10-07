package net.frozenblock.lib.platform.api.neoforge;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import java.util.ArrayList;

public class TintRegistryHelperImpl {
	// TODO: [Liuk] implement these methods
	private static final ArrayList<Pair<Block, BlockColor>> BLOCK_COLORS = new ArrayList<>();
	private static final ArrayList<Pair<Item, ItemColor>> ITEM_COLORS = new ArrayList<>();

	public static void gatherBlocks(RegisterColorHandlersEvent.Block event) {
		BLOCK_COLORS.forEach(pair -> event.register(pair.getSecond(), pair.getFirst()));
	}

	public static void gatherItems(RegisterColorHandlersEvent.Item event) {
		ITEM_COLORS.forEach(pair -> event.register(pair.getSecond(), pair.getFirst()));
	}

	public static void registerDefaultFoliageColorForItem(ItemLike itemLike) {

	}

	public static void registerAverageFoliageColorForBlock(Block block) {

	}

	public static void registerBirchFoliageColorForItem(ItemLike itemLike) {

	}

	public static void registerBirchFoliageColorForBlock(Block block) {

	}

	public static void registerEvergreenFoliageColorForItem(ItemLike itemLike) {

	}


	public static void registerEvergreenFoliageColorForBlock(Block block) {

	}

	public static void registerMangroveFoliageColorForItem(ItemLike itemLike) {

	}

	public static void registerMangroveFoliageColorForBlock(Block block) {

	}

	public static void registerColorForItem(ItemLike itemLike, int color) {

	}

	public static void registerColorForBlock(Block block, int color) {

	}
}
