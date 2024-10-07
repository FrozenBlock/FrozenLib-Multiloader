package net.frozenblock.lib.block.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link MultifaceClusterBlock}, but only one face is permitted.
 */
public abstract class FaceClusterBlock extends MultifaceClusterBlock {
	public FaceClusterBlock(int height, int xzOffset, Properties properties) {
		super(height, xzOffset, properties);
	}

	@Override
	public boolean isValidStateForPlacement(@NotNull BlockGetter level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull Direction direction) {
		if (this.isFaceSupported(direction) && !state.is(this)) {
			BlockPos blockPos = pos.relative(direction);
			return canAttachTo(level, direction, blockPos, level.getBlockState(blockPos));
		} else {
			return false;
		}
	}
}
