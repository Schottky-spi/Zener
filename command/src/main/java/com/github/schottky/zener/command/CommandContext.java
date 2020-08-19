package com.github.schottky.zener.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandContext {

    public CommandContext(CommandSender sender, Command command, String label) {
        this.sender = sender;
        this.command = command;
        this.label = label;
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
}
