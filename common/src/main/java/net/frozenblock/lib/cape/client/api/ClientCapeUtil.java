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

package net.frozenblock.lib.cape.client.api;

import com.google.gson.JsonIOException;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.experimental.UtilityClass;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.resource.api.ResourceListenerAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
@UtilityClass
@Environment(EnvType.CLIENT)
public class ClientCapeUtil {
	/**
	 * The directory to store saved cape textures to.
	 */
	public static final Path CAPE_CACHE_PATH = Minecraft.getInstance().gameDirectory.toPath().resolve("frozenlib_cape_cache");
	private static final Map<String, Pair<ResourceLocation, ResourceLocation>> CAPES = new Object2ObjectLinkedOpenHashMap<>();

	@ApiStatus.Internal
	public static void registerCapeTextureFromURL(
		@NotNull ResourceLocation capeLocation, ResourceLocation capeTextureLocation, String textureURL
	) throws JsonIOException {
		if (!CAPES.containsKey(textureURL)) {
			CAPES.put(textureURL, Pair.of(capeLocation, capeTextureLocation));
		}
	}


	@ApiStatus.Internal
	public static void init() {
		ResourceListenerAPI.getClient().registerReloadListener((ResourceManagerReloadListener) resourceManager -> CAPES.forEach((textureURL, capeAndTextureLocation) -> {
			CompletableFuture<ResourceLocation> completableFuture = new CompletableFuture<>();
			ResourceLocation capeLocation = capeAndTextureLocation.getFirst();
			ResourceLocation capeTextureLocation = capeAndTextureLocation.getSecond();
			HttpTexture httpTexture = new HttpTexture(
				CAPE_CACHE_PATH.resolve(capeLocation.getNamespace()).resolve(capeLocation.getPath() + ".png").toFile(),
				textureURL,
				DefaultPlayerSkin.getDefaultTexture(),
				false,
				() -> completableFuture.complete(capeTextureLocation)
			);
			Minecraft.getInstance().getTextureManager().register(capeTextureLocation, httpTexture);
		}));
	}
}
