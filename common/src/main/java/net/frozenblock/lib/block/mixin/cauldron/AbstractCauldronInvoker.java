package net.frozenblock.lib.block.mixin.cauldron;

import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractCauldronBlock.class)
public interface AbstractCauldronInvoker {

	@Invoker("canReceiveStalactiteDrip")
	boolean frozenLib$canReceiveStalactiteDrip(Fluid fluid);

}
