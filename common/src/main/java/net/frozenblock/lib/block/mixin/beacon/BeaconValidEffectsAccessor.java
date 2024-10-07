package net.frozenblock.lib.block.mixin.beacon;

import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BeaconBlockEntity.class)
public interface BeaconValidEffectsAccessor {
	@Accessor("VALID_EFFECTS")
	public static void frozenLib$setValidEffects(Set<Holder<MobEffect>> validEffects) {
		throw new AssertionError("Mixin injection failed - FrozenLib BeaconValidEffectsAccessor.");
	}
}
