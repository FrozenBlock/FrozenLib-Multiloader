package net.frozenblock.lib.neoforge_platform.mixin.event.client;

import net.frozenblock.lib.env.api.EnvType;
import net.frozenblock.lib.env.api.Environment;
import net.frozenblock.lib.event.event.client.ClientLifecycleEvents;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixinNeoForgePatch {

	@Inject(
		method = "destroy",
		at = @At(
			value = "INVOKE",
			target = "Lorg/slf4j/Logger;info(Ljava/lang/String;)V",
			shift = At.Shift.AFTER,
			remap = false
		)
	)
	private void frozenLib$onStopping(CallbackInfo info) {
		ClientLifecycleEvents.CLIENT_STOPPING.invoke(event -> event.onClientStopping((Minecraft) (Object) this));
	}

	// We inject after the thread field is set so `ThreadExecutor#getThread` will work
	@Inject(
		method = "run",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/Minecraft;gameThread:Ljava/lang/Thread;",
			shift = At.Shift.AFTER,
			ordinal = 0
		)
	)
	private void frozenLib$onStart(CallbackInfo info) {
		ClientLifecycleEvents.CLIENT_STARTED.invoke(event -> event.onClientStarted((Minecraft) (Object) this));
	}
}
