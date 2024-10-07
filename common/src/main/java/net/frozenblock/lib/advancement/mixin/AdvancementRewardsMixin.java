package net.frozenblock.lib.advancement.mixin;

import java.util.List;
import net.frozenblock.lib.advancement.api.IAdvancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AdvancementRewards.class)
public class AdvancementRewardsMixin implements IAdvancement.IALoot, IAdvancement.IARecipe {

	@Shadow
	@Final
	@Mutable
	private List<ResourceKey<LootTable>> loot;

	@Shadow
	@Final
	@Mutable
	private List<ResourceLocation> recipes;

	@Override
	public void frozenLib$setLoot(List<ResourceKey<LootTable>> loot) {
		this.loot = loot;
	}

	@Override
	public void frozenLib$setRecipes(List<ResourceLocation> recipes) {
		this.recipes = recipes;
	}
}
