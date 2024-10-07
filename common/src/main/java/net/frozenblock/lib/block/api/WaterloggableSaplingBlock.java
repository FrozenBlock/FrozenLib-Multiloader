package net.frozenblock.lib.block.api;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A copy of {@link SaplingBlock} that allows for waterlogging.
 * <p>
 * Keep in mind this also lets the block be placed on Clay, Mud, and Sand. If you don't like this functionality, override {@link #mayPlaceOn(BlockState, BlockGetter, BlockPos)} in an extending class.
 */
public class WaterloggableSaplingBlock extends SaplingBlock implements SimpleWaterloggedBlock {
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public WaterloggableSaplingBlock(TreeGrower generator, Properties settings) {
		super(generator, settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0).setValue(WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(WATERLOGGED);
	}

	@Override
	protected boolean mayPlaceOn(@NotNull BlockState floor, @NotNull BlockGetter world, @NotNull BlockPos pos) {
		return super.mayPlaceOn(floor, world, pos) || floor.is(Blocks.CLAY) || floor.is(Blocks.MUD) || floor.is(Blocks.SAND);
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
		FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
		return Objects.requireNonNull(super.getStateForPlacement(ctx)).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
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

		return direction == Direction.UP && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState()
			: super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	public @NotNull FluidState getFluidState(@NotNull BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}
}
