package net.frozenblock.lib.resource.mixin;

import net.minecraft.server.ReloadableServerResources;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ReloadableServerResources.class)
public class ReloadableServerResourcesMixin {

	// TODO: [Liuk] crashes when making a new world
	/*
	@ModifyExpressionValue(
		method = {"lambda$loadResources$2", "method_58296"},
		require = 1,
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
