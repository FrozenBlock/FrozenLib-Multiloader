package net.frozenblock.lib.platform;


import java.util.List;
import net.frozenblock.lib.item.api.ICreativeTabHelper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.jetbrains.annotations.NotNull;

public class NeoForgeCreativeTabHelper implements ICreativeTabHelper {

	private final BuildCreativeModeTabContentsEvent event;
	public NeoForgeCreativeTabHelper(BuildCreativeModeTabContentsEvent event) {
		this.event = event;
	}

	@Override
	public void addBefore(ItemLike comparedItem, List<ItemStack> list, CreativeModeTab.TabVisibility tabVisibility) {
		for(var item : list) {
			//TODO: [Liuk] check if this works
			event.insertBefore(comparedItem.asItem().getDefaultInstance(), item, tabVisibility);
		}
	}

	@Override
	public void addAfter(ItemLike comparedItem, List<ItemStack> list, CreativeModeTab.TabVisibility tabVisibility) {
		for(var item : list) {
			event.insertAfter(comparedItem.asItem().getDefaultInstance(), item, tabVisibility);
		}
	}

	@Override
	public void accept(@NotNull ItemStack itemStack, CreativeModeTab.@NotNull TabVisibility tabVisibility) {
		event.accept(itemStack, tabVisibility);
	}
}
