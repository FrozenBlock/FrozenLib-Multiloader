package net.frozenblock.lib.event.mixin;

import net.frozenblock.lib.event.event.PlayerJoinEvents;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

	@Shadow
	@Final
	private MinecraftServer server;

	@Inject(method = "placeNewPlayer", at = @At("HEAD"))
	public void frozenLib$onPlayerJoinedPre(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo info) {
		PlayerJoinEvents.BEFORE_JOIN_SERVER.invoke(event -> event.beforePlayerJoin(this.server, player, connection, cookie));
	}

	@Inject(method = "placeNewPlayer", at = @At("TAIL"))
	public void frozenLib$onPlayerJoinedPost(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo info) {
		PlayerJoinEvents.AFTER_JOIN_SERVER.invoke(event -> event.onPlayerJoin(this.server, player, connection, cookie));
	}
}
