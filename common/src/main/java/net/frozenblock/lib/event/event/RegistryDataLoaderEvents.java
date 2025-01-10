package net.frozenblock.lib.event.event;

import lombok.experimental.UtilityClass;
import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.CommonEventEntrypoint;
import net.minecraft.resources.ResourceKey;

@UtilityClass
public class RegistryDataLoaderEvents {
	/**
	 * An event ran as a data-driven entry is being registered.
	 * <p>
	 * Called in {@link net.minecraft.resources.RegistryDataLoader#loadContentsFromNetwork loadContentsFromNetwork}
	 * and {@link net.minecraft.resources.RegistryDataLoader#loadElementFromResource loadElementFromResource}.
	 */
	public static final FrozenEvent<RegisterResource> REGISTER_RESOURCE = FrozenEvent.createEvent(RegisterResource.class);

	@FunctionalInterface
	public interface RegisterResource extends CommonEventEntrypoint {
		/**
		 * @param object The object about to be registered.
		 * @param resourceKey The {@link ResourceKey} the object will be registered to.
		 * @param loadedFromNetwork Whether the data was loaded from the network, or a local file.
		 */
		void onRegisterResource(Object object, ResourceKey<?> resourceKey, boolean loadedFromNetwork);
	}
}
