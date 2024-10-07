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

package net.frozenblock.lib.worldgen.biome.api.parameters;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.experimental.UtilityClass;
import net.frozenblock.lib.worldgen.biome.impl.impl.BiomeParameters;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * This class contains a copy of {@link OverworldBiomeBuilder}'s data, for ease of use.
 * <p>
 * It also lets you obtain the parameters for any vanilla biome.
 */
@UtilityClass
public class OverworldBiomeBuilderParameters {
	public static final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1F, 1F);

	public static final Map<ResourceLocation, BiomeParameters> BIOMES = new LinkedHashMap<>();
	private static boolean hasRun = false;

	/**
	 * @param location The biome to check.
	 * @return the parameters of the biome.
	 */
	public static BiomeParameters getParameters(ResourceLocation location) {
		runBiomes();
		return getOrCreateParameters(location);
	}

	private static void runBiomes() {
		if (!hasRun) {
			hasRun = true;
			addBiomes(pair -> addParameters(pair.getFirst(), pair.getSecond()));
		}
	}

	/**
	 * @param key The biome to check.
	 * @return the parameters of the biome.
	 */
	public static BiomeParameters getParameters(@NotNull ResourceKey<Biome> key) {
		return getParameters(key.location());
	}

	private static void addParameters(ResourceLocation location, Climate.ParameterPoint parameters) {
		BiomeParameters biomeParameters = getOrCreateParameters(location);
		biomeParameters.add(parameters);
	}

	private static BiomeParameters getOrCreateParameters(ResourceLocation location) {
		if (BIOMES.containsKey(location)) {
			return BIOMES.get(location);
		} else {
			BiomeParameters parameters = new BiomeParameters();
			BIOMES.put(location, parameters);
			return parameters;
		}
	}

	/**
	 * @param parameters The {@link BiomeParameters} to use.
	 * @return a list of {@link Climate.ParameterPoint}s.
	 */
	@Contract(pure = true)
	public static List<Climate.ParameterPoint> points(@NotNull BiomeParameters parameters) {
		return parameters.points;
	}

	/**
	 * @param key The biome to check.
	 * @return a list of the biome's {@link Climate.ParameterPoint}s.
	 */
	public static List<Climate.ParameterPoint> points(@NotNull ResourceKey<Biome> key) {
		return points(key.location());
	}

	/**
	 * @param location The biome to check.
	 * @return a list of the biome's {@link Climate.ParameterPoint}s.
	 */
	public static List<Climate.ParameterPoint> points(ResourceLocation location) {
		return points(OverworldBiomeBuilderParameters.getParameters(location));
	}

	private static void addBiomes(Consumer<Pair<ResourceLocation, Climate.ParameterPoint>> key) {
		ImmutableList.Builder<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> builder = new ImmutableList.Builder<>();
		new OverworldBiomeBuilder().addBiomes(parameterPointResourceKeyPair -> builder.add(parameterPointResourceKeyPair));
		builder.build().forEach(parameterPointResourceKeyPair -> key.accept(new Pair<>(parameterPointResourceKeyPair.getSecond().location(), parameterPointResourceKeyPair.getFirst())));
	}
}
