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

package net.frozenblock.lib.modmenu.api;

import java.util.ArrayList;
import net.frozenblock.lib.modmenu.impl.FrozenLibModMenuBadge;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * An entrypoint that allows for the creation of custom ModMenu badges.
 *
 * <p> The entrypoint's identifier is `frozenlib:modmenu_badges.`
 */
public abstract class ModMenuBadgeCreationEntrypoint {

	@Contract(value = "_, _, _, _ -> new", pure = true)
	public static @NotNull FrozenLibModMenuBadge createBadge(String translationKey, int outlineColor, int fillColor, String key) {
		return new FrozenLibModMenuBadge(translationKey, outlineColor, fillColor, key);
	}

	/**
	 * @return A list of {@link FrozenLibModMenuBadge}s. Use {@link ModMenuBadgeCreationEntrypoint#createBadge(String, int, int, String)}.
	 */
	public abstract ArrayList<FrozenLibModMenuBadge> newBadges();

}

