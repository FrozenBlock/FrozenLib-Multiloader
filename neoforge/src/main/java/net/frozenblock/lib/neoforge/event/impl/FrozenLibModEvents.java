package net.frozenblock.lib.neoforge.event.impl;

import net.frozenblock.lib.debug.item.FrozenLibDevItems;
import net.frozenblock.lib.item.api.CreativeTabAPI;
import net.frozenblock.lib.neoforge.event.api.FrozenLibNeoForgePacketRegisterEvent;
import net.frozenblock.lib.networking.client.FrozenLibClientNetworking;
import net.frozenblock.lib.platform.NeoForgeCreativeTabHelper;
import net.frozenblock.lib.platform.api.PlatformHelper;
import net.frozenblock.lib.platform.api.neoforge.TintRegistryHelperImpl;
import net.frozenblock.lib.registry.api.FrozenLibRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Here we invoke MOD EVENTS<br>
 * Look for an event class, {@code CTRL+Click} on the class and see where its invoked.<br>
 * If it says {@code NeoForge.EVENT_BUS.post(...)}, you should put your event in {@link FlibClientNeoEvents}
 * */
public class FrozenLibModEvents {

	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
		TintRegistryHelperImpl.gatherItems(event);
	}

	@SubscribeEvent
	public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
		TintRegistryHelperImpl.gatherBlocks(event);
	}

	@SubscribeEvent
	public static void creativeModeTab(@NotNull BuildCreativeModeTabContentsEvent event) {
		CreativeTabAPI.modifyEntriesEvent(event.getTabKey()).invoke(e -> e.modifyEntries(new NeoForgeCreativeTabHelper(event)));
	}

	@SubscribeEvent
	public static void addNewRegistries(@NotNull NewRegistryEvent event) {
		FrozenLibRegistries.getRegistries().forEach(event::register);
	}

	@SubscribeEvent
	public static void registerClientPacketReceivers(FrozenLibNeoForgePacketRegisterEvent event) {
		if (PlatformHelper.envType() != Dist.CLIENT) return;
		FrozenLibClientNetworking.registerClientReceivers();
	}

	@SubscribeEvent
	public static void registerEvent(RegisterEvent event) {
		if (event.getRegistry() == BuiltInRegistries.ITEM) {
			if (PlatformHelper.isDevelopmentEnvironment()) {
				FrozenLibDevItems.register();
			}
		}
	}
}
