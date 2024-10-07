package net.frozenblock.lib.advancement.mixin;

import java.util.List;
import net.frozenblock.lib.advancement.api.IAdvancement;
import net.minecraft.advancements.AdvancementRequirements;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AdvancementRequirements.class)
public class AdvancementRequirementsMixin implements IAdvancement.IARequirements {

	@Shadow
	@Final
	@Mutable
	private List<List<String>> requirements;

	@Override
	public void frozenLib$setRequirements(List<List<String>> requirements) {
		this.requirements = requirements;
	}
}
