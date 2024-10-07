package net.frozenblock.lib.event.mixin;

import net.frozenblock.lib.event.event.RegistryFreezeEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltInRegistries.class)
public class BuiltInRegistriesMixin {

	@Inject(method = "freeze", at = @At("HEAD"))
	private static void frozenLib$freezeBuiltinsStart(CallbackInfo info) {
		RegistryFreezeEvents.START_REGISTRY_FREEZE.invoke(event -> event.onStartRegistryFreeze(null, true));
	}

	@Inject(method = "freeze", at = @At("TAIL"))
	private static void frozenLib$freezeBuiltinsEnd(CallbackInfo info) {
		RegistryFreezeEvents.END_REGISTRY_FREEZE.invoke(event -> event.onEndRegistryFreeze(null, true));
	}
}
