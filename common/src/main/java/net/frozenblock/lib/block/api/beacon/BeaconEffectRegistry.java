package net.frozenblock.lib.block.api.beacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import net.frozenblock.lib.FrozenLibLogUtils;
import net.frozenblock.lib.block.mixin.beacon.BeaconValidEffectsAccessor;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;

/**
 * A helper class for adding a new {@link MobEffect} to the Beacon.
 */
@UtilityClass
public class BeaconEffectRegistry {

	/**
	 * Adds a {@link MobEffect} to the Beacon.
	 *
	 * @param effect The {@link MobEffect} to add.
	 * @param tier   The tier to add the {@link MobEffect} to. Tier 4 contains Regeneration and can be used simultaneously with tier 1-3 effects.
	 */
	public static void register(Holder<MobEffect> effect, int tier) {
		if (tier <= 0 || tier >= 4) {
			throw new IllegalArgumentException("Attempted to register Beacon effect " + effect.unwrapKey().get().location() + " at tier " + tier + ". Tier must be between 1 and 4.");
		}

		if (BeaconBlockEntity.BEACON_EFFECTS.get(tier - 1) instanceof ArrayList<Holder<MobEffect>> arrayList) {
			arrayList.add(effect);
			BeaconValidEffectsAccessor.frozenLib$setValidEffects(
				BeaconBlockEntity.BEACON_EFFECTS.stream()
					.flatMap(Collection::stream)
					.collect(Collectors.toSet())
			);
		} else {
			FrozenLibLogUtils.LOGGER.error("Attempted to register Beacon effect " + effect.unwrapKey().get().location() + " at tier " + tier + ". Tier list is not an instance of ArrayList!");
		}
	}
}
