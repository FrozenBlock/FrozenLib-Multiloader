package net.frozenblock.lib.event.event;

import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.CommonEventEntrypoint;

/**
 * A class made to help with simple registration of packets on NeoForge for those more used to Fabric's system.
 */
public class FrozenLibPacketRegistryEvents {
	/**
	 * Calls when packets can be safely registered through FrozenLib on NeoForge.
	 * <p>
	 * Registering packets through FrozenLib after this event is called will result in them not being registered at all on NeoForge.
	 */
	public static final FrozenEvent<Register> REGISTER = FrozenEvent.createEvent(Register.class);

	@FunctionalInterface
	public interface Register extends CommonEventEntrypoint {
		void registerPackets();
	}

	private FrozenLibPacketRegistryEvents() {
	}
}
