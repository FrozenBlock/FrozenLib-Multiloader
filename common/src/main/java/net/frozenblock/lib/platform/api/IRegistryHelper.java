package net.frozenblock.lib.platform.api;


import com.mojang.serialization.Lifecycle;
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

public interface IRegistryHelper {
	/**
	 * Automatically creates and registers a mapped registry
	 */
	<T> MappedRegistry<T> createBuildAndRegister(ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle, FrozenLibRegistryAttribute attribute, BuiltInRegistries.RegistryBootstrap<T> bootstrap);

	<T> Registry<T> createBuilder(ResourceKey<Registry<T>> key, boolean sync);

	<T> DelayedRegistry<T> createDelayedRegistry(WritableRegistry<T> registry);

	/**
	 * Registers a server - client packet.
	 */
	<T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec);

	/**
	 * Registers a client -> server packet.
	 */
	<T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec);

	/**
	 * Registers a bidirectional packet.
	 */
	<T extends CustomPacketPayload> void registerBidirectional(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec);

	/**
	 * Registers a client listener for a packet (client)
	 * */
	<T extends CustomPacketPayload> void registerClientReceiver(CustomPacketPayload.Type<T> type, ICPlayPayloadHandler<T> clientSide);

	/**
	 * Registers a server listener for a packet (server)
	 * */
	<T extends CustomPacketPayload> void registerServerReceiver(CustomPacketPayload.Type<T> type, ISPlayPayloadHandler<T> serverSide);

	/**
	 * Finalizes and builds all packets.
	 * <p>
	 * This method was added to help for registering packets on NeoForge, as in the server dist you can't exactly call client classes.
	 * <p>
	 * This has no impact on Fabric.
	 */
	void finalizeAndBuildPackets();

	//TODO: [Liuk] Implementing configuration packets?
}
