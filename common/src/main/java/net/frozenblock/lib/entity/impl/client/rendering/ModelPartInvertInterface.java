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

package net.frozenblock.lib.entity.impl.client.rendering;

/**
 * Used to make a {@link net.minecraft.client.model.geom.ModelPart} render inverted.
 */
public interface ModelPartInvertInterface {

	/**
	 * @param inverted Whether the {@link net.minecraft.client.model.geom.ModelPart} should be inverted.
	 */
	void frozenLib$setInverted(boolean inverted);

}
