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

package net.frozenblock.lib.debug.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.List;
import net.frozenblock.lib.debug.client.impl.DebugRenderManager;
import net.frozenblock.lib.environment.api.EnvType;
import net.frozenblock.lib.environment.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.network.protocol.common.custom.GoalDebugPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class ImprovedGoalSelectorDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
	public final Int2ObjectMap<EntityGoalInfo> goalSelectors = new Int2ObjectLinkedOpenHashMap<>();
	private final Minecraft client;

	public ImprovedGoalSelectorDebugRenderer(Minecraft client) {
		this.client = client;
	}

	@Override
	public void clear() {
		this.goalSelectors.clear();
	}

	public void addGoalSelector(
		@NotNull Entity entity,
		List<GoalDebugPayload.DebugGoal> selectors
	) {
		this.goalSelectors.put(
			entity.getId(),
			new EntityGoalInfo(entity, selectors)
		);
	}

	public void removeGoalSelector(int index) {
		this.goalSelectors.remove(index);
	}

	@Override
	public void render(@NotNull PoseStack matrices, @NotNull MultiBufferSource vertexConsumers, double cameraX, double cameraY, double cameraZ) {
		Vec3 cameraPos = new Vec3(cameraX, cameraY, cameraZ);

		for (EntityGoalInfo entityGoalInfo : this.goalSelectors.values()) {
			Vec3 entityPos = entityGoalInfo.entity.getPosition(DebugRenderManager.PARTIAL_TICK).add(0D, entityGoalInfo.entity.getEyeHeight() + 0.5D, 0D);
			if (cameraPos.closerThan(entityPos, 160D, 256D)) {
				for (int i = 0; i < entityGoalInfo.goals.size(); i++) {
					GoalDebugPayload.DebugGoal debugGoal = entityGoalInfo.goals.get(i);
					double x = entityPos.x;
					double y = entityPos.y + (double) i * 0.25D;
					double z = entityPos.z;
					int textColor = debugGoal.isRunning() ? -16711936 : -3355444;
					DebugRenderer.renderFloatingText(matrices, vertexConsumers, debugGoal.name(), x, y, z, textColor);
				}
			}
		}
	}

	@Environment(EnvType.CLIENT)
	record EntityGoalInfo(Entity entity, List<GoalDebugPayload.DebugGoal> goals) {
	}
}
