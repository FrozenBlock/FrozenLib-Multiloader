package net.frozenblock.lib.neoforge.event.impl;

import net.frozenblock.lib.event.event.RegisterCommandEvents;
import net.frozenblock.lib.event.event.ServerLifecycleEvents;
import net.frozenblock.lib.event.event.ServerPlayConnectionEvents;
import net.frozenblock.lib.event.event.ServerTickEvents;
import net.frozenblock.lib.item.api.bonemeal.BonemealApi;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.BonemealEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jetbrains.annotations.NotNull;

public class FlibCommonNeoEvents {
	@SubscribeEvent
	public static void onBonemeal(@NotNull BonemealEvent event) {
		BlockState blockState = event.getState();
		BlockPos pos = event.getPos();
		Level world = event.getLevel();
		BonemealApi.BonemealBehavior bonemealBehavior = BonemealApi.get(blockState.getBlock());
		if (bonemealBehavior != null && bonemealBehavior.meetsRequirements(world, pos, blockState)) {
			if (world instanceof ServerLevel serverLevel) {
				if (bonemealBehavior.isBonemealSuccess(world, world.random, pos, blockState)) {
					bonemealBehavior.performBonemeal(serverLevel, world.random, pos, blockState);
				}
				event.getStack().shrink(1);
			}
			event.setSuccessful(true);
		}
	}

	@SubscribeEvent
	public static void registerCommandsEvent(@NotNull RegisterCommandsEvent event) {
		final var dispatcher = event.getDispatcher();
		final var context = event.getBuildContext();
		final var selection = event.getCommandSelection();
		//We redirect the event from neo to common
		RegisterCommandEvents.REGISTER.invoke(flibEvent -> flibEvent.register(dispatcher, context, selection));
	}

	@SubscribeEvent
	public static void serverStarting(ServerStartingEvent event) {
		ServerLifecycleEvents.SERVER_STARTING.invoke(e -> e.onServerStarting(event.getServer()));
	}

	@SubscribeEvent
	public static void serverStarted(ServerStartedEvent event) {
		ServerLifecycleEvents.SERVER_STARTED.invoke(e -> e.onServerStarted(event.getServer()));
	}

	@SubscribeEvent
	public static void serverStopping(ServerStoppingEvent event) {
		ServerLifecycleEvents.SERVER_STOPPING.invoke(e -> e.onServerStopping(event.getServer()));
	}

	@SubscribeEvent
	public static void serverStopped(ServerStoppedEvent event) {
		ServerLifecycleEvents.SERVER_STOPPED.invoke(e -> e.onServerStopped(event.getServer()));
	}

	@SubscribeEvent
	public static void serverStartTick(ServerTickEvent.Pre event) {
		ServerTickEvents.START_SERVER_TICK.invoke(e -> e.onStartTick(event.getServer()));
	}

	@SubscribeEvent
	public static void serverEndTick(ServerTickEvent.Pre event) {
		ServerTickEvents.END_SERVER_TICK.invoke(e -> e.onEndTick(event.getServer()));
	}

	@SubscribeEvent
	public static void levelStartTick(LevelTickEvent.@NotNull Pre event) {
		if (event.getLevel() instanceof ServerLevel serverLevel) {
			ServerTickEvents.START_LEVEL_TICK.invoke(e -> e.onStartTick(serverLevel));
		}
	}

	@SubscribeEvent
	public static void levelEndTick(LevelTickEvent.@NotNull Post event) {
		if (event.getLevel() instanceof ServerLevel serverLevel) {
			ServerTickEvents.END_LEVEL_TICK.invoke(e -> e.onEndTick(serverLevel));
		}
	}

	@SubscribeEvent
	public static void joinServer(PlayerEvent.PlayerLoggedInEvent event) {
		ServerPlayConnectionEvents.JOIN.invoke(e -> e.onPlayReady(event.getEntity()));
	}

	@SubscribeEvent
	public static void leaveServer(PlayerEvent.PlayerLoggedOutEvent event) {
		ServerPlayConnectionEvents.DISCONNECT.invoke(e -> e.onPlayDisconnect(event.getEntity()));
	}
}
