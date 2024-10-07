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

package net.frozenblock.lib.advancement.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

/**
 * An API used for the modification of {@link Advancement}s upon runtime.
 * <p>
 * This is useful for, in example, adding new food types to the "Balanced Diet" advancement.
 */
public final class AdvancementAPI {
	private AdvancementAPI() {
	}

	/**
	 * Makes a copy of {@link AdvancementRewards#EMPTY} for use in the Advancement API.
	 * <p>
	 * Use only when needed, as this will increase memory usage.
	 */
	public static void setupRewards(@NotNull Advancement advancement) {
		if (advancement.rewards() == AdvancementRewards.EMPTY) {
			IAdvancement.class.cast(advancement).frozenLib$setRewards(new AdvancementRewards(0, List.of(), List.of(), Optional.empty()));
		}
	}

	/**
	 * Makes a copy of {@link AdvancementRequirements#EMPTY} for use in the Advancement API.
	 * <p>
	 * Use only when needed, as this will increase memory usage.
	 */
	public static void setupRequirements(@NotNull Advancement advancement) {
		if (advancement.requirements() == AdvancementRequirements.EMPTY) {
			IAdvancement.class.cast(advancement).frozenLib$setRequirements(new AdvancementRequirements(List.of()));
		}
	}

	public static void setupCriteria(@NotNull Advancement advancement) {
		if (!(advancement.criteria() instanceof HashMap<String, Criterion<?>>)) {
			IAdvancement.class.cast(advancement).frozenLib$setCriteria(new HashMap<>(advancement.criteria()));
		}
	}

	/**
	 * Adds a {@link Criterion} to be tracked by the advancement, identified with a {@link String}.
	 * <p>
	 * In order to use this as a requirement, see {@link #addRequirementsAsNewList(Advancement, AdvancementRequirements)} and {@link #addRequirementsToList(Advancement, List)}.
	 *
	 * @param advancement The {@link Advancement} to modify.
	 * @param key         The {@link String} to identify the {@link Criterion} with.
	 * @param criterion   the {@link Criterion} instance to track.
	 */
	public static void addCriteria(Advancement advancement, String key, Criterion<?> criterion) {
		if (criterion == null) return;
		setupCriteria(advancement);
		advancement.criteria().putIfAbsent(key, criterion);
	}

	/**
	 * Adds a list of requirements that a player can complete to finish an {@link Advancement}.
	 * <p>
	 * These requirements will finish an {@link Advancement} even if other requirements arent.
	 * To add requirements to an existing list so all requirements have to be met, use {@link #addRequirementsToList(Advancement, List)}.
	 *
	 * @param advancement  The {@link Advancement} to modify.
	 * @param requirements The list of requirements, in {@link String} form, that can be completed.
	 */
	public static void addRequirementsAsNewList(Advancement advancement, AdvancementRequirements requirements) {
		if (requirements == null || requirements.isEmpty()) return;
		setupRequirements(advancement);
		List<List<String>> list = new ArrayList<>(advancement.requirements().requirements());
		list.addAll(requirements.requirements());
		IAdvancement.IARequirements.class.cast(advancement.requirements()).frozenLib$setRequirements(Collections.unmodifiableList(list));
	}

	/**
	 * Adds a list of requirements for a player to complete in order to finish an {@link Advancement}.
	 * <p>
	 * This will be added to the first list of requirements in an {@link Advancement}.
	 *
	 * @param advancement  The {@link Advancement} to modify.
	 * @param requirements The list of requirements, in {@link String} form, required to be completed..
	 */
	public static void addRequirementsToList(Advancement advancement, List<String> requirements) {
		if (requirements == null || requirements.isEmpty()) return;
		setupRequirements(advancement);
		List<List<String>> list = new ArrayList<>(advancement.requirements().requirements());
		if (list.isEmpty()) {
			list.add(requirements);
		} else {
			List<String> existingList = list.getFirst();
			List<String> finalList = new ArrayList<>(existingList);
			finalList.addAll(requirements);
			list.add(Collections.unmodifiableList(finalList));
			list.remove(existingList);
		}
		IAdvancement.IARequirements.class.cast(advancement.requirements()).frozenLib$setRequirements(Collections.unmodifiableList(list));
	}

	/**
	 * Adds a list of loot tables to reward a player for completing the {@link Advancement}.
	 *
	 * @param advancement The {@link Advancement} to modify.
	 * @param lootTables  The list of loot tables, in {@link ResourceLocation} form, to grant to the player.
	 */
	public static void addLootTables(Advancement advancement, @NotNull List<ResourceKey<LootTable>> lootTables) {
		if (lootTables.isEmpty()) return;
		setupRewards(advancement);
		AdvancementRewards rewards = advancement.rewards();
		List<ResourceKey<LootTable>> newLoot = new ArrayList<>(rewards.loot());
		newLoot.addAll(lootTables);
		IAdvancement.IALoot.class.cast(rewards).frozenLib$setLoot(Collections.unmodifiableList(newLoot));
	}

	/**
	 * Adds a list of recipes as rewards for completing the {@link Advancement}.
	 *
	 * @param advancement The {@link Advancement} to modify.
	 * @param recipes     The list of recipes, in {@link ResourceLocation} form, to grant to the player.
	 */
	public static void addRecipes(@NotNull Advancement advancement, List<ResourceLocation> recipes) {
		AdvancementRewards rewards = advancement.rewards();
		List<ResourceLocation> newLoot = new ArrayList<>(rewards.recipes());
		newLoot.addAll(recipes);
		IAdvancement.IARecipe.class.cast(rewards).frozenLib$setRecipes(Collections.unmodifiableList(newLoot));
	}
}
