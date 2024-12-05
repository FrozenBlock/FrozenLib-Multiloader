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

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Lifecycle;
import net.frozenblock.lib.event.api.FrozenEvent;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.quiltmc.qsl.frozenblock.core.registry.api.event.RegistryEventStorage;
import org.quiltmc.qsl.frozenblock.core.registry.api.event.RegistryEvents;
import org.quiltmc.qsl.frozenblock.core.registry.impl.event.MutableRegistryEntryContextImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Stores and invokes registry events.
 * Handles applying and creating sync data.
 */
@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<V> implements Registry<V>, RegistryEventStorage<V> {
	@Unique
	public MutableRegistryEntryContextImpl<V> frozenLib_quilt$entryContext;

	@Unique
	public FrozenEvent<RegistryEvents.EntryAdded<V>> frozenLib_quilt$entryAddedEvent;

	@Inject(method = "<init>(Lnet/minecraft/resources/ResourceKey;Lcom/mojang/serialization/Lifecycle;Z)V", at = @At("TAIL"))
	private void hackBecauseMixinHatesMe(ResourceKey<? extends Registry<V>> key, Lifecycle lifecycle, boolean useIntrusiveHolders, CallbackInfo ci) {
		this.frozenLib_quilt$entryContext = new MutableRegistryEntryContextImpl<>(this);
		this.frozenLib_quilt$entryAddedEvent = FrozenEvent.createEvent(RegistryEvents.EntryAdded.class);
	}

	@ModifyExpressionValue(
		method = "register(Lnet/minecraft/resources/ResourceKey;Ljava/lang/Object;Lnet/minecraft/core/RegistrationInfo;)Lnet/minecraft/core/Holder$Reference;",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;",
			ordinal = 0
		),
		require = 0
	)
	private V quilt$eagerFillReference(V object, @Local(argsOnly = true) ResourceKey<V> key, @Local(argsOnly = true) V value, @Local(argsOnly = true) RegistrationInfo registrationInfo) {
		if (object instanceof Holder.Reference reference) {
			HolderReferenceInvoker.class.cast(reference).invokeBindValue(value);
		}
		return object;
	}

	/**
	 * Invokes the entry add event.
	 */
	@SuppressWarnings({"ConstantConditions", "unchecked"})
	@Inject(
		method = "register(Lnet/minecraft/resources/ResourceKey;Ljava/lang/Object;Lnet/minecraft/core/RegistrationInfo;)Lnet/minecraft/core/Holder$Reference;",
		at = @At("RETURN"),
		require = 0
	)
	private void quilt$invokeEntryAddEvent(ResourceKey<V> key, V entry, RegistrationInfo registrationInfo, CallbackInfoReturnable<Holder<V>> cir, @Local int i) {
		this.frozenLib_quilt$entryContext.set(key.location(), entry, i);
		RegistryEventStorage.as((MappedRegistry<V>) (Object) this).frozenLib_quilt$getEntryAddedEvent().invoke(e->e.onAdded(this.frozenLib_quilt$entryContext));
	}

	@Override
	public FrozenEvent<RegistryEvents.EntryAdded<V>> frozenLib_quilt$getEntryAddedEvent() {
		return this.frozenLib_quilt$entryAddedEvent;
	}
}
