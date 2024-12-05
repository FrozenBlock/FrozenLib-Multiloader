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

package net.frozenblock.lib.debug.item;

import net.frozenblock.lib.FrozenLibConstants;
import net.frozenblock.lib.registry.api.FrozenLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class FrozenLibDevItems {
	private static final Item CAMERA = new PanoramaCamera(new Item.Properties().stacksTo(1));
	private static final Item LOOT_TABLE_WHACKER = new LootTableWhacker(new Item.Properties().stacksTo(1));

	public static void register() {
		FrozenLibRegistry.register(BuiltInRegistries.ITEM, FrozenLibConstants.id("camera"), CAMERA);
		FrozenLibRegistry.register(BuiltInRegistries.ITEM, FrozenLibConstants.string("loot_table_whacker"), LOOT_TABLE_WHACKER);
	}
}
