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

package net.frozenblock.lib.cape.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public record Cape(ResourceLocation registryId, Component capeName, ResourceLocation texture, Optional<List<UUID>> allowedPlayers) {

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Cape cape = (Cape) o;
		return Objects.equals(capeName, cape.capeName)
			&& Objects.equals(texture, cape.texture)
			&& Objects.equals(registryId, cape.registryId)
			&& Objects.equals(allowedPlayers, cape.allowedPlayers);
	}

	@Override
	public int hashCode() {
		return Objects.hash(registryId, capeName, texture, allowedPlayers);
	}
}
