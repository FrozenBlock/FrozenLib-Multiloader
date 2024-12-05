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

package org.quiltmc.qsl.frozenblock.fabric.core.registry.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.protocol.status.ServerStatus;
import org.quiltmc.qsl.frozenblock.fabric.core.registry.api.sync.ModProtocol;
import org.quiltmc.qsl.frozenblock.fabric.core.registry.impl.sync.mod_protocol.ModProtocolContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerStatus.Version.class)
public class ServerStatusVersionMixin implements ModProtocolContainer {
	@Mutable
	@Shadow
	@Final
	public static Codec<ServerStatus.Version> CODEC;

	@Unique
	private Map<String, IntList> frozenLib_quilt$modProtocol;

	@ModifyReturnValue(method = "current", at = @At("RETURN"))
	private static ServerStatus.Version quilt$addProtocolVersions(ServerStatus.Version original) {
		if (ModProtocol.disableQuery) return null;

		var map = new HashMap<String, IntList>();
		for (var protocol : ModProtocol.REQUIRED) {
			map.put(protocol.id(), protocol.versions());
		}

		((ModProtocolContainer) (Object) original).frozenLib_quilt$setModProtocol(map);
		return original;
	}

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void frozenLib_quilt$extendCodec(CallbackInfo ci) {
		CODEC = ModProtocolContainer.createCodec(CODEC);
	}

	public void frozenLib_quilt$setModProtocol(Map<String, IntList> map) {
		this.frozenLib_quilt$modProtocol = map;
	}

	public Map<String, IntList> frozenLib_quilt$getModProtocol() {
		return this.frozenLib_quilt$modProtocol;
	}
}
