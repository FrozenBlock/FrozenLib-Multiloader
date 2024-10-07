package net.frozenblock.lib.block.api.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.frozenblock.lib.env.api.EnvType;
import net.frozenblock.lib.env.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

/**
 * A {@link BlockEntityRenderer} that renders a given texture as a billboard, like a particle.
 */
@Environment(EnvType.CLIENT)
public abstract class BillboardBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
	private final ModelPart base;
	private final Quaternionf rotation = new Quaternionf(0F, 0F, 0F, 1F);

	public BillboardBlockEntityRenderer(Context ctx) {
		ModelPart root = this.getRoot(ctx);
		this.base = root.getChild("base");
	}

	@NotNull
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		modelPartData.addOrReplaceChild("base", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-8F, -16F, 0F, 16F, 16F, 0.0F),
			PartPose.offsetAndRotation(0F, 0F, 0F, Mth.PI, 0F, 0F)
		);
		return LayerDefinition.create(modelData, 16, 16);
	}

	@ApiStatus.Internal
	@Override
	public void render(@NotNull T entity, float tickDelta, @NotNull PoseStack poseStack, @NotNull MultiBufferSource vertexConsumers, int light, int overlay) {
		this.rotation.set(0F, 0F, 0F, 1F);
		this.rotation.mul(Axis.YP.rotationDegrees(-Minecraft.getInstance().gameRenderer.getMainCamera().yRot));
		poseStack.translate(0.5F, 0F, 0.5F);
		poseStack.pushPose();
		poseStack.mulPose(this.rotation);
		this.base.render(poseStack, vertexConsumers.getBuffer(RenderType.entityCutout(this.getTexture(entity))), light, overlay);
		poseStack.popPose();
	}

	public abstract ResourceLocation getTexture(T entity);

	public abstract ModelPart getRoot(Context ctx);
}
