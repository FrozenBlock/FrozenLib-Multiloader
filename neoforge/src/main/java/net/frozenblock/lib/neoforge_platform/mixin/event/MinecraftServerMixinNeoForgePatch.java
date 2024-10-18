package net.frozenblock.lib.neoforge_platform.mixin.event;

import com.llamalad7.mixinextras.sugar.Local;
import net.frozenblock.lib.event.event.ServerLevelEvents;
import net.frozenblock.lib.event.event.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixinNeoForgePatch {

	@Shadow
	private MinecraftServer.ReloadableResources resources;

	@Redirect(method = "createLevels", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
	private <K, V> V frozenLib$neoForge$onLoadWorld(Map<K, V> worlds, K registryKey, V serverWorld) {
		final V result = worlds.put(registryKey, serverWorld);
		ServerLevelEvents.LOAD.invoke(event -> event.onWorldLoad(MinecraftServer.class.cast(this), (ServerLevel)serverWorld));
		return result;
	}

	@Inject(method = "stopServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;close()V"))
	private void frozenLib$neoForge$onUnloadWorldAtShutdown(CallbackInfo ci, @Local ServerLevel world) {
		ServerLevelEvents.UNLOAD.invoke(event -> event.onWorldUnload(MinecraftServer.class.cast(this), world));
	}

	@Inject(method = "reloadResources", at = @At("HEAD"))
	private void frozenLib$neoForge$startResourceReload(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> info) {
		ServerLifecycleEvents.START_DATA_PACK_RELOAD.invoke(
			event -> event.startDataPackReload(MinecraftServer.class.cast(this), this.resources.resourceManager())
		);
	}

	@Inject(method = "reloadResources", at = @At("TAIL"))
	private void frozenLib$neoForge$endResourceReload(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> info) {
		info.getReturnValue().handleAsync((value, throwable) -> {
			// Hook into fail
			ServerLifecycleEvents.END_DATA_PACK_RELOAD.invoke(
				event -> event.endDataPackReload(MinecraftServer.class.cast(this), this.resources.resourceManager(), throwable == null)
			);
			return value;
		}, MinecraftServer.class.cast(this));
	}

	@Inject(method = "saveEverything", at = @At("HEAD"))
	private void frozenLib$neoForge$startSave(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> info) {
		ServerLifecycleEvents.BEFORE_SAVE.invoke(event -> event.onBeforeSave(MinecraftServer.class.cast(this), flush, force));
	}

	@Inject(method = "saveEverything", at = @At("TAIL"))
	private void frozenLib$neoForge$endSave(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> info) {
		ServerLifecycleEvents.AFTER_SAVE.invoke(event -> event.onAfterSave(MinecraftServer.class.cast(this), flush, force));
	}
}
