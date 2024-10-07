package net.frozenblock.lib.platform.api;

import java.util.List;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public interface CreativeTabHelper extends CreativeModeTab.Output {
	void addBefore(ItemLike comparedItem, List<ItemStack> list, CreativeModeTab.TabVisibility tabVisibility);

	void addAfter(ItemLike comparedItem, List<ItemStack> list, CreativeModeTab.TabVisibility tabVisibility);

}
