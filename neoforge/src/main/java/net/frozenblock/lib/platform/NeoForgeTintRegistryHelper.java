package net.frozenblock.lib.platform;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import net.frozenblock.lib.platform.api.ITintRegistryHelper;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public class NeoForgeTintRegistryHelper implements ITintRegistryHelper {
	private static final ArrayList<Pair<Block, BlockColor>> BLOCK_COLORS = new ArrayList<>();
	private static final ArrayList<Pair<Item, ItemColor>> ITEM_COLORS = new ArrayList<>();

	public static void gatherBlocks(RegisterColorHandlersEvent.Block event) {
		BLOCK_COLORS.forEach(pair -> event.register(pair.getSecond(), pair.getFirst()));
	}

	public static void gatherItems(RegisterColorHandlersEvent.Item event) {
		ITEM_COLORS.forEach(pair -> event.register(pair.getSecond(), pair.getFirst()));
	}

	@Override
	public void registerDefaultFoliageColorForItem(ItemLike itemLike) {

	}

	@Override
	public void registerAverageFoliageColorForBlock(Block block) {

	}

	@Override
	public void registerBirchFoliageColorForItem(ItemLike itemLike) {

	}

	@Override
	public void registerBirchFoliageColorForBlock(Block block) {

	}

	@Override
	public void registerEvergreenFoliageColorForItem(ItemLike itemLike) {

	}

	@Override
	public void registerEvergreenFoliageColorForBlock(Block block) {

	}

	@Override
	public void registerMangroveFoliageColorForItem(ItemLike itemLike) {

	}

	@Override
	public void registerMangroveFoliageColorForBlock(Block block) {

	}

	@Override
	public void registerColorForItem(ItemLike itemLike, int color) {

	}

	@Override
	public void registerColorForBlock(Block block, int color) {

	}
}
