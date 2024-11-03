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

package net.frozenblock.lib.config.api.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.ClientEventEntrypoint;
import net.frozenblock.lib.event.entrypoint.CommonEventEntrypoint;

@FunctionalInterface
public interface ConfigLoadEvent extends CommonEventEntrypoint {

	FrozenEvent<ConfigLoadEvent> EVENT = FrozenEvent.createEvent(ConfigLoadEvent.class);

	void onLoad(Config<?> config) throws Exception;
	@Environment(EnvType.CLIENT)
	interface Client extends ClientEventEntrypoint {

		FrozenEvent<Client> EVENT = FrozenEvent.createEvent(Client.class);

		void onLoad(Config<?> config) throws Exception;
	}
}
