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

package net.frozenblock.lib.gametest.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3dc;
import org.joml.Vector3fc;
import org.joml.Vector3ic;

/**
 *
 * @param type The {@link PositionType} linked to {@link #pos}.
 * @param pos The position
 * @param opposite The position in the opposite position type
 * @param <T> The type of position being tracked
 * @since 1.3.8
 */
public record TrackedPosition<T>(PositionType type, T pos, T opposite) {

	/**
	 * Creates a new {@link TrackedPosition} with the given position as its relative value.
	 */
	@Contract("_, _ -> new")
	public static @NotNull TrackedPosition<BlockPos> createRelative(@NotNull GameTestHelper helper, BlockPos pos) {
		return new TrackedPosition<>(PositionType.RELATIVE, pos, helper.absolutePos(pos));
	}

	/**
	 * Creates a new {@link TrackedPosition} with the given position as its relative value.
	 * <p>
	 * Asserts the given block is at the position.
	 */
	public static TrackedPosition<BlockPos> createRelative(GameTestHelper helper, Block block, BlockPos pos) {
		return createRelative(helper, pos).assertBlockPresent(helper, block);
	}

	/**
	 * Creates a new {@link TrackedPosition} with the given position as its absolute value.
	 */
	@Contract("_, _ -> new")
	public static @NotNull TrackedPosition<BlockPos> createAbsolute(@NotNull GameTestHelper helper, BlockPos pos) {
		return new TrackedPosition<>(PositionType.ABSOLUTE, pos, helper.relativePos(pos));
	}

	/**
	 * Creates a new {@link TrackedPosition} with the given position as its absolute value.
	 * <p>
	 * Asserts the given block is at the relative position.
	 */
	public static TrackedPosition<BlockPos> createAbsolute(GameTestHelper helper, Block block, BlockPos pos) {
		return createAbsolute(helper, pos).assertBlockPresent(helper, block);
	}

	/**
	 * Creates a new {@link TrackedPosition} with the given position as its relative value.
	 */
	@Contract("_, _ -> new")
	public static @NotNull TrackedPosition<Vec3> createRelative(@NotNull GameTestHelper helper, Vec3 pos) {
		return new TrackedPosition<>(PositionType.RELATIVE, pos, helper.absoluteVec(pos));
	}

	/**
	 * Creates a new {@link TrackedPosition} with the given position as its relative value.
	 * <p>
	 * Asserts the given block is at the position.
	 */
	public static TrackedPosition<Vec3> createRelative(GameTestHelper helper, Block block, Vec3 pos) {
		return createRelative(helper, pos).assertBlockPresent(helper, block);
	}

	/**
	 * Creates a new {@link TrackedPosition} with the given position as its absolute value.
	 */
	@Contract("_, _ -> new")
	public static @NotNull TrackedPosition<Vec3> createAbsolute(@NotNull GameTestHelper helper, Vec3 pos) {
		return new TrackedPosition<>(PositionType.ABSOLUTE, pos, helper.relativeVec(pos));
	}

	/**
	 * Creates a new {@link TrackedPosition} with the given position as its absolute value.
	 * <p>
	 * Asserts the given block is at the relative position.
	 */
	public static TrackedPosition<Vec3> createAbsolute(GameTestHelper helper, Block block, Vec3 pos) {
		return createAbsolute(helper, pos).assertBlockPresent(helper, block);
	}

	public T absolute() {
		return switch (this.type()) {
			case RELATIVE -> this.opposite();
			case ABSOLUTE -> this.pos();
		};
	}

	public T relative() {
		return switch (this.type()) {
			case RELATIVE -> this.pos();
			case ABSOLUTE -> this.opposite();
		};
	}

	public TrackedPosition<T> assertBlockPresent(GameTestHelper helper, Block block) throws IllegalStateException {
		T relative = this.relative();
		switch (relative) {
			case Position position ->  helper.assertBlockPresent(block, BlockPos.containing(position));
			case BlockPos blockPos -> helper.assertBlockPresent(block, blockPos);
			case Vec3i vec3i -> helper.assertBlockPresent(block, new BlockPos(vec3i));
			case Vector3ic vec3i -> helper.assertBlockPresent(block, new BlockPos(vec3i.x(), vec3i.y(), vec3i.z()));
			case Vector3fc vec3f -> helper.assertBlockPresent(block, BlockPos.containing(vec3f.x(), vec3f.y(), vec3f.z()));
			case Vector3dc vec3d -> helper.assertBlockPresent(block, BlockPos.containing(vec3d.x(), vec3d.y(), vec3d.z()));
			case null, default -> throw new IllegalStateException("Invalid position type: " + relative.getClass().getName());
		}
		return this;
	}
}
