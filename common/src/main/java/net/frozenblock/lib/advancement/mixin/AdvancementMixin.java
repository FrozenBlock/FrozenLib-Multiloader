package net.frozenblock.lib.advancement.mixin;

import java.util.Map;
import net.frozenblock.lib.advancement.api.IAdvancement;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Debug(export = true)
@Mixin(Advancement.class)
public class AdvancementMixin implements IAdvancement {

	@Shadow
	@Final
	@Mutable
	private AdvancementRewards rewards;
	@Shadow
	@Final
	@Mutable
	private AdvancementRequirements requirements;
	@Shadow
	@Final
	@Mutable
	private Map<String, Criterion<?>> criteria;

	@Unique
	@Override
	public void frozenLib$setRewards(AdvancementRewards rewards) {
		this.rewards = rewards;
	}

	@Unique
	@Override
	public void frozenLib$setRequirements(AdvancementRequirements requirements) {
		this.requirements = requirements;
	}

	@Unique
	@Override
	public void frozenLib$setCriteria(Map<String, Criterion<?>> criteria) {
		this.criteria = criteria;
	}
}
