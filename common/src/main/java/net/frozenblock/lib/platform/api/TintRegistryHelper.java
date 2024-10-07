package net.frozenblock.lib.platform.api;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.frozenblock.lib.env.api.EnvType;
import net.frozenblock.lib.env.api.Environment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

@Environment(EnvType.CLIENT)
public class TintRegistryHelper {

	@ExpectPlatform
	public static void registerDefaultFoliageColorForItem(ItemLike itemLike) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerAverageFoliageColorForBlock(Block block) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerBirchFoliageColorForItem(ItemLike itemLike) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerBirchFoliageColorForBlock(Block block) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerEvergreenFoliageColorForItem(ItemLike itemLike) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerEvergreenFoliageColorForBlock(Block block) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerMangroveFoliageColorForItem(ItemLike itemLike) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerMangroveFoliageColorForBlock(Block block) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerColorForItem(ItemLike itemLike, int color) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerColorForBlock(Block block, int color) {
		throw new AssertionError();
	}
}
