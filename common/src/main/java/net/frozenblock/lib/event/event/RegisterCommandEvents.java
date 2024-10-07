package net.frozenblock.lib.event.event;

import com.mojang.brigadier.CommandDispatcher;
import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.CommonEventEntrypoint;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RegisterCommandEvents {
	/**
	 * Runs when you are able to register new commands.
	 */
	public static FrozenEvent<RegisterCommand> REGISTER = FrozenEvent.createEvent(RegisterCommand.class);

	@FunctionalInterface
	public interface RegisterCommand extends CommonEventEntrypoint {
		void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext ctx, Commands.CommandSelection selection);
	}
}
