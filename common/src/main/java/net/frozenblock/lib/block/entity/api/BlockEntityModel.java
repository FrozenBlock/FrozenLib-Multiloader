package net.frozenblock.lib.block.entity.api;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Function;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

/**
 * this is liuks fault
 *
 * @param <T> garbage rn
 */
public abstract class BlockEntityModel<T extends BlockEntity> extends Model {

	public BlockEntityModel(Function<ResourceLocation, RenderType> renderType) {
		super(renderType);
	}

	public abstract void setupAnim(T entity, float ageInTicks);

	public abstract void render(T entity, float ageInTicks, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color, float alpha);

	@Override
	public final void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
	}
}
