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

package net.frozenblock.lib.item.api.bonemeal;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

// TODO: JavaDocs

public class BonemealApi {
	private static final Map<Block, BonemealBehavior> BONEMEAL_BEHAVIORS = new Object2ObjectOpenHashMap<>();

	public static void register(Block block, BonemealBehavior bonemealBehavior) {
		BONEMEAL_BEHAVIORS.put(block, bonemealBehavior);
	}

	@Nullable
	public static BonemealBehavior get(Block block) {
		return BONEMEAL_BEHAVIORS.getOrDefault(block, null);
	}

	public interface BonemealBehavior {
		boolean meetsRequirements(LevelReader level, BlockPos pos, BlockState state);

		default boolean isBonemealSuccess(LevelReader level, RandomSource random, BlockPos pos, BlockState state) {
			return true;
		}

		void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state);
	}

}
