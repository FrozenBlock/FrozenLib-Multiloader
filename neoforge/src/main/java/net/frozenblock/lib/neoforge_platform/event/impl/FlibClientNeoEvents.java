package net.frozenblock.lib.neoforge_platform.event.impl;

import net.frozenblock.lib.event.event.client.ClientEntityEvents;
import net.frozenblock.lib.event.event.client.ClientPlayConnectionEvents;
import net.frozenblock.lib.event.event.client.ClientTickEvents;
import net.frozenblock.lib.networking.impl.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Here we invoke NEO FORGE EVENTS<br>
 * Look for an event class, {@code CTRL+Click} on the class and see where its invoked.<br>
 * If it says {@code ModLoader.postEvent(...)}, you should put your event in {@link FrozenLibModEvents}
 * */
@OnlyIn(Dist.CLIENT)
public class FlibClientNeoEvents {

	@SubscribeEvent
	public static void onEntityLoaded(@NotNull EntityJoinLevelEvent event) {
		if (event.getLevel() instanceof ClientLevel clientLevel) {
			ClientEntityEvents.ENTITY_LOAD.invoke(e -> e.onLoad(event.getEntity(), clientLevel));
		}
	}

	@SubscribeEvent
	public static void onEntityUnloaded(@NotNull EntityLeaveLevelEvent event) {
		if (event.getLevel() instanceof ClientLevel clientLevel) {
			ClientEntityEvents.ENTITY_UNLOAD.invoke(e -> e.onUnload(event.getEntity(), clientLevel));
		}
	}

	@SubscribeEvent
	public static void onClientTickStart(ClientTickEvent.Pre event) {
		ClientTickEvents.START_CLIENT_TICK.invoke(e -> e.onStartTick(Minecraft.getInstance()));
	}

	@SubscribeEvent
	public static void onClientTickEnd(ClientTickEvent.Pre event) {
		ClientTickEvents.END_CLIENT_TICK.invoke(e -> e.onEndTick(Minecraft.getInstance()));
	}

	@SubscribeEvent
	public static void onPlayerJoin(ClientPlayerNetworkEvent.LoggingIn event) {
		ClientPlayConnectionEvents.JOIN.invoke(e -> e.onPlayReady(event.getPlayer().connection, new PacketSender(event.getConnection()), Minecraft.getInstance()));
	}

	@SubscribeEvent
	public static void onPlayerLeave(ClientPlayerNetworkEvent.@NotNull LoggingOut event) {
		if (event.getPlayer() != null) {
			ClientPlayConnectionEvents.DISCONNECT.invoke(e -> e.onPlayDisconnect(event.getPlayer().connection, Minecraft.getInstance()));
		}
	}

	@SubscribeEvent
	public static void startClientLevelTick(LevelTickEvent.@NotNull Pre event) {
		if (event.getLevel() instanceof ClientLevel clientLevel) {
			ClientTickEvents.START_LEVEL_TICK.invoke(e -> e.onStartTick(clientLevel));
		}
	}

	@SubscribeEvent
	public static void endClientLevelTick(LevelTickEvent.@NotNull Post event) {
		if (event.getLevel() instanceof ClientLevel clientLevel) {
			ClientTickEvents.END_LEVEL_TICK.invoke(e -> e.onEndTick(clientLevel));
		}
	}

}
