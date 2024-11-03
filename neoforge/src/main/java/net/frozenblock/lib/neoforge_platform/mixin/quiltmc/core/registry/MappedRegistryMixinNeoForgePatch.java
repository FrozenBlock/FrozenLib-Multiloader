package net.frozenblock.lib.neoforge_platform.mixin.quiltmc.core.registry;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.frozenblock.lib.event.api.FrozenEvent;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.resources.ResourceKey;
import org.quiltmc.qsl.frozenblock.core.registry.api.event.RegistryEventStorage;
import org.quiltmc.qsl.frozenblock.core.registry.api.event.RegistryEvents;
import org.quiltmc.qsl.frozenblock.core.registry.impl.event.MutableRegistryEntryContextImpl;
import org.quiltmc.qsl.frozenblock.core.registry.mixin.HolderReferenceInvoker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MappedRegistry.class)
public class MappedRegistryMixinNeoForgePatch<V> {

	@Shadow
	@Final
	private ObjectList<Holder.Reference<V>> byId;

	// because of how unique works this should work fine
	@Unique
	public MutableRegistryEntryContextImpl<V> frozenLib_quilt$entryContext;

	@Unique
	public FrozenEvent<RegistryEvents.EntryAdded<V>> frozenLib_quilt$entryAddedEvent;

	@ModifyExpressionValue(
		method = "register(ILnet/minecraft/resources/ResourceKey;Ljava/lang/Object;Lnet/minecraft/core/RegistrationInfo;)Lnet/minecraft/core/Holder$Reference;",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;",
			ordinal = 0
		)
	)
	private V quilt$neoforge$eagerFillReference(V object, @Local(argsOnly = true) ResourceKey<V> key, @Local(argsOnly = true) V value, @Local(argsOnly = true) RegistrationInfo registrationInfo) {
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
	private void quilt$neoforge$invokeEntryAddEvent(ResourceKey<V> key, V entry, RegistrationInfo registrationInfo, CallbackInfoReturnable<Holder<V>> info) {
		this.frozenLib_quilt$entryContext.set(key.location(), entry, this.byId.size());
		RegistryEventStorage.as((MappedRegistry<V>) (Object) this).frozenLib_quilt$getEntryAddedEvent().invoke(e->e.onAdded(this.frozenLib_quilt$entryContext));
	}
}
