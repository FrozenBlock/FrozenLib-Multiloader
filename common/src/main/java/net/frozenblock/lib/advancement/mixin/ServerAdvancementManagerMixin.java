package net.frozenblock.lib.advancement.mixin;

import com.google.gson.JsonElement;
import java.util.Map;
import net.frozenblock.lib.advancement.api.AdvancementHolderRegisteredEvent;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerAdvancementManager.class)
public class ServerAdvancementManagerMixin {
	@Shadow
	private Map<ResourceLocation, AdvancementHolder> advancements;

	@Shadow
	@Final
	private HolderLookup.Provider registries;

	@Inject(
		method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Map;values()Ljava/util/Collection;"
		)
	)
	private void frozenLib$modifyAdvancement(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo info) {
		for (AdvancementHolder holder : advancements.values()) {
			AdvancementHolderRegisteredEvent.INIT.invoke(e -> e.onInit(holder, this.registries));
		}
	}
}
