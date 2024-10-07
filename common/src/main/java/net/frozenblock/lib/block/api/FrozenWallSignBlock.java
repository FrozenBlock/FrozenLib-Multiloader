package net.frozenblock.lib.block.api;

import java.util.Objects;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

/**
 * A copy of {@link WallSignBlock} that lets you define a custom loot table.
 */
public class FrozenWallSignBlock extends WallSignBlock {
	public final ResourceKey<LootTable> lootTable;

	public FrozenWallSignBlock(Properties settings, WoodType signType, ResourceKey<LootTable> lootTable) {
		super(signType, settings);
		this.lootTable = lootTable;
	}

	@Override
	public @NotNull ResourceKey<LootTable> getLootTable() {
		if (!Objects.equals(this.drops, this.lootTable)) {
			this.drops = this.lootTable;
		}

		assert this.drops != null;
		return this.drops;
	}
}
