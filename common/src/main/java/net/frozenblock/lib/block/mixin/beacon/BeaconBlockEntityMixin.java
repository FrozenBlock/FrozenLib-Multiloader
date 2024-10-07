package net.frozenblock.lib.block.mixin.beacon;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BeaconBlockEntity.class, priority = 10000)
public class BeaconBlockEntityMixin {

	@ModifyExpressionValue(
		method = "<clinit>",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/List;of(Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/List;",
			ordinal = 0
		)
	)
	private static List<Holder<MobEffect>> frozenLib$addTier1Effects(List<Holder<MobEffect>> list) {
		return new ArrayList<>(list);
	}

	@ModifyExpressionValue(
		method = "<clinit>",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/List;of(Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/List;",
			ordinal = 1
		)
	)
	private static List<Holder<MobEffect>> frozenLib$addTier2Effects(List<Holder<MobEffect>> list) {
		return new ArrayList<>(list);
	}

	@ModifyExpressionValue(
		method = "<clinit>",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/List;of(Ljava/lang/Object;)Ljava/util/List;",
			ordinal = 0
		)
	)
	private static List<Holder<MobEffect>> frozenLib$addTier3Effects(List<Holder<MobEffect>> list) {
		return new ArrayList<>(list);
	}

	@ModifyExpressionValue(
		method = "<clinit>",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/List;of(Ljava/lang/Object;)Ljava/util/List;",
			ordinal = 1
		)
	)
	private static List<Holder<MobEffect>> frozenLib$addTier4Effects(List<Holder<MobEffect>> list) {
		return new ArrayList<>(list);
	}

}
