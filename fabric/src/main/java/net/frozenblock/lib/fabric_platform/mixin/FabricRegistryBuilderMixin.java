package net.frozenblock.lib.fabric_platform.mixin;

import java.util.EnumSet;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.event.registry.RegistryAttributeHolder;
import net.frozenblock.lib.fabric_platform.api.IBuild;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FabricRegistryBuilder.class)
public class FabricRegistryBuilderMixin<T, R extends WritableRegistry<T>> implements IBuild<R> {

	@Shadow
	@Final
	private R registry;

	@Shadow
	@Final
	private EnumSet<RegistryAttribute> attributes;

	@Override
	public R build() {
		final ResourceKey<?> key = registry.key();

		for (RegistryAttribute attribute : attributes) {
			RegistryAttributeHolder.get(key).addAttribute(attribute);
		}
		return registry;
	}
}
