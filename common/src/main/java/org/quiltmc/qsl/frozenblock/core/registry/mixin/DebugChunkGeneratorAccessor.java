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

package org.quiltmc.qsl.frozenblock.core.registry.mixin;

import java.util.List;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.DebugLevelSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DebugLevelSource.class)
public interface DebugChunkGeneratorAccessor {

	@Mutable
	@Accessor("ALL_BLOCKS")
	static void setAllBlocks(List<BlockState> blocks) {
		throw new IllegalStateException("Mixin injection failed.");
	}

	@Mutable
	@Accessor("GRID_WIDTH")
	static void setXSideLength(int length) {
		throw new IllegalStateException("Mixin injection failed.");
	}

	@Mutable
	@Accessor("GRID_HEIGHT")
	static void setZSideLength(int length) {
		throw new IllegalStateException("Mixin injection failed.");
	}
}
