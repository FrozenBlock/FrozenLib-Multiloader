package net.frozenblock.lib.resource.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.ReloadableServerResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ReloadableServerResources.class)
public class ReloadableServerResourcesMixin {

	// TODO: [Liuk] implement whatever you were trying to implement & fix neoforge crash
	/*
	@ModifyExpressionValue(
		method = "method_58296",
		at = @At(
			value = "NEW",
			target = "Lnet/minecraft/server/ReloadableServerResources;",
			ordinal = 0
		)
	)
	private static ReloadableServerResources frozenLib$addCustomListeners(ReloadableServerResources instance) {

		return instance;
	}
	 */
}
