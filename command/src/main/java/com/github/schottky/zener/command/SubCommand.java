package com.github.schottky.zener.command;

import com.google.common.base.Joiner;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.apiguardian.api.API;

import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

@API(status = API.Status.EXPERIMENTAL)
public abstract class SubCommand<T extends CommandBase> extends CommandBase {

    protected final T parentCommand;

    public SubCommand(T parentCommand) {
        this.parentCommand = parentCommand;
        scanForSubCommands();
    }

    @Override
    void initialize() { }

    /**
     * returns the amount of parent-commands this class has.
     * This is implemented recursively by simply adding one to the
     * parent's depth-method.
     * The Base-class will return 0
     * @return The depth of it's parent plus one
     */

    @Override
    public int computeDepth() {
        return 1 + this.parentCommand.computeDepth();
    }

    /**
     * returns the root-command of this sub-command hierarchy.
     * @return The root-command
     */
    @Override
    public CommandBase root() {
        return parentCommand.root();
    }

    /**
     * returns the path that this sub-command describes, in other words
     * all commands that lead up to this command.
     * The returned lost may be modifiable or unmodifiable, but is
     * not backed by the command-structure.
     * @return The path
     */

    @Override
    public List<CommandBase> path() {
        final LinkedList<CommandBase> bases = new LinkedList<>();
        CommandBase current = this;
        while (current instanceof SubCommand) {
            bases.addFirst(current);
            current = ((SubCommand<?>) current).parentCommand;
        }
        bases.addFirst(current);
        return bases;
    }

    @Override
    public ComponentBuilder createDescription(String rootLabel, CommandContext context) {
        final ComponentBuilder builder = createCommandSyntax(rootLabel, context);

        final String simpleDescription = shortDescription();
        if (simpleDescription != null && !simpleDescription.isEmpty())
            builder.append(" - ").color(ChatColor.WHITE).append(simpleDescription).color(ChatColor.YELLOW);

        return builder;
    }

    public ComponentBuilder createCommandSyntax(String rootLabel, CommandContext context) {
        final ComponentBuilder builder = new ComponentBuilder()
                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                        "/" + rootLabel + " " + Joiner.on(' ').join(context.getRawArgs()) + " " + name()))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new BaseComponent[] { new TextComponent(name()) }))
                .append("/").color(ChatColor.GRAY).append(rootLabel).color(ChatColor.AQUA);

        for (String label : context.getRawArgs()) {
            builder.append(" ").append(label).color(ChatColor.AQUA);
        }

        return builder.append(" ").append(name()).color(ChatColor.AQUA);
    }

    @Override
    public String toString() {
        final StringJoiner joiner = new StringJoiner(" ", "/", "");
        for (CommandBase base: path())
            joiner.add(base.name());
        return joiner.toString();
    }
}
