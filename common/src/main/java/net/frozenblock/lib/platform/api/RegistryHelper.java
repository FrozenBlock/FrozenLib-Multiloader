package net.frozenblock.lib.platform.api;


import com.mojang.serialization.Lifecycle;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.frozenblock.lib.platform.impl.ICPlayPayloadHandler;
import net.frozenblock.lib.platform.impl.ISPlayPayloadHandler;
import net.frozenblock.lib.registry.api.FrozenLibRegistryAttribute;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import org.quiltmc.qsl.frozenblock.core.registry.impl.event.DelayedRegistry;

public class RegistryHelper {
	/**
	 * Automatically creates and registers a mapped registry
	 */
	@ExpectPlatform
	public static <T> MappedRegistry<T> createBuildAndRegister(
		ResourceKey<? extends Registry<T>> key,
		Lifecycle lifecycle,
		FrozenLibRegistryAttribute attribute,
		BuiltInRegistries.RegistryBootstrap<T> bootstrap
	) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <T> Registry<T> createBuilder(ResourceKey<Registry<T>> key, boolean sync) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <T> DelayedRegistry<T> createDelayedRegistry(WritableRegistry<T> registry) {
		throw new AssertionError();
	}

	/**
	 * Registers a server - client packet.
	 */
	@ExpectPlatform
	public static <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		throw new AssertionError();
	}

	/**
	 * Registers a client -> server packet.
	 */
	@ExpectPlatform
	public static <T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		throw new AssertionError();
	}

	/**
	 * Registers a bidirectional packet.
	 */
	@ExpectPlatform
	public static <T extends CustomPacketPayload> void registerBidirectional(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		throw new AssertionError();
	}

	/**
	 * Registers a client listener for a packet (client)
	 * */
	@ExpectPlatform
	public static <T extends CustomPacketPayload> void registerClientReceiver(CustomPacketPayload.Type<T> type, ICPlayPayloadHandler<T> clientSide) {
		throw new AssertionError();
	}

	/**
	 * Registers a server listener for a packet (server)
	 * */
	@ExpectPlatform
	public static <T extends CustomPacketPayload> void registerServerReceiver(CustomPacketPayload.Type<T> type, ISPlayPayloadHandler<T> serverSide) {
		throw new AssertionError();
	}

	/**
	 * Finalizes and builds all packets.
	 * <p>
	 * This method was added to help for registering packets on NeoForge, as in the server dist you can't exactly call client classes.
	 * <p>
	 * This has no impact on Fabric.
	 */
	@ExpectPlatform
	public static void finalizeAndBuildPackets() {
		throw new AssertionError();
	}

	//TODO: [Liuk] Implementing configuration packets
}
