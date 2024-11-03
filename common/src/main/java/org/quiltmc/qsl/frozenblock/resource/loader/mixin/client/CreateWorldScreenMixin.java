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

package org.quiltmc.qsl.frozenblock.resource.loader.mixin.client;

import java.util.function.Consumer;
import net.frozenblock.lib.environment.api.EnvType;
import net.frozenblock.lib.environment.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.WorldDataConfiguration;
import org.quiltmc.qsl.frozenblock.resource.loader.api.ResourceLoaderEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Modified to work on Fabric & NeoForge.
 */
@Environment(EnvType.CLIENT)
@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin {

    @Inject(
		method = "applyNewPackConfig",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/WorldLoader;load(Lnet/minecraft/server/WorldLoader$InitConfig;Lnet/minecraft/server/WorldLoader$WorldDataSupplier;Lnet/minecraft/server/WorldLoader$ResultFactory;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"
		)
    )
    private void onDataPackLoadStart(
		PackRepository packRepository,
		WorldDataConfiguration worldDataConfiguration,
		Consumer<WorldDataConfiguration> consumer,
		CallbackInfo info
	) {
        ResourceLoaderEvents.START_DATA_PACK_RELOAD.invoke(event -> event.onStartDataPackReload(null, null));
    }

    @Inject(
		method = "openFresh",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/WorldLoader;load(Lnet/minecraft/server/WorldLoader$InitConfig;Lnet/minecraft/server/WorldLoader$WorldDataSupplier;Lnet/minecraft/server/WorldLoader$ResultFactory;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"
		)
    )
    private static void onDataPackLoadStart(
		Minecraft client,
		Screen parent,
		CallbackInfo info
	) {
        ResourceLoaderEvents.START_DATA_PACK_RELOAD.invoke(event -> event.onStartDataPackReload(null, null));
    }

	@Inject(
		method = {"method_49629", "lambda$applyNewPackConfig$17(Ljava/util/function/Consumer;Ljava/lang/Void;Ljava/lang/Throwable;)Ljava/lang/Object;",},
		at = @At(
			value = "INVOKE",
			target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Throwable;)V",
			shift = At.Shift.AFTER,
			remap = false
		),
		require = 1
	)
	private void onFailDataPackLoading(Consumer<WorldDataConfiguration> consumer, Void unused, Throwable throwable, CallbackInfoReturnable<Object> info) {
		ResourceLoaderEvents.END_DATA_PACK_RELOAD.invoke(event -> event.onEndDataPackReload(null, null, throwable));
	}
}
