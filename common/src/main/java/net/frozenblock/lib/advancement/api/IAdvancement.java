package net.frozenblock.lib.advancement.api;

import java.util.List;
import java.util.Map;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Implemented in mixin class
 */
public interface IAdvancement {
	void frozenLib$setRewards(AdvancementRewards rewards);

	void frozenLib$setRequirements(AdvancementRequirements requirements);

	void frozenLib$setCriteria(Map<String, Criterion<?>> criteria);

	interface IARequirements {
		void frozenLib$setRequirements(List<List<String>> requirements);
	}

	interface IALoot {
		void frozenLib$setLoot(List<ResourceKey<LootTable>> loot);
	}

	interface IARecipe {
		void frozenLib$setRecipes(List<ResourceLocation> recipe);
	}
}
