package net.frozenblock.lib.block.entity.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * A basic block entity which already includes methods for saving/loading data.<br><br>
 * in {@link #saveDataIntoWorld(CompoundTag)} you should PUT data from your local variables into the CompoundTag <br><br>
 * in {@link #getDataFromWorld(CompoundTag)} you should GET data from the CompoundTag
 * and assign it to your local variables
 */
public abstract class SmartBlockEntity extends BlockEntity {
	public SmartBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	public abstract void saveDataIntoWorld(CompoundTag tag);

	public abstract void getDataFromWorld(CompoundTag tag);

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
		super.saveAdditional(tag, registries);
		this.saveDataIntoWorld(tag);
	}

	@Override
	public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
		CompoundTag tag = super.getUpdateTag(registries);
		this.saveDataIntoWorld(tag);
		return tag;
	}

	@Override
	protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
		super.loadAdditional(tag, registries);
		this.getDataFromWorld(tag);
	}

	/**
	 * Implemented in Neo
	 */
	@SuppressWarnings("unused")
	public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
		this.getDataFromWorld(tag);
	}

}
