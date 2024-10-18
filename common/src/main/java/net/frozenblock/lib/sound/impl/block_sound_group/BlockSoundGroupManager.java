/*
 * Copyright (C) 2024 FrozenBlock
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.lib.sound.impl.block_sound_group;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import net.frozenblock.lib.FrozenLibConstants;
import net.frozenblock.lib.FrozenLibLogUtils;
import net.frozenblock.lib.resource.api.SimpleResourceReloadListener;
import net.frozenblock.lib.sound.api.block_sound_group.BlockSoundTypeOverwrite;
import net.frozenblock.lib.sound.api.block_sound_group.SoundTypeCodecs;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApiStatus.Internal
public class BlockSoundGroupManager implements SimpleResourceReloadListener<BlockSoundGroupManager.SoundGroupLoader> {
	// TODO: [Liuk] help
	private static final Logger LOGGER = LoggerFactory.getLogger("FrozenLib Block Sound Group Manager");
	private static final String DIRECTORY = "blocksoundoverwrites";

	public static final BlockSoundGroupManager INSTANCE = new BlockSoundGroupManager();

	private Map<ResourceLocation, BlockSoundTypeOverwrite> overwrites;
	private final Map<ResourceLocation, BlockSoundTypeOverwrite> queuedOverwrites = new Object2ObjectOpenHashMap<>();

	@Nullable
	public List<BlockSoundTypeOverwrite> getOverwrites() {
		if (this.overwrites != null) {
			return this.overwrites.values().stream().toList();
		}
		return null;
	}

	@Nullable
	public BlockSoundTypeOverwrite getOverwrite(ResourceLocation id) {
		return this.overwrites.get(id);
	}

	/**
	 * Adds a block with the specified {@link ResourceLocation}.
	 */
	public void addBlock(ResourceLocation key, SoundType sounds, BooleanSupplier condition) {
		if (!BuiltInRegistries.BLOCK.containsKey(key)) {
			FrozenLibLogUtils.log(
				"Error whilst adding a block to BlockSoundGroupOverwrites: The specified block id " + key.toString() + " has not been added to the Registry",
				true
			);
		} else {
			this.queuedOverwrites.put(getPath(key), new BlockSoundTypeOverwrite(key, sounds, condition));
		}
	}

	/**
	 * This will only work with vanilla blocks.
	 */
	public void addBlock(String id, SoundType sounds, BooleanSupplier condition) {
		var key = ResourceLocation.parse(id);
		addBlock(key, sounds, condition);
	}

	/**
	 * Adds a block with the specified namespace and id.
	 */
	public void addBlock(String namespace, String id, SoundType sounds, BooleanSupplier condition) {
		var key = ResourceLocation.fromNamespaceAndPath(namespace, id);
		addBlock(key, sounds, condition);
	}

	public void addBlock(Block block, SoundType sounds, BooleanSupplier condition) {
		var key = BuiltInRegistries.BLOCK.getKey(block);
		addBlock(key, sounds, condition);
	}

	public void addBlocks(Block @NotNull [] blocks, SoundType sounds, BooleanSupplier condition) {
		for (Block block : blocks) {
			var key = BuiltInRegistries.BLOCK.getKey(block);
			addBlock(key, sounds, condition);
		}
	}

	public void addBlockTag(TagKey<Block> tag, SoundType sounds, BooleanSupplier condition) {
		var tagIterable = BuiltInRegistries.BLOCK.getTag(tag);
		for (Holder<Block> block : tagIterable.get()) {
			var key = block.unwrapKey().orElseThrow().location();
			addBlock(key, sounds, condition);
		}
	}

	public static @NotNull ResourceLocation getPath(@NotNull ResourceLocation blockId) {
		return ResourceLocation.fromNamespaceAndPath(blockId.getNamespace(), DIRECTORY + "/" + blockId.getPath() + ".json");
	}

	@Override
	public CompletableFuture<SoundGroupLoader> load(ResourceManager manager, ProfilerFiller profiler, Executor executor) {
		return CompletableFuture.supplyAsync(() -> new SoundGroupLoader(manager, profiler), executor);
	}

	@Override
	public CompletableFuture<Void> apply(@NotNull SoundGroupLoader prepared, ResourceManager manager, ProfilerFiller profiler, Executor executor) {
		this.overwrites = prepared.getOverwrites();
		this.overwrites.putAll(this.queuedOverwrites);
		return CompletableFuture.runAsync(() -> {
		});
	}


	//TODO: [Liuk] This reloads alongside resource packs I believe, since block sound type overrides also work data-driven. Ask tree.
	@NotNull
	public ResourceLocation getFabricId() {
		return FrozenLibConstants.id("block_sound_group_reloader");
	}

	public static class SoundGroupLoader {
		private final ResourceManager manager;
		private final ProfilerFiller profiler;
		private final Map<ResourceLocation, BlockSoundTypeOverwrite> overwrites = new Object2ObjectOpenHashMap<>();

		public SoundGroupLoader(ResourceManager manager, ProfilerFiller profiler) {
			this.manager = manager;
			this.profiler = profiler;
			this.loadSoundOverwrites();
		}

		private void loadSoundOverwrites() {
			profiler.push("Load Sound Overwrites");
			Map<ResourceLocation, Resource> resources = manager.listResources(DIRECTORY, id -> id.getPath().endsWith(".json"));
			var entrySet = resources.entrySet();
			for (Map.Entry<ResourceLocation, Resource> entry : entrySet) {
				this.addOverwrite(entry.getKey(), entry.getValue());
			}
			profiler.pop();
		}

		private void addOverwrite(ResourceLocation id, @NotNull Resource resource) {
			BufferedReader reader;
			try {
				reader = resource.openAsReader();
			} catch (IOException e) {
				LOGGER.error(String.format("Unable to open BufferedReader for id %s", id), e);
				return;
			}

			JsonObject json = GsonHelper.parse(reader);
			DataResult<Pair<BlockSoundTypeOverwrite, JsonElement>> result = SoundTypeCodecs.SOUND_TYPE_OVERWRITE.decode(JsonOps.INSTANCE, json);

			if (result.error().isPresent()) {
				LOGGER.error(String.format("Unable to parse sound overwrite file %s. \nReason: %s", id, result.error().get().message()));
				return;
			}

			ResourceLocation overwriteId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath().substring((DIRECTORY + "/").length()));
			overwrites.put(overwriteId, result.result().orElseThrow().getFirst());
		}
		public Map<ResourceLocation, BlockSoundTypeOverwrite> getOverwrites() {
			return this.overwrites;
		}
	}
}
