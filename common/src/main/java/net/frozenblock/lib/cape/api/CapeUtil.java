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

package net.frozenblock.lib.cape.api;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import net.frozenblock.lib.FrozenLibLogUtils;
import net.frozenblock.lib.cape.client.api.ClientCapeUtil;
import net.frozenblock.lib.cape.impl.Cape;
import net.frozenblock.lib.env.api.EnvType;
import net.frozenblock.lib.platform.api.PlatformHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Lets you add capes that can be worn using FrozenLib's in-game config.
 */
@UtilityClass
public class CapeUtil {
	private static final Map<ResourceLocation, Cape> CAPES = new HashMap<>();
	private static final List<String> CAPE_REPOS = new ArrayList<>();

	/**
	 * @return a {@link List} of the current cape repos in use.
	 */
	public static @NotNull @Unmodifiable List<String> getCapeRepos() {
		return ImmutableList.copyOf(CAPE_REPOS);
	}

	/**
	 * @return a {@link Collection} of the currently registered capes.
	 */
	public static @NotNull @Unmodifiable Collection<Cape> getCapes() {
		return CAPES.values();
	}

	/**
	 * Returns a list of all capes a player is able to wear.
	 *
	 * @param uuid The player's UUID.
	 * @return a {@link List} of all capes the player is able to wear.
	 */
	@ApiStatus.Internal
	public static @NotNull @Unmodifiable List<Cape> getUsableCapes(UUID uuid) {
		return ImmutableList.copyOf(getCapes().stream().filter(cape -> canPlayerUserCape(uuid, cape)).toList());
	}

	/**
	 * Returns a cape registered with a given {@link ResourceLocation}, if available.
	 *
	 * @param location The {@link ResourceLocation} of the cape.
	 * @return a cape registered with the given {@link ResourceLocation}, if available.
	 */
	public static Optional<Cape> getCape(ResourceLocation location) {
		return Optional.ofNullable(CAPES.get(location));
	}

	/**
	 * Returns whether a player can wear a specific cape.
	 *
	 * @param uuid   The player's UUID.
	 * @param capeID The {@link ResourceLocation} of the cape.
	 * @return whether the player can wear the cape.
	 */
	public static boolean canPlayerUserCape(UUID uuid, ResourceLocation capeID) {
		Optional<Cape> optionalCape = CapeUtil.getCape(capeID);
		return optionalCape.map(cape -> canPlayerUserCape(uuid, cape)).orElse(true);
	}

	/**
	 * Returns whether a player can wear a specific cape.
	 *
	 * @param uuid The player's UUID.
	 * @param cape The {@link Cape}.
	 * @return whether the player can wear the cape.
	 */
	public static boolean canPlayerUserCape(UUID uuid, @NotNull Cape cape) {
		return cape.allowedPlayers().map(uuids -> uuids.contains(uuid)).orElse(true);
	}

	/**
	 * Registers a cape that all players can wear.
	 *
	 * @param id        The {@link ResourceLocation} to register the cape to.
	 * @param textureId The {@link ResourceLocation} to use for the cape's texture.
	 * @param capeName  The cape's name, in {@link Component} form.
	 */
	public static void registerCape(ResourceLocation id, ResourceLocation textureId, Component capeName) {
		CAPES.put(id, new Cape(id, capeName, textureId, Optional.empty()));
	}

	/**
	 * Registers a cape that all players can wear.
	 *
	 * @param id       The {@link ResourceLocation} to register the cape to.
	 * @param capeName The cape's name, in {@link Component} form.
	 */
	public static void registerCape(ResourceLocation id, Component capeName) {
		CAPES.put(id, new Cape(id, capeName, buildCapeTextureLocation(id), Optional.empty()));
	}

	/**
	 * Registers a cape that only specific players can wear.
	 *
	 * @param id             The {@link ResourceLocation} to register the cape to.
	 * @param capeName       The cape's name, in {@link Component} form.
	 * @param allowedPlayers The list of players, identified by their account {@link UUID}s, that can wear the cape.
	 */
	public static void registerCapeWithWhitelist(ResourceLocation id, Component capeName, List<UUID> allowedPlayers) {
		CAPES.put(id, new Cape(id, capeName, buildCapeTextureLocation(id), Optional.of(allowedPlayers)));
	}

	/**
	 * Registers a cape that only specific players can wear.
	 *
	 * @param id       The {@link ResourceLocation} to register the cape to.
	 * @param capeName The cape's name, in {@link Component} form.
	 * @param uuids    The list of players, identified by their account {@link UUID}s, that can wear the cape.
	 */
	public static void registerCapeWithWhitelist(ResourceLocation id, Component capeName, UUID... uuids) {
		CAPES.put(id, new Cape(id, capeName, buildCapeTextureLocation(id), Optional.of(ImmutableList.copyOf(uuids))));
	}

	/**
	 * Registers capes from a repo.
	 *
	 * @param urlString The repo's URL.
	 */
	public static void registerCapesFromURL(String urlString) {
		try {
			URL url = URI.create(urlString).toURL();
			URLConnection request = url.openConnection();
			request.connect();

			JsonElement parsedJson = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
			JsonObject capeDir = parsedJson.getAsJsonObject();
			JsonArray capeArray = capeDir.get("capes").getAsJsonArray();

			capeArray.forEach(jsonElement -> registerCapeFromURL(jsonElement.getAsString()));
			CAPE_REPOS.add(urlString);
		} catch (IOException ignored) {
		}
	}

	/**
	 * Registers a selected cape from a URK.
	 *
	 * @param urlString The cape's URL.
	 */
	private static void registerCapeFromURL(String urlString) {
		try {
			URL url = URI.create(urlString).toURL();
			URLConnection request = url.openConnection();
			request.connect();

			JsonElement parsedJson = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
			JsonObject capeJson = parsedJson.getAsJsonObject();
			String capeId = capeJson.get("id").getAsString();
			Component capeName = Component.literal(capeJson.get("name").getAsString());
			String capeTexture = capeJson.get("texture").getAsString();
			JsonElement allowedUUIDElement = capeJson.get("allowed_uuids");
			boolean whitelisted = allowedUUIDElement != null;

			ResourceLocation capeLocation = ResourceLocation.tryParse(capeId);
			if (capeLocation != null) {
				ResourceLocation capeTextureLocation = CapeUtil.buildCapeTextureLocation(capeLocation);
				if (PlatformHelper.envType() == EnvType.CLIENT) {
					ClientCapeUtil.registerCapeTextureFromURL(capeLocation, capeTextureLocation, capeTexture);
				}

				if (!whitelisted) {
					registerCape(capeLocation, capeName);
				} else {
					List<UUID> uuidList = new ArrayList<>();
					allowedUUIDElement.getAsJsonArray().asList().forEach(jsonElement -> uuidList.add(UUID.fromString(jsonElement.getAsString())));
					registerCapeWithWhitelist(capeLocation, capeName, ImmutableList.copyOf(uuidList));
				}
			}
		} catch (IOException ignored) {
			FrozenLibLogUtils.LOGGER.error("Failed to parse Cape from URL: {}", urlString);
		}
	}

	@ApiStatus.Internal
	public static ResourceLocation buildCapeTextureLocation(@NotNull ResourceLocation cape) {
		return ResourceLocation.tryBuild(cape.getNamespace(), "textures/cape/" + cape.getPath() + ".png");
	}
}
