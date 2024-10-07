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

package net.frozenblock.lib.worldgen.surface.api;

import java.util.List;
import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.CommonEventEntrypoint;
import net.minecraft.world.level.levelgen.SurfaceRules;

/**
 * Events that allows adding surface rules to dimensions.
 * <p>
 * Defined with the {@code frozenlib:events} key in {@code fabric.mod.json}.
 * <p>
 * Compatible with TerraBlender.
 */
public class SurfaceRuleEvents {

	/**
	 * Lets you modify the Surface Rules of Overworld-based world presets.
	 */
	public static final FrozenEvent<OverworldSurfaceRuleCallback> MODIFY_OVERWORLD = FrozenEvent.createEvent(OverworldSurfaceRuleCallback.class);

	/**
	 * Lets you modify the Surface Rules of Overworld-based world presets without checking the preliminary surface.
	 */
	public static final FrozenEvent<OverworldSurfaceRuleNoPrelimSurfaceCallback> MODIFY_OVERWORLD_NO_PRELIMINARY_SURFACE = FrozenEvent.createEvent(OverworldSurfaceRuleNoPrelimSurfaceCallback.class);

	/**
	 * Lets you modify the Surface Rules of Nether-based world presets.
	 */
	public static final FrozenEvent<NetherSurfaceRuleCallback> MODIFY_NETHER = FrozenEvent.createEvent(NetherSurfaceRuleCallback.class);

	/**
	 * Lets you modify the Surface Rules of End-based world presets.
	 */
	public static final FrozenEvent<EndSurfaceRuleCallback> MODIFY_END = FrozenEvent.createEvent(EndSurfaceRuleCallback.class);

	/**
	 * Lets you modify the Surface Rules of custom world presets.
	 */
	public static final FrozenEvent<GenericSurfaceRuleCallback> MODIFY_GENERIC = FrozenEvent.createEvent(GenericSurfaceRuleCallback.class);

	public interface OverworldSurfaceRuleCallback extends CommonEventEntrypoint {
		void addOverworldSurfaceRules(List<SurfaceRules.RuleSource> context);
	}


	public interface OverworldSurfaceRuleNoPrelimSurfaceCallback extends CommonEventEntrypoint {
		void addOverworldNoPrelimSurfaceRules(List<SurfaceRules.RuleSource> context);
	}


	public interface NetherSurfaceRuleCallback extends CommonEventEntrypoint {
		void addNetherSurfaceRules(List<SurfaceRules.RuleSource> context);
	}


	public interface EndSurfaceRuleCallback extends CommonEventEntrypoint {
		void addEndSurfaceRules(List<SurfaceRules.RuleSource> context);
	}

	public interface GenericSurfaceRuleCallback extends CommonEventEntrypoint {
		void addGenericSurfaceRules(List<FrozenDimensionBoundRuleSource> context);
	}
}
