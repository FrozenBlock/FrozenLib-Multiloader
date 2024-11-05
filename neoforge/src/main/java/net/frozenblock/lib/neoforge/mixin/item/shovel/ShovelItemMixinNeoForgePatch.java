package net.frozenblock.lib.neoforge.mixin.item.shovel;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShovelItem.class)
public class ShovelItemMixinNeoForgePatch {

	@WrapOperation(
		method = "useOn",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;getToolModifiedState(Lnet/minecraft/world/item/context/UseOnContext;Lnet/neoforged/neoforge/common/ItemAbility;Z)Lnet/minecraft/world/level/block/state/BlockState;"
		)
	)
	public BlockState frozenlib$neoForge$removeOtherBehaviorsB(
		BlockState instance,
		UseOnContext useOnContext,
		ItemAbility itemAbility,
		boolean b,
		Operation<BlockState> original,
		@Share("frozenLib$isCustomBehavior") LocalBooleanRef isCustomBehavior
	) {
		if (isCustomBehavior.get()) {
			return Blocks.AIR.defaultBlockState();
		}
		return original.call(instance, useOnContext, itemAbility, b);
	}
}
