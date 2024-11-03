package net.frozenblock.lib.config.impl.networking.client;

import java.util.List;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.frozenblock.lib.config.impl.networking.ConfigSyncPacket;
import net.frozenblock.lib.environment.api.EnvType;
import net.frozenblock.lib.environment.api.Environment;
import net.frozenblock.lib.platform.api.PacketHelper;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class ClientConfigSyncNetworking {

	@Environment(EnvType.CLIENT)
	public static void sendC2S(@NotNull Iterable<Config<?>> configs) {
		//TODO: [Liuk] find a way to check this on common
		//if (!ClientPlayNetworking.canSend(PACKET_TYPE)) return;

		for (Config<?> config : configs) {
			if (!config.supportsSync()) continue;
			ConfigSyncPacket<?> packet = new ConfigSyncPacket<>(config.modId(), config.configClass().getName(), config.instance());
			PacketHelper.sendToServer(packet);
		}
	}

	@Environment(EnvType.CLIENT)
	public static void sendC2S() {
		sendC2S(ConfigRegistry.getAllConfigs());
	}

	@Environment(EnvType.CLIENT)
	public static <T> void trySendC2S(Config<T> config) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft == null) return; // Added to fix a random Forge issue

		if (ConfigSyncPacket.hasPermissionsToSendSync(Minecraft.getInstance().player, false))
			sendC2S(List.of(config));
	}

}
