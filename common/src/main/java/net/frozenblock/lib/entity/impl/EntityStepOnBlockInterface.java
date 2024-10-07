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

package net.frozenblock.lib.entity.impl;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Used to call a method each time an entity steps on a block.
 */
public interface EntityStepOnBlockInterface {

	/**
	 * Custom behavior to run when the entity steps on a block.
	 *
	 * @param level      The current {@link Level}.
	 * @param blockPos   The position of the block being stepped on.
	 * @param blockState The {@link BlockState} of the block being stepped on.
	 */
	void frozenLib$onSteppedOnBlock(Level level, BlockPos blockPos, BlockState blockState);

}
