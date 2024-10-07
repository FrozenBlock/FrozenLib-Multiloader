package net.frozenblock.lib.block.api.tag;

import lombok.experimental.UtilityClass;
import net.frozenblock.lib.FrozenLibConstants;
import net.frozenblock.lib.block.api.tick.BlockScheduledTicks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * Contains custom block tags used within FrozenLib.
 */
@UtilityClass
public class FrozenLibBlockTags {
	/**
	 * A tag used for blocks that Dripstone can drip onto.
	 * <p>
	 * Use {@link BlockScheduledTicks#addToBlock(Block, BlockScheduledTicks.InjectedScheduledTick)} to add this in tandem with {@link net.frozenblock.lib.block.api.dripstone.DripstoneDripApi#getDripstoneFluid(ServerLevel, BlockPos)}.
	 */
	public static final TagKey<Block> DRIPSTONE_CAN_DRIP_ON = bind("dripstone_can_drip");

	@NotNull
	private static TagKey<Block> bind(String path) {
		return TagKey.create(Registries.BLOCK, FrozenLibConstants.id(path));
	}
}
