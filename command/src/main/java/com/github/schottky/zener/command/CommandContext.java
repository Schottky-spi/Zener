package com.github.schottky.zener.command;

import com.github.schottky.zener.command.resolver.CommandException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * represents the context under which a command was invoked / tab-completion was done.
 * This contains all relevant information that is provided using
 * {@link org.bukkit.command.CommandExecutor#onCommand(CommandSender, Command, String, String[]) onCommand}.
 * Additionally, this provides utility-methods to get a player or console from the context.
 */
public class CommandContext {

    public CommandContext(CommandSender sender, Command command, String label, String[] rawArgs) {
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.rawArgs = rawArgs;
    }

    private final CommandSender sender;

    public CommandSender getSender() {
        return sender;
    }

    public Player getPlayer() throws CommandException {
        if (sender instanceof Player)
            return (Player) sender;
        throw new CommandException("Command can only be executed by a player");
    }

    public ConsoleCommandSender getConsole() throws CommandException {
        if (sender instanceof ConsoleCommandSender)
            return (ConsoleCommandSender) sender;
        throw new CommandException("Command can only be executed by a console");
    }

    private final Command command;

    public Command getCommand() {
        return command;
    }

    private final String label;

    public String getLabel() {
        return label;
    }

    private final String[] rawArgs;

    public String[] getRawArgs() {
        return rawArgs;
    }
}
