package net.frozenblock.lib.event.mixin;

import net.frozenblock.lib.event.event.PlayerJoinEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

	@Shadow
	@Final
	private MinecraftServer server;

	@Inject(method = "addPlayer", at = @At("HEAD"))
	private void beforeAddPlayer(ServerPlayer player, CallbackInfo info) {
		PlayerJoinEvents.BEFORE_PLAYER_ADDED_TO_LEVEL.invoke(event -> event.beforePlayerAddedToLevel(server, ServerLevel.class.cast(this), player));
	}

	@Inject(method = "addPlayer", at = @At("TAIL"))
	private void afterAddPlayer(ServerPlayer player, CallbackInfo info) {
		PlayerJoinEvents.AFTER_PLAYER_ADDED_TO_LEVEL.invoke(event -> event.onPlayerAddedToLevel(server, ServerLevel.class.cast(this), player));
	}
}
