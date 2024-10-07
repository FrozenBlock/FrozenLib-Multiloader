package net.frozenblock.lib.advancement.api;

import lombok.experimental.UtilityClass;
import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.CommonEventEntrypoint;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;

@UtilityClass
public class AdvancementHolderRegisteredEvent {

	/**
	 * Invoked for each AdvancementHolder registered on the server.
	 * <p>
	 * Enables you to modify advancements.
	 */
	public static final FrozenEvent<AdvancementInit> INIT = FrozenEvent.createEvent(AdvancementInit.class);

	@FunctionalInterface
	public interface AdvancementInit extends CommonEventEntrypoint {
		void onInit(AdvancementHolder holder, HolderLookup.Provider registries);
	}
}
