package net.frozenblock.lib.event.mixin;

import net.frozenblock.lib.event.event.RegistryFreezeEvents;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MappedRegistry.class)
public class MappedRegistryMixin<T> {

	@Inject(method = "freeze", at = @At("HEAD"))
	private void frozenLib$freezeStart(CallbackInfoReturnable<Registry<T>> info) {
		RegistryFreezeEvents.START_REGISTRY_FREEZE.invoke(event -> event.onStartRegistryFreeze(MappedRegistry.class.cast(this), false));
	}

	@Inject(method = "freeze", at = @At("TAIL"))
	private void frozenLib$freezeEnd(CallbackInfoReturnable<Registry<T>> info) {
		RegistryFreezeEvents.END_REGISTRY_FREEZE.invoke(event -> event.onEndRegistryFreeze(MappedRegistry.class.cast(this), false));
	}
}
