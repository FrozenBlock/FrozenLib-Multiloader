package net.frozenblock.lib.registry.mixin;

import net.minecraft.core.MappedRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MappedRegistry.class)
public interface RegistryInvoker {

	@Accessor("frozen")
	boolean isFrozen();
}
