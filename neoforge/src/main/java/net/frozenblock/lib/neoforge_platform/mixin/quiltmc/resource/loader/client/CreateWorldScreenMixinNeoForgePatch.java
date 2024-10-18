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

package net.frozenblock.lib.neoforge_platform.mixin.quiltmc.resource.loader.client;

import net.frozenblock.lib.env.api.EnvType;
import net.frozenblock.lib.env.api.Environment;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import org.quiltmc.qsl.frozenblock.resource.loader.api.ResourceLoaderEvents;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixinNeoForgePatch {

	@Dynamic
	@Inject(
		method = {"m_qcsfhvrb", "method_41851", "lambda$openFresh$2"},
		at = @At("HEAD"),
		require = 1
	)
	private static void onEndDataPackLoadOnOpen(
		CloseableResourceManager resourceManager,
		ReloadableServerResources resources,
		LayeredRegistryAccess<?> layeredRegistryAccess,
		@Coerce Object object,
		CallbackInfoReturnable<WorldCreationContext> info
	) {
		ResourceLoaderEvents.END_DATA_PACK_RELOAD.invoke(event -> event.onEndDataPackReload(null, resourceManager, null));
	}

	@Dynamic
	@Inject(
		method = {"m_spzhhpkv", "method_45681", "lambda$applyNewPackConfig$13"},
		at = @At("HEAD"),
		require = 1
	)
	private static void onCreateDataPackLoadEnd(
		WorldLoader.DataLoadContext loadContext,
		CallbackInfoReturnable<WorldCreationContext> info
	) {
		ResourceLoaderEvents.END_DATA_PACK_RELOAD.invoke(event -> event.onEndDataPackReload(null, loadContext.resources(), null));
	}
}
