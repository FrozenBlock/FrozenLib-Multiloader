package net.frozenblock.lib.event.event;

import lombok.experimental.UtilityClass;
import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.CommonEventEntrypoint;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class RegistryFreezeEvents {
	/**
	 * An event indicating the start of a {@link Registry}'s freeze.
	 * <p>
	 * The registry will not be frozen when this is invoked.
	 */
	public static final FrozenEvent<StartRegistryFreeze> START_REGISTRY_FREEZE = FrozenEvent.createEvent(StartRegistryFreeze.class);

	/**
	 * An event indicating the end of a {@link Registry}'s freeze.
	 * <p>
	 * The registry will be frozen when this is invoked.
	 */
	public static final FrozenEvent<EndRegistryFreeze> END_REGISTRY_FREEZE = FrozenEvent.createEvent(EndRegistryFreeze.class);

	@FunctionalInterface
	public interface StartRegistryFreeze extends CommonEventEntrypoint {
		/**
		 * @param allRegistries This indicates whether the Registry is being frozen from {@link BuiltInRegistries#freeze()} or not.
		 */
		void onStartRegistryFreeze(@Nullable Registry<?> registry, boolean allRegistries);
	}

	@FunctionalInterface
	public interface EndRegistryFreeze extends CommonEventEntrypoint {
		/**
		 * @param allRegistries This indicates whether the Registry is being frozen from {@link BuiltInRegistries#freeze()} or not.
		 */
		void onEndRegistryFreeze(@Nullable Registry<?> registry, boolean allRegistries);
	}
}
