/*
 * Copyright 2024 The Quilt Project
 * Copyright 2024 FrozenBlock
 * Modified to work on Fabric
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.quiltmc.qsl.frozenblock.core.registry.api.sync;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.CommonEventEntrypoint;
import net.frozenblock.lib.platform.FrozenLibPlatformHelper;
import org.slf4j.Logger;

public class ModProtocol {
	public static boolean enabled = false;
	public static boolean disableQuery = false;
	public static String prioritizedId = "";
	public static ModProtocolDef prioritizedEntry;
	public static final Logger LOGGER = LogUtils.getLogger();

	// TODO: [Liuk] implement
	public static final FrozenEvent<LoadModProtocol> LOAD_MOD_PROTOCOL = FrozenEvent.createEvent(LoadModProtocol.class);

	private static final Map<String, ModProtocolDef> PROTOCOL_VERSIONS = new HashMap<>();
	public static final List<ModProtocolDef> REQUIRED = new ArrayList<>();
	public static final List<ModProtocolDef> ALL = new ArrayList<>();

	public static void loadVersions() {
		FrozenLibPlatformHelper.PROTOCOL.loadVersions();
	}



	public static IntList getVersion(String string) {
		var x = PROTOCOL_VERSIONS.get(string);
		return x == null ? IntList.of() : x.versions();
	}

	public static void add(ModProtocolDef def) {
		PROTOCOL_VERSIONS.put(def.id(), def);

		if (!def.optional()) {
			REQUIRED.add(def);
		}

		ALL.add(def);
		enabled = true;
	}

	@FunctionalInterface
	public interface LoadModProtocol extends CommonEventEntrypoint {
		void load();
	}
}
