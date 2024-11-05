package net.frozenblock.lib.fabric.mixin;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.impl.itemgroup.ItemGroupEventsImpl;
import net.frozenblock.lib.item.api.CreativeTabAPI;
import net.frozenblock.lib.platform.FabricCreativeModeTabHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemGroupEventsImpl.class)
public class ItemGroupEventsImplMixin {

	@Inject(at = @At("RETURN"), method = "getModifyEntriesEvent")
	private static void frozenLib$invokeCustomEvent(ResourceKey<CreativeModeTab> registryKey, CallbackInfoReturnable<Event<ItemGroupEvents.ModifyEntries>> info) {
		var returnable = info.getReturnValue();
		returnable.register(entries -> CreativeTabAPI.modifyEntriesEvent(registryKey).invoke(e->e.modifyEntries(new FabricCreativeModeTabHelper(entries))));
	}
}
