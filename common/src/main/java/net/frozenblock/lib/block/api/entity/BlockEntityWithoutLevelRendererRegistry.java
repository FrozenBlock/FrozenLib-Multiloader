package net.frozenblock.lib.block.api.entity;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Lets you add blocks that render as a {@link BlockEntity} in item form.
 */
@UtilityClass
public class BlockEntityWithoutLevelRendererRegistry {
	@ApiStatus.Internal
	private static final Object2ObjectLinkedOpenHashMap<Block, BlockEntity> BLOCK_TO_BLOCK_ENTITY_MAP = new Object2ObjectLinkedOpenHashMap<>();

	/**
	 * Registers a {@link Block} to render as a {@link BlockEntity} while in item form.
	 *
	 * @param block           The block to render as a {@link BlockEntity} while in item form.
	 * @param blockEntityType The {@link BlockEntityType} to render.
	 */
	public static void register(Block block, @NotNull BlockEntityType<?> blockEntityType) {
		BLOCK_TO_BLOCK_ENTITY_MAP.put(block, blockEntityType.create(BlockPos.ZERO, block.defaultBlockState()));
	}

	/**
	 * @param block The block to check.
	 * @return the {@link BlockEntity} the block will render as, if available.
	 */
	@ApiStatus.Internal
	public static Optional<BlockEntity> getBlockEntity(Block block) {
		return Optional.ofNullable(BLOCK_TO_BLOCK_ENTITY_MAP.get(block));
	}

	/**
	 * @param block The block to check.
	 * @return whether the block will render as a {@link BlockEntity} while in item form.
	 */
	@ApiStatus.Internal
	public static boolean hasBlock(Block block) {
		return BLOCK_TO_BLOCK_ENTITY_MAP.containsKey(block);
	}

}
