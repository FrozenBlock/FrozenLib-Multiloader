package net.frozenblock.lib.platform;

import java.util.List;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.frozenblock.lib.platform.api.CreativeTabHelper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

// TODO: [Liuk] make this work in a static context
public class FabricCreativeModeTabHelper implements CreativeTabHelper {

	private final FabricItemGroupEntries event;
	public FabricCreativeModeTabHelper(FabricItemGroupEntries event) {
		this.event = event;
	}

	@Override
	public void addBefore(ItemLike comparedItem, List<ItemStack> list, CreativeModeTab.TabVisibility tabVisibility) {
		event.addBefore(comparedItem, list, tabVisibility);
	}

	@Override
	public void addAfter(ItemLike comparedItem, List<ItemStack> list, CreativeModeTab.TabVisibility tabVisibility) {
		event.addAfter(comparedItem, list, tabVisibility);
	}

	@Override
	public void accept(ItemStack stack, CreativeModeTab.TabVisibility tabVisibility) {
		event.accept(stack, tabVisibility);
	}
}
