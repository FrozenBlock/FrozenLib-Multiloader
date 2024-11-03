package net.frozenblock.lib.platform;

import java.util.List;
import net.frozenblock.lib.platform.api.CreativeTabHelper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.resource.ResourcePackLoader;
import org.jetbrains.annotations.NotNull;

// TODO: [Liuk] make this work in a static context
public class NeoForgeCreativeTabHelper implements CreativeTabHelper {
	private final BuildCreativeModeTabContentsEvent event;

	public NeoForgeCreativeTabHelper(BuildCreativeModeTabContentsEvent event) {
		this.event = event;
	}

	@Override
	public void addBefore(ItemLike comparedItem, @NotNull List<ItemStack> list, CreativeModeTab.TabVisibility tabVisibility) {
		for (var item : list) {
			//TODO: [Liuk] check if this works
			event.insertBefore(comparedItem.asItem().getDefaultInstance(), item, tabVisibility);
		}
	}

	@Override
	public void addAfter(ItemLike comparedItem, @NotNull List<ItemStack> list, CreativeModeTab.TabVisibility tabVisibility) {
		for (var item : list) {
			event.insertAfter(comparedItem.asItem().getDefaultInstance(), item, tabVisibility);
		}
	}

	@Override
	public void accept(@NotNull ItemStack itemStack, CreativeModeTab.@NotNull TabVisibility tabVisibility) {
		event.accept(itemStack, tabVisibility);
	}
}
