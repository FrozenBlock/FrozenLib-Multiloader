package net.frozenblock.lib.neoforge.mixin.event;

import java.util.List;
import net.frozenblock.lib.event.event.ServerLifecycleEvents;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin exists as there is no {@code joined} parameter in {@link net.neoforged.neoforge.event.OnDatapackSyncEvent}.
 */
@Mixin(PlayerList.class)
public abstract class PlayerListMixinNeoForgePatch {

	@Shadow
	public abstract List<ServerPlayer> getPlayers();

	@Inject(
		method = "placeNewPlayer",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;<init>(Ljava/util/Collection;)V"
		)
	)
	public void frozenLib$neoForge$onSyncedDataPacks(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo info) {
		ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.invoke(event -> event.onSyncDataPackContents(player, true));
	}

	@Inject(
		method = "reloadResources",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/network/protocol/common/ClientboundUpdateTagsPacket;<init>(Ljava/util/Map;)V"
		)
	)
	public void frozenLib$neoForge$onDataPacksReloaded(CallbackInfo info) {
		this.getPlayers().forEach(player ->
			ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.invoke(event -> event.onSyncDataPackContents(player, false))
		);
	}
}
