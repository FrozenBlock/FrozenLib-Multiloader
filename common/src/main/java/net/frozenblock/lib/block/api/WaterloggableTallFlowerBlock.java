package net.frozenblock.lib.block.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A copy of {@link TallFlowerBlock} that allows for waterlogging.
 */
public class WaterloggableTallFlowerBlock extends TallFlowerBlock implements SimpleWaterloggedBlock {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public WaterloggableTallFlowerBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false));
	}

	@Override
	public @NotNull BlockState updateShape(
		@NotNull BlockState state,
		@NotNull Direction direction,
		@NotNull BlockState neighborState,
		@NotNull LevelAccessor world,
		@NotNull BlockPos pos,
		@NotNull BlockPos neighborPos
	) {
		if (state.getValue(WATERLOGGED)) {
			world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}

	@Nullable
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
		FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
		BlockState state = super.getStateForPlacement(ctx);
		if (state != null) {
			state = state.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
		}
		return state;
	}

	@Override
	public @NotNull FluidState getFluidState(@NotNull BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(WATERLOGGED);
	}
}
