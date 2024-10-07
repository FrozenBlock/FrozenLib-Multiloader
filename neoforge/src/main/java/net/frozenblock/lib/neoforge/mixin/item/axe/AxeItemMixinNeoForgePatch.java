package net.frozenblock.lib.neoforge.mixin.item.axe;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.Optional;
import net.frozenblock.lib.item.api.axe.AxeApi;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AxeItem.class)
public class AxeItemMixinNeoForgePatch {

	@WrapOperation(
		method = "useOn",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/AxeItem;evaluateNewBlockState(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/item/context/UseOnContext;)Ljava/util/Optional;"
		)
	)
	public Optional<BlockState> frozenlib$neoForge$runAxeBehavior(
		AxeItem instance,
		Level level,
		BlockPos pos,
		Player player,
		BlockState state,
		UseOnContext context,
		Operation<Optional<BlockState>> original
	) {
		BlockState blockState = level.getBlockState(pos);
		Direction direction = context.getClickedFace();
		AxeApi.AxeBehavior axeBehavior = AxeApi.get(blockState.getBlock());
		if (axeBehavior != null && axeBehavior.meetsRequirements(level, pos, direction, state)) {
			BlockState outputState = axeBehavior.getOutputBlockState(state);
			if (outputState != null) {
				axeBehavior.onSuccess(level, pos, direction, outputState, state);
				return Optional.of(outputState);
			}
		}

		return original.call(instance, level, pos, player, state, context);
	}
}
