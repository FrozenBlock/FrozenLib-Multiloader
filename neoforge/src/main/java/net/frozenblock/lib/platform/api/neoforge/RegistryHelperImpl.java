package net.frozenblock.lib.platform.api.neoforge;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
import io.netty.util.internal.UnstableApi;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.frozenblock.lib.FrozenLibConstants;
import net.frozenblock.lib.environment.api.EnvType;
import net.frozenblock.lib.platform.api.PlatformHelper;
import net.frozenblock.lib.platform.impl.ClientPayloadContext;
import net.frozenblock.lib.platform.impl.ICPlayPayloadHandler;
import net.frozenblock.lib.platform.impl.ISPlayPayloadHandler;
import net.frozenblock.lib.platform.impl.ServerPayloadContext;
import net.frozenblock.lib.registry.api.FrozenLibRegistryAttribute;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.neoforged.neoforge.registries.callback.RegistryCallback;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.frozenblock.core.registry.impl.event.DelayedRegistry;
import java.util.Map;

@UnstableApi
public class RegistryHelperImpl {
	private static final PayloadRegistrar REGISTRY = new PayloadRegistrar(FrozenLibConstants.PACKET_VERSION);

	public static <T> MappedRegistry<T> createBuildAndRegister(
		ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle, FrozenLibRegistryAttribute attribute, BuiltInRegistries.RegistryBootstrap<T> bootstrap
	) {
		RegistryBuilder<T> registryBuilder = new RegistryBuilder<>(key);

		if (attribute != null) {
			registryBuilder.sync(attribute == FrozenLibRegistryAttribute.SYNCED);
		}

		final Registry<T> registry = registryBuilder.create();

		if (bootstrap != null) {
			bootstrap.run(registry);
		}

		return (MappedRegistry<T>) registry;
	}

	public <T> Registry<T> createBuilder(ResourceKey<Registry<T>> key, boolean sync) {
		return new RegistryBuilder<>(key).sync(sync).create();
	}

	public static <T> DelayedRegistry<T> createDelayedRegistry(WritableRegistry<T> registry) {
		return new DelayedRegistry<>(registry) {
			@Override
			public boolean doesSync() {
				return this.wrapped.doesSync();
			}

			@Override
			public int getMaxId() {
				return this.wrapped.getMaxId();
			}

			@Override
			public void addCallback(@NotNull RegistryCallback<T> registryCallback) {
				this.wrapped.addCallback(registryCallback);
			}

			@Override
			public void addAlias(@NotNull ResourceLocation resourceLocation, @NotNull ResourceLocation resourceLocation1) {
				this.wrapped.addAlias(resourceLocation, resourceLocation1);
			}

			@Override
			public @NotNull ResourceLocation resolve(@NotNull ResourceLocation resourceLocation) {
				return this.wrapped.resolve(resourceLocation);
			}

			@Override
			public @NotNull ResourceKey<T> resolve(@NotNull ResourceKey<T> resourceKey) {
				return this.wrapped.resolve(resourceKey);
			}

			@Override
			public int getId(@NotNull ResourceKey<T> resourceKey) {
				return this.wrapped.getId(resourceKey);
			}

			@Override
			public int getId(@NotNull ResourceLocation resourceLocation) {
				return this.wrapped.getId(resourceLocation);
			}

			@Override
			public boolean containsValue(T t) {
				return this.wrapped.containsValue(t);
			}

			@Override
			public <A> @Nullable A getData(@NotNull DataMapType<T, A> dataMapType, @NotNull ResourceKey<T> resourceKey) {
				return this.wrapped.getData(dataMapType, resourceKey);
			}

			@Override
			public <A> @NotNull Map<ResourceKey<T>, A> getDataMap(@NotNull DataMapType<T, A> dataMapType) {
				return this.wrapped.getDataMap(dataMapType);
			}
		};
	}

	private static final Map<Pair<CustomPacketPayload.Type<?>, StreamCodec<? super RegistryFriendlyByteBuf, ?>>, PacketDist> PLAY_PACKETS = new Object2ObjectLinkedOpenHashMap<>();
	private static final Map<CustomPacketPayload.Type<?>, IPayloadHandler> PLAY_CLIENT_RECEIVERS = new Object2ObjectLinkedOpenHashMap<>();
	private static final Map<CustomPacketPayload.Type<?>, IPayloadHandler> PLAYER_SERVER_RECEIVERS = new Object2ObjectLinkedOpenHashMap<>();

	public static <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		PLAY_PACKETS.put(Pair.of(type, codec), PacketDist.CLIENT_RECEIVES);
	}

	public static <T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		PLAY_PACKETS.put(Pair.of(type, codec), PacketDist.SERVER_RECEIVES);
	}

	public static <T extends CustomPacketPayload> void registerBidirectional(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		PLAY_PACKETS.put(Pair.of(type, codec), PacketDist.BIDIRECTIONAL);
	}

	public static <T extends CustomPacketPayload> void registerClientReceiver(CustomPacketPayload.Type<T> type, ICPlayPayloadHandler<T> clientSide) {
		PLAY_CLIENT_RECEIVERS.put(type, (packet, context) -> clientSide.handle((T) packet, new ClientPayloadContext(context.player(), context.connection())));
	}

	public static <T extends CustomPacketPayload> void registerServerReceiver(CustomPacketPayload.Type<T> type, ISPlayPayloadHandler<T> serverSide) {
		PLAYER_SERVER_RECEIVERS.put(type, (packet, context) -> serverSide.handle((T) packet, new ServerPayloadContext((ServerPlayer) context.player(), context.connection())));
	}

	public static void finalizeAndBuildPackets() {
		boolean isServer = PlatformHelper.envType() == EnvType.SERVER;
		PLAY_PACKETS.forEach(
			(pair, packetDist) -> {
				CustomPacketPayload.Type type = pair.getFirst();
				StreamCodec codec = pair.getSecond();

				@Nullable IPayloadHandler clientReceiver = PLAY_CLIENT_RECEIVERS.get(type);
				@Nullable IPayloadHandler serverReceiver = PLAYER_SERVER_RECEIVERS.get(type);

				if (packetDist == PacketDist.CLIENT_RECEIVES) {
					if (isServer) {
						REGISTRY.playToClient(type, codec, (packet, ctx) -> {});
					} else {
						REGISTRY.playToClient(type, codec, clientReceiver);
					}
				} else if (packetDist == PacketDist.SERVER_RECEIVES) {
					REGISTRY.playToServer(type, codec, serverReceiver);
				} else if (packetDist == PacketDist.BIDIRECTIONAL) {
					if (isServer) {
						REGISTRY.playBidirectional(
							type,
							codec,
							new DirectionalPayloadHandler<CustomPacketPayload>(
								(packet, ctx) -> {},
								serverReceiver
							)
						);
					} else {
						REGISTRY.playBidirectional(
							type,
							codec,
							new DirectionalPayloadHandler<CustomPacketPayload>(
								clientReceiver,
								serverReceiver
							)
						);
					}
				}
			}
		);
	}

	private enum PacketDist {
		CLIENT_RECEIVES,
		SERVER_RECEIVES,
		BIDIRECTIONAL
	}
}
