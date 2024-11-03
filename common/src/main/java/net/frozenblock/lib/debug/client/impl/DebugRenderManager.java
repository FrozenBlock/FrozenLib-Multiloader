/*
 * Copyright (C) 2024 FrozenBlock
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.lib.debug.client.impl;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import net.frozenblock.lib.FrozenLibConstants;
import net.frozenblock.lib.config.frozenlib_config.FrozenLibConfig;
import net.frozenblock.lib.debug.client.api.DebugRendererEvents;
import net.frozenblock.lib.debug.client.renderer.ImprovedGameEventListenerRenderer;
import net.frozenblock.lib.debug.client.renderer.ImprovedGoalSelectorDebugRenderer;
import net.frozenblock.lib.debug.client.renderer.WindDebugRenderer;
import net.frozenblock.lib.debug.client.renderer.WindDisturbanceDebugRenderer;
import net.frozenblock.lib.debug.networking.GoalDebugRemovePayload;
import net.frozenblock.lib.debug.networking.ImprovedGameEventDebugPayload;
import net.frozenblock.lib.debug.networking.ImprovedGameEventListenerDebugPayload;
import net.frozenblock.lib.debug.networking.ImprovedGoalDebugPayload;
import net.frozenblock.lib.environment.api.EnvType;
import net.frozenblock.lib.environment.api.Environment;
import net.frozenblock.lib.event.event.client.ClientTickEvents;
import net.frozenblock.lib.platform.impl.ClientPayloadContext;
import net.frozenblock.lib.wind.api.ClientWindManager;
import net.frozenblock.lib.wind.impl.networking.WindAccessPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public class DebugRenderManager {
	private static final List<Runnable> ON_CLEAR_RUNNABLES = new ArrayList<>();
	public static Map<DebugRendererHolder, ResourceLocation> DEBUG_RENDERER_HOLDERS = new Object2ObjectLinkedOpenHashMap<>();
	public static float PARTIAL_TICK;

	public static void registerRenderer(ResourceLocation location, DebugRendererHolder.RenderInterface renderInterface) {
		if (location == null) throw new IllegalArgumentException("ResourceLocation cannot be null!");
		DEBUG_RENDERER_HOLDERS.put(new DebugRendererHolder(renderInterface), location);
		Stream<Map.Entry<DebugRendererHolder, ResourceLocation>> entries = DEBUG_RENDERER_HOLDERS.entrySet().stream()
			.sorted(Comparator.comparing(value -> value.getValue().getPath()));

		Map<DebugRendererHolder, ResourceLocation> newRenderHolders = new Object2ObjectLinkedOpenHashMap<>();
		entries.forEach(entry -> newRenderHolders.put(entry.getKey(), entry.getValue()));

		DEBUG_RENDERER_HOLDERS = newRenderHolders;
	}

	private static Optional<ImprovedGoalSelectorDebugRenderer> goalRenderer = Optional.empty();
	public static void onReceiveGoalDebugPacket(@NotNull ImprovedGoalDebugPayload packet, @NotNull ClientPayloadContext ctx) {
		Entity entity = ctx.client().level.getEntity(packet.entityId());
		if (entity != null) {
			goalRenderer.ifPresent(renderer -> renderer.addGoalSelector(entity, packet.goals()));
		}
	}
	public static void onReceiveGoalDebugRemovePacket(@NotNull GoalDebugRemovePayload packet, @NotNull ClientPayloadContext ctx) {
		goalRenderer.ifPresent(renderer -> renderer.removeGoalSelector(packet.entityId()));
	}

	private static Optional<ImprovedGameEventListenerRenderer> gameEventRenderer = Optional.empty();
	public static void onReceiveGameEventListenerDebugPacket(@NotNull ImprovedGameEventListenerDebugPayload packet, @NotNull ClientPayloadContext ctx) {
		gameEventRenderer.ifPresent(renderer -> renderer.trackListener(
			packet.listenerPos(),
			packet.listenerRange()
		));
	}
	public static void onReceiveGameEventDebugPacket(@NotNull ImprovedGameEventDebugPayload packet, @NotNull ClientPayloadContext ctx) {
		gameEventRenderer.ifPresent(renderer -> renderer.trackGameEvent(
			packet.gameEventType(),
			packet.pos()
		));
	}

	public static void onReceiveWindAccessPacket(@NotNull WindAccessPacket packet, @NotNull ClientPayloadContext ctx) {
		ClientWindManager.addAccessedPosition(packet.accessPos());
	}

	public static void init() {
		DebugRendererEvents.DEBUG_RENDERERS_CREATED.register(client -> {
			ImprovedGoalSelectorDebugRenderer improvedGoalSelectorRenderer = new ImprovedGoalSelectorDebugRenderer(client);
			goalRenderer = Optional.of(improvedGoalSelectorRenderer);
			addClearRunnable(improvedGoalSelectorRenderer::clear);
			registerRenderer(FrozenLibConstants.id("goal"), improvedGoalSelectorRenderer::render);
		});

		DebugRendererEvents.DEBUG_RENDERERS_CREATED.register(client -> {
			ImprovedGameEventListenerRenderer improvedGameEventRenderer = new ImprovedGameEventListenerRenderer(client);
			gameEventRenderer = Optional.of(improvedGameEventRenderer);
			ClientTickEvents.START_LEVEL_TICK.register(clientLevel -> {
				if (FrozenLibConfig.IS_DEBUG) {
					improvedGameEventRenderer.tick();
				}
			});
			registerRenderer(FrozenLibConstants.id("game_event"), improvedGameEventRenderer::render);
		});

		DebugRendererEvents.DEBUG_RENDERERS_CREATED.register(client -> {
			WindDebugRenderer windDebugRenderer = new WindDebugRenderer(client);
			ClientTickEvents.START_LEVEL_TICK.register(clientLevel -> {
				if (FrozenLibConfig.IS_DEBUG) {
					windDebugRenderer.tick();
					ClientWindManager.clearAccessedPositions();
				}
			});
			addClearRunnable(windDebugRenderer::clear);
			registerRenderer(FrozenLibConstants.id("wind"), windDebugRenderer::render);
		});

		DebugRendererEvents.DEBUG_RENDERERS_CREATED.register(client -> {
			WindDisturbanceDebugRenderer windDisturbanceDebugRenderer = new WindDisturbanceDebugRenderer(client);
			ClientTickEvents.START_LEVEL_TICK.register(clientLevel -> {
				if (FrozenLibConfig.IS_DEBUG) {
					windDisturbanceDebugRenderer.tick();
				}
			});
			addClearRunnable(windDisturbanceDebugRenderer::clear);
			registerRenderer(FrozenLibConstants.id("wind_disturbance"), windDisturbanceDebugRenderer::render);
		});
	}

	public static void clearAdditionalRenderers() {
		ON_CLEAR_RUNNABLES.forEach(Runnable::run);
	}

	public static void addClearRunnable(Runnable runnable) {
		ON_CLEAR_RUNNABLES.add(runnable);
	}

	public static void updatePartialTick() {
		PARTIAL_TICK = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
	}
}
