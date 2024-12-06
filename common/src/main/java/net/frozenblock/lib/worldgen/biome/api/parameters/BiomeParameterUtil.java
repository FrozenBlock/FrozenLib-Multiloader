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

import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Climate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class BiomeParameterUtil {

	public static void addWeirdness(BiomeRunnable runnable, @NotNull List<Climate.Parameter> weirdnesses) {
		for (Climate.Parameter weirdness : weirdnesses) {
			runnable.run(weirdness);
		}
	}

	/**
	 * @return climate parameters in between both specified parameters.
	 * <p>
	 * Is NOT identical to {@link Climate.Parameter#span(Climate.Parameter)}.
	 * Instead, this will meet in the middle of both parameters.
	 */
	@NotNull
	public static Climate.Parameter inBetween(Climate.Parameter par1, Climate.Parameter par2, float width) {
		if (width >= 1F || width <= 0F) {
			throw new IllegalArgumentException("FrozenLib: Cannot run inBetween if width >= 1 or width <= 0!");
		}
		width *= 0.5F;
		long lowest = par1.min();
		long highest = par2.max();
		if (lowest > highest) {
			throw new IllegalArgumentException("FrozenLib: Cannot run inBetween when lower parameter is higher than the first!");
		}
		long difference = (highest - lowest);
		long middle = (long) (lowest + (difference * 0.5F));
		long offset = (long) (difference * width);
		return Climate.Parameter.span(middle - offset, middle + offset);
	}

	/**
	 * @return climate parameters starting from the lower first parameter, and offset from the center of the two parameters, biased towards the lower end.
	 */
	@NotNull
	public static Climate.Parameter inBetweenLowCutoff(Climate.Parameter par1, Climate.Parameter par2, float width) {
		if (width >= 1F || width <= 0F) {
			throw new IllegalArgumentException("FrozenLib: Cannot run inBetweenLowCutoff if width >= 1 or width <= 0!");
		}
		width *= 0.5F;
		long lowest = par1.min();
		long highest = par2.max();
		if (lowest > highest) {
			throw new IllegalArgumentException("FrozenLib: Cannot run inBetweenLowCutoff when lower parameter is higher than the first!");
		}
		long difference = highest - lowest;
		long middle = (long) (lowest + (difference * 0.5F));
		long offset = (long) (difference * width);
		return Climate.Parameter.span(lowest, middle - offset);
	}

	/**
	 * @return climate parameters ending at the higher second parameter, and offset from the center of the two parameters, biased towards the higher end.
	 */
	@NotNull
	public static Climate.Parameter inBetweenHighCutoff(Climate.Parameter par1, Climate.Parameter par2, float width) {
		if (width >= 1F || width <= 0F) {
			throw new IllegalArgumentException("FrozenLib: Cannot run inBetweenHighCutoff if width >= 1 or width <= 0!");
		}
		width *= 0.5F;
		float lowest = par1.min();
		float highest = par2.max();
		if (lowest > highest) {
			throw new IllegalArgumentException("FrozenLib: Cannot run inBetweenHighCutoff when lower parameter is higher than the first!");
		}
		float difference = (highest - lowest);
		float middle = lowest + (difference * 0.5F);
		float offset = difference * width;
		return Climate.Parameter.span(middle + offset, highest);
	}


	/**
	 * Returns parameters for a border between two consecutive temperature {@link Climate.Parameter}s, with the amount of space taken up per-parameter dictated by its span.
	 * <p>
	 * For reference, see {@link Temperature}.
	 * @param firstTemperature The index (starting at 0) of the first vanilla temperature parameter.
	 * @param secondTemperature The index (starting at 0) of the second vanilla temperature parameter.
	 * @param percentagePerSlot The percentage of space taken up per-parameter based on its span.
	 * @return a border between two consecutive temperature {@link Climate.Parameter}s, with the amount of space taken up per-parameter dictated by its span.
	 */
	public static Climate.@NotNull Parameter transitionBetweenTemperature(int firstTemperature, int secondTemperature, float percentagePerSlot) {
		if (firstTemperature > secondTemperature)
			throw new IllegalArgumentException("FrozenLib: Cannot run transitionBetweenTemperature when firstTemperature > secondTemperature!");
		if (firstTemperature + 1 != secondTemperature)
			throw new IllegalArgumentException("FrozenLib: Cannot run transitionBetweenTemperature when secondTemperature is not consecutive to firstTemperature!");
		if (percentagePerSlot < 0 || percentagePerSlot > 1)
			throw new IllegalArgumentException("FrozenLib: Cannot run transitionBetweenTemperature if percentagePerSlot < 0 or percentagePerSlot > 1!");

		return makeParameterBorder(
			Temperature.TEMPERATURES[firstTemperature],
			Temperature.TEMPERATURES[secondTemperature],
			percentagePerSlot
		);
	}

	/**
	 * Returns parameters for a border between two consecutive humidity {@link Climate.Parameter}s, with the amount of space taken up per-parameter dictated by its span.
	 * <p>
	 * For reference, see {@link Humidity}.
	 * @param firstHumidity The index (starting at 0) of the first vanilla humidity parameter.
	 * @param secondHumidity The index (starting at 0) of the second vanilla humidity parameter.
	 * @param percentagePerSlot The percentage of space taken up per-parameter based on its span.
	 * @return a border between two consecutive humidity {@link Climate.Parameter}s, with the amount of space taken up per-parameter dictated by its span.
	 */
	public static Climate.@NotNull Parameter transitionBetweenHumidity(int firstHumidity, int secondHumidity, float percentagePerSlot) {
		if (firstHumidity > secondHumidity)
			throw new IllegalArgumentException("FrozenLib: Cannot run transitionBetweenHumidity when firstHumidity > secondHumidity!");
		if (firstHumidity + 1 != secondHumidity)
			throw new IllegalArgumentException("FrozenLib: Cannot run transitionBetweenHumidity when secondHumidity is not consecutive to firstHumidity!");
		if (percentagePerSlot < 0 || percentagePerSlot > 1)
			throw new IllegalArgumentException("FrozenLib: Cannot run transitionBetweenHumidity if percentagePerSlot < 0 or percentagePerSlot > 1!");

		return makeParameterBorder(
			Humidity.HUMIDITIES[firstHumidity],
			Humidity.HUMIDITIES[secondHumidity],
			percentagePerSlot
		);
	}

	/**
	 * Returns parameters for a border between two consecutive erosion {@link Climate.Parameter}s, with the amount of space taken up per-parameter dictated by its span.
	 * <p>
	 * For reference, see {@link Erosion}.
	 * @param firstErosion The index (starting at 0) of the first vanilla erosion parameter.
	 * @param secondErosion The index (starting at 0) of the second vanilla erosion parameter.
	 * @param percentagePerSlot The percentage of space taken up per-parameter based on its span.
	 * @return a border between two consecutive erosion {@link Climate.Parameter}s, with the amount of space taken up per-parameter dictated by its span.
	 */
	public static Climate.@NotNull Parameter transitionBetweenErosion(int firstErosion, int secondErosion, float percentagePerSlot) {
		if (firstErosion > secondErosion)
			throw new IllegalArgumentException("FrozenLib: Cannot run transitionBetweenErosion when firstErosion > secondErosion!");
		if (firstErosion + 1 != secondErosion)
			throw new IllegalArgumentException("FrozenLib: Cannot run transitionBetweenErosion when secondErosion is not consecutive to firstErosion!");
		if (percentagePerSlot < 0 || percentagePerSlot > 1)
			throw new IllegalArgumentException("FrozenLib: Cannot run transitionBetweenErosion if percentagePerSlot < 0 or percentagePerSlot > 1!");

		return makeParameterBorder(
			Erosion.EROSIONS[firstErosion],
			Erosion.EROSIONS[secondErosion],
			percentagePerSlot
		);
	}

	/**
	 * Returns parameters for a border between two consecutive depth {@link Climate.Parameter}s, with the amount of space taken up per-parameter dictated by its span.
	 * <p>
	 * For reference, see {@link Depth}.
	 * @param firstDepth The index (starting at 0) of the first vanilla depth parameter.
	 * @param secondDepth The index (starting at 0) of the second vanilla depth parameter.
	 * @param percentagePerSlot The percentage of space taken up per-parameter based on its span.
	 * @return a border between two consecutive depth {@link Climate.Parameter}s, with the amount of space taken up per-parameter dictated by its span.
	 */
	public static Climate.@NotNull Parameter transitionBetweenDepth(int firstDepth, int secondDepth, float percentagePerSlot) {
		if (firstDepth > secondDepth)
			throw new IllegalArgumentException("FrozenLib: Cannot run transitionBetweenDepth when firstDepth > secondDepth!");
		if (firstDepth + 1 != secondDepth)
			throw new IllegalArgumentException("FrozenLib: Cannot run transitionBetweenDepth when secondDepth is not consecutive to firstDepth!");
		if (percentagePerSlot < 0 || percentagePerSlot > 1)
			throw new IllegalArgumentException("FrozenLib: Cannot run transitionBetweenDepth if percentagePerSlot < 0 or percentagePerSlot > 1!");

		return makeParameterBorder(
			Depth.DEPTHS[firstDepth],
			Depth.DEPTHS[secondDepth],
			percentagePerSlot
		);
	}

	/**
	 * Returns parameters for a border between two consecutive continentalness {@link Climate.Parameter}s, with the amount of space taken up per-parameter dictated by its span.
	 * <p>
	 * For reference, see {@link Continentalness}.
	 * @param firstContinentalness The index (starting at 0) of the first vanilla continentalness parameter.
	 * @param secondContinentalness The index (starting at 0) of the second vanilla continentalness parameter.
	 * @param percentagePerSlot The percentage of space taken up per-parameter based on its span.
	 * @return a border between two consecutive continentalness {@link Climate.Parameter}s, with the amount of space taken up per-parameter dictated by its span.
	 */
	public static Climate.@NotNull Parameter transitionBetweenContinentalness(int firstContinentalness, int secondContinentalness, float percentagePerSlot) {
		if (firstContinentalness > secondContinentalness)
			throw new IllegalArgumentException("FrozenLib: Cannot run transitionBetweenContinentalness when firstContinentalness > secondContinentalness!");
		if (firstContinentalness + 1 != secondContinentalness)
			throw new IllegalArgumentException("FrozenLib: Cannot run transitionBetweenContinentalness when secondContinentalness is not consecutive to firstContinentalness!");
		if (percentagePerSlot < 0 || percentagePerSlot > 1)
			throw new IllegalArgumentException("FrozenLib: Cannot run transitionBetweenContinentalness if percentagePerSlot < 0 or percentagePerSlot > 1!");

		return makeParameterBorder(
			Continentalness.CONTINENTALNESSES[firstContinentalness],
			Continentalness.CONTINENTALNESSES[secondContinentalness],
			percentagePerSlot
		);
	}

	/**
	 * Returns parameters for a border between two consecutive weirdness {@link Climate.Parameter}s, with the amount of space taken up per-parameter dictated by its span.
	 * <p>
	 * For reference, see {@link Weirdness}.
	 * @param firstWeirdness The index (starting at 0) of the first vanilla weirdness parameter.
	 * @param secondWeirdness The index (starting at 0) of the second vanilla weirdness parameter.
	 * @param percentagePerSlot The percentage of space taken up per-parameter based on its span.
	 * @return a border between two consecutive weirdness {@link Climate.Parameter}s, with the amount of space taken up per-parameter dictated by its span.
	 */
	public static Climate.@NotNull Parameter transitionBetweenWeirdnesses(int firstWeirdness, int secondWeirdness, float percentagePerSlot) {
		if (firstWeirdness > secondWeirdness)
			throw new IllegalArgumentException("FrozenLib: Cannot run transitionBetweenWeirdnesses when firstWeirdness > secondWeirdness!");
		if (firstWeirdness + 1 != secondWeirdness)
			throw new IllegalArgumentException("FrozenLib: Cannot run transitionBetweenWeirdnesses when secondWeirdness is not consecutive to firstWeirdness!");
		if (percentagePerSlot < 0 || percentagePerSlot > 1)
			throw new IllegalArgumentException("FrozenLib: Cannot run transitionBetweenWeirdnesses if percentagePerSlot < 0 or percentagePerSlot > 1!");

		return makeParameterBorder(
			Weirdness.WEIRDNESSES[firstWeirdness],
			Weirdness.WEIRDNESSES[secondWeirdness],
			percentagePerSlot
		);
	}

	/**
	 * Returns parameters for a border between two consecutive {@link Climate.Parameter}s, with the amount of space taken up per-parameter dictated by its span.
	 * <p>
	 * The first parameter's max point must be equal to the second parameter's min point.
	 * @param firstParameter The first {@link Climate.Parameter}.
	 * @param secondParameter The second {@link Climate.Parameter}.
	 * @param percentagePerSlot The percentage of space taken up per-parameter based on its span.
	 * @return a border between two consecutive {@link Climate.Parameter}s, with the amount of space taken up per-parameter dictated by its span.
	 */
	@Contract(value = "_, _, _ -> new", pure = true)
	public static Climate.@NotNull Parameter makeParameterBorder(
		Climate.@NotNull Parameter firstParameter, Climate.@NotNull Parameter secondParameter, float percentagePerSlot
	) {
		long border = firstParameter.max();
		long secondMin = secondParameter.min();
		if (border != secondMin)
			throw new IllegalArgumentException("FrozenLib: Cannot run makeParameterBorder when firstParameter's max is not equal to secondParameter's min!");

		long firstWidth = firstParameter.max() - firstParameter.min();
		long secondWidth = secondParameter.max() - secondParameter.min();
		return new Climate.Parameter((long) (border - (firstWidth * percentagePerSlot)), (long) (border + (secondWidth * percentagePerSlot)));
	}

	/**
	 * Finds borders between two {@link net.minecraft.world.level.biome.Climate.ParameterPoint}s, ignoring {@link Depth} and {@link Weirdness}.
	 * @param point1 The first {@link net.minecraft.world.level.biome.Climate.ParameterPoint} to find borders between.
	 * @param point2 The second {@link net.minecraft.world.level.biome.Climate.ParameterPoint} to find borders between.
	 * @param percentagePerSlot The percentage per "slot" of worldgen noise the border should take up.
	 * @return A {@link List} of found borders, ignoring {@link Depth} and {@link Weirdness}.
	 */
	public static @NotNull List<Climate.ParameterPoint> findBorderParameters(
		Climate.@NotNull ParameterPoint point1,
		Climate.@NotNull ParameterPoint point2,
		float percentagePerSlot
	) {
		List<Climate.ParameterPoint> borders = new ArrayList<>();

		List<Climate.Parameter> temperatures = findBorderParameters(point1.temperature(), point2.temperature(), percentagePerSlot);
		if (!temperatures.isEmpty()) {
			List<Climate.Parameter> humidities = findBorderParameters(point1.humidity(), point2.humidity(), percentagePerSlot);
			if (!humidities.isEmpty()) {
				List<Climate.Parameter> continentalnesses = findBorderParameters(point1.continentalness(), point2.continentalness(), percentagePerSlot);
				if (!continentalnesses.isEmpty()) {
					List<Climate.Parameter> erosions = findBorderParameters(point1.erosion(), point2.erosion(), percentagePerSlot);
					if (!erosions.isEmpty()) {
						long offset = (long) ((point1.offset() + point2.offset()) * 0.5D);
						temperatures.forEach(temperature ->
							humidities.forEach(humidity ->
								continentalnesses.forEach(continentalness ->
									erosions.forEach(erosion ->
										borders.add(
											new Climate.ParameterPoint(
												temperature,
												humidity,
												continentalness,
												erosion,
												OverworldBiomeBuilderParameters.FULL_RANGE,
												OverworldBiomeBuilderParameters.FULL_RANGE,
												offset
											)
										)
									)
								)
							)
						);
					}
				}
			}
		}
		return borders;
	}

	/**
	 * Finds borders between two {@link List}s of {@link net.minecraft.world.level.biome.Climate.ParameterPoint}s, ignoring {@link Depth} and {@link Weirdness}.
	 * <p>
	 * This is best used alongside {@link OverworldBiomeBuilderParameters#getParameters(net.minecraft.resources.ResourceKey)}
	 * @param pointList1 The first {@link List} of {@link net.minecraft.world.level.biome.Climate.ParameterPoint}s to find borders between.
	 * @param pointList2 The second {@link List} of {@link net.minecraft.world.level.biome.Climate.ParameterPoint}s to find borders between.
	 * @param percentagePerSlot The percentage per "slot" of worldgen noise the border should take up.
	 * @return A {@link List} of found borders, ignoring {@link Depth} and {@link Weirdness}.
	 */
	public static @NotNull List<Climate.ParameterPoint> findBorderParameters(
		@NotNull List<Climate.ParameterPoint> pointList1,
		@NotNull List<Climate.ParameterPoint> pointList2,
		float percentagePerSlot
	) {
		List<Climate.ParameterPoint> borders = new ArrayList<>();
		pointList1.forEach(point1 -> pointList2.forEach(point2 -> borders.addAll(findBorderParameters(point1, point2, percentagePerSlot))));
		return borders;
	}

	/**
	 * Finds borders between two {@link net.minecraft.world.level.biome.Climate.Parameter}s.
	 * <p>
	 * Do note that this keeps intersections between {@link net.minecraft.world.level.biome.Climate.Parameter}s intact.
	 * @param firstParameter The first {@link net.minecraft.world.level.biome.Climate.Parameter} to find a border between.
	 * @param secondParameter The second {@link net.minecraft.world.level.biome.Climate.Parameter} to find a border between.
	 * @param percentagePerSlot The percentage per "slot" of worldgen noise the border should take up.
	 * @return A {@link List} of found borders and intersections.
	 */
	public static @NotNull List<Climate.Parameter> findBorderParameters(
		Climate.@NotNull Parameter firstParameter, Climate.@NotNull Parameter secondParameter, float percentagePerSlot
	) {
		List<Climate.Parameter> borders = new ArrayList<>();
		if (firstParameter.equals(secondParameter)) return List.of(firstParameter);

		List<Climate.Parameter> splitFirstParam = splitParameter(firstParameter, secondParameter);
		List<Climate.Parameter> splitSecondParam = splitParameter(secondParameter, firstParameter);

		splitFirstParam.forEach(parameter1 ->
			splitSecondParam.forEach(parameter2 -> {
				if (parameter1.equals(parameter2) && !borders.contains(parameter1)) {
					borders.add(parameter1);
				} else {
					try {
						Climate.Parameter borderParameter = makeParameterBorder(parameter1, parameter2, percentagePerSlot);
						if (!borders.contains(borderParameter)) borders.add(borderParameter);
					} catch (IllegalArgumentException ignored) {}
					try {
						Climate.Parameter borderParameter = makeParameterBorder(parameter2, parameter1, percentagePerSlot);
						if (!borders.contains(borderParameter)) borders.add(borderParameter);
					} catch (IllegalArgumentException ignored) {}
				}
			})
		);

		return borders;
	}
	/**
	 * Splits a {@link net.minecraft.world.level.biome.Climate.Parameter} into multiple {@link net.minecraft.world.level.biome.Climate.Parameter}s, according to a reference.
	 * <p>
	 * Do note that this keeps intersections between {@link net.minecraft.world.level.biome.Climate.Parameter}s intact.
	 * @param parameter The first {@link net.minecraft.world.level.biome.Climate.Parameter}.
	 * @param referenceParameter The {@link net.minecraft.world.level.biome.Climate.Parameter} to reference and create borders with.
	 * @return A {@link List} of split-up {@link net.minecraft.world.level.biome.Climate.Parameter}s based on the given reference {@link net.minecraft.world.level.biome.Climate.Parameter}.
	 */
	@Contract(pure = true)
	public static @NotNull List<Climate.Parameter> splitParameter(Climate.@NotNull Parameter parameter, Climate.@NotNull Parameter referenceParameter) {
		List<Climate.Parameter> splitParameters = new ArrayList<>();

		long min = parameter.min();
		long max = parameter.max();

		long refMin = referenceParameter.min();
		long refMax = referenceParameter.max();

		if (min < refMin) {
			splitParameters.add(
				new Climate.Parameter(min, refMin)
			);
		}

		if (max > refMax) {
			splitParameters.add(
				new Climate.Parameter(refMax, max)
			);
		}

		if (min <= refMin && max >= refMax) {
			splitParameters.add(
				new Climate.Parameter(refMin, refMax)
			);
		}

		return splitParameters;
	}

	/**
	 * Presses a parameter towards its center by a given amount.
	 */
	@NotNull
	@Contract("_, _ -> new")
	public static Climate.Parameter squish(@NotNull Climate.Parameter parameter, float squish) {
		return Climate.Parameter.span(parameter.min() + squish, parameter.max() - squish);
	}

	/**
	 * @return Whether a {@link net.minecraft.world.level.biome.Climate.ParameterPoint} is considered to be weird.
	 */
	public static boolean isWeird(@NotNull Climate.ParameterPoint point) {
		return point.weirdness().max() < 0L;
	}

	@FunctionalInterface
	public interface BiomeRunnable {
		void run(Climate.Parameter weirdness);
	}
}
