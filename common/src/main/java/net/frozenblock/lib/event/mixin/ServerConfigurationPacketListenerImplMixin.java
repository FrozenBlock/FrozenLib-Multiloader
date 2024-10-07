package net.frozenblock.lib.event.mixin;

import net.frozenblock.lib.event.event.ServerConfigurationConnectionEvents;
import net.frozenblock.lib.event.impl.FrozenServerConfigurationNetworkHandler;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerConfigurationPacketListenerImpl.class)
public abstract class ServerConfigurationPacketListenerImplMixin extends ServerCommonPacketListenerImpl implements FrozenServerConfigurationNetworkHandler {

	public ServerConfigurationPacketListenerImplMixin(MinecraftServer server, Connection connection, CommonListenerCookie cookie) {
		super(server, connection, cookie);
	}

	@Inject(method = "startConfiguration", at = @At("HEAD"))
	private void frozenlib$onClientReady(CallbackInfo info) {
		ServerConfigurationConnectionEvents.BEFORE_CONFIGURE.invoke(e -> e.beforeSendConfiguration(ServerConfigurationPacketListenerImpl.class.cast(this), this.server));
	}
}
