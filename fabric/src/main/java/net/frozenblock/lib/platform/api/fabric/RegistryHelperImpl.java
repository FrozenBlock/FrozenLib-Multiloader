package net.frozenblock.lib.platform.api.fabric;

import com.mojang.serialization.Lifecycle;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.frozenblock.lib.fabric.api.IBuild;
import net.frozenblock.lib.platform.impl.ClientPayloadContext;
import net.frozenblock.lib.platform.impl.ICPlayPayloadHandler;
import net.frozenblock.lib.platform.impl.ISPlayPayloadHandler;
import net.frozenblock.lib.platform.impl.ServerPayloadContext;
import net.frozenblock.lib.registry.api.FrozenLibRegistryAttribute;
import net.minecraft.client.Minecraft;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import org.quiltmc.qsl.frozenblock.core.registry.impl.event.DelayedRegistry;

// TODO: [Liuk] fix
public class RegistryHelperImpl {

	public static <T> MappedRegistry<T> createBuildAndRegister(
		ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle, FrozenLibRegistryAttribute attribute, BuiltInRegistries.RegistryBootstrap<T> bootstrap
	) {
		var registry = new MappedRegistry<>(key, lifecycle, false);
		var fabricRegistryBuilder = FabricRegistryBuilder.from(registry);

		if (attribute != null) {
			fabricRegistryBuilder.attribute(
				attribute == FrozenLibRegistryAttribute.SYNCED ?
					net.fabricmc.fabric.api.event.registry.RegistryAttribute.SYNCED :
					net.fabricmc.fabric.api.event.registry.RegistryAttribute.MODDED
			);
		}

		var registeredRegistry = fabricRegistryBuilder.buildAndRegister();

		if (bootstrap != null) {
			bootstrap.run(registeredRegistry);
		}

		return registeredRegistry;
	}

	@SuppressWarnings("unchecked") // L java
	public static <T> Registry<T> createBuilder(ResourceKey<Registry<T>> key, boolean sync) {
		final var builder = FabricRegistryBuilder.createSimple(key);
		if (sync) builder.attribute(net.fabricmc.fabric.api.event.registry.RegistryAttribute.SYNCED);
		return (Registry<T>) IBuild.class.cast(builder).build();
	}

	public static <T> DelayedRegistry<T> createDelayedRegistry(WritableRegistry<T> registry) {
		return null;
	}

	public static <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		PayloadTypeRegistry.playS2C().register(type, codec);
	}

	public static <T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		PayloadTypeRegistry.playC2S().register(type, codec);
	}

	public static <T extends CustomPacketPayload> void registerBidirectional(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		registerS2C(type, codec);
		registerC2S(type, codec);
	}

	public static <T extends CustomPacketPayload> void registerClientReceiver(
		CustomPacketPayload.Type<T> type, ICPlayPayloadHandler<T> clientSide
	) {
		ClientPlayNetworking.registerGlobalReceiver(
			type,
			(payload, context) -> clientSide.handle(
				payload,
				new ClientPayloadContext(
					context.player(),
					Minecraft.getInstance().getConnection().getConnection()
				)
			)
		);
	}

	public static <T extends CustomPacketPayload> void registerServerReceiver(
		CustomPacketPayload.Type<T> type, ISPlayPayloadHandler<T> serverSide
	) {
		ServerPlayNetworking.registerGlobalReceiver(
			type,
			(payload, context) -> serverSide.handle(
				payload,
				new ServerPayloadContext(
					context.player(),
					context.player().connection.connection
				)
			)
		);
	}

	public static void finalizeAndBuildPackets() {
	}
}
