package net.frozenblock.lib.event.api;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.frozenblock.lib.FrozenLibLogUtils;
import net.frozenblock.lib.event.impl.EventType;
import net.frozenblock.lib.platform.api.EntryPointHelper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class FrozenEvent<T> {

	private final ArrayList<T> callbacks = new ArrayList<>();

	private FrozenEvent() {}

	public final void invoke(Consumer<T> eventConsumer) {
		for (var callback : callbacks) {
			eventConsumer.accept(callback);
		}
	}

	public final void register(T callback) {
		callbacks.add(callback);
	}

	/**
	 * A list to store all the registered events
	 */
	private static final List<Class<?>> REGISTERED_EVENTS = new ArrayList<>();

	/**
	 * Creates the specified event.
	 * <p>
	 * Creating an event this way ensures all event entrypoints implementing the event's callbacks will be registered as well.
	 *
	 * @param type The type of the event to be registered
	 */
	public static <T> @NotNull FrozenEvent<T> createEvent(Class<? super T> type) {
		if (REGISTERED_EVENTS.contains(type)) FrozenLibLogUtils.logError("Event type " + type.getName() + " already registered! Please create a new interface for new events!");
		REGISTERED_EVENTS.add(type);
		FrozenEvent<T> event = new FrozenEvent<>();
		// Loop through all event types
		for (var eventType : EventType.VALUES) {
			// Check if the listener type is assignable from the event type
			if (eventType.entrypoint().isAssignableFrom(type)) {
				// Get the entrypoints for the specified listener type
				List<?> entrypoints = EntryPointHelper.getEntrypoints(eventType.entrypointIdentifier(), eventType.entrypoint());

				// Loop through the entrypoints
				for (Object entrypoint : entrypoints) {
					// Check if the entrypoint is assignable from the event type
					if (type.isAssignableFrom(entrypoint.getClass())) {
						// Register the entrypoint to the event
						event.register((T) entrypoint);
					}
				}
				// Break the loop once a match is found
				break;
			}
		}
		return event;
	}
}
