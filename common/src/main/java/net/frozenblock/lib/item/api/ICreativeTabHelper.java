package net.frozenblock.lib.item.api;

import java.util.List;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public interface ICreativeTabHelper extends CreativeModeTab.Output {
	void addBefore(ItemLike comparedItem, List<ItemStack> list, CreativeModeTab.TabVisibility tabVisibility);

	void addAfter(ItemLike comparedItem, List<ItemStack> list, CreativeModeTab.TabVisibility tabVisibility);

}
