package com.github.schottky.zener.command;

import com.github.schottky.zener.command.resolver.ArgumentResolver;
import com.github.schottky.zener.command.resolver.CommandException;
import com.github.schottky.zener.command.resolver.SuccessMessage;
import com.github.schottky.zener.command.resolver.argument.Argument;
import com.github.schottky.zener.command.resolver.argument.HighLevelArg;
import com.github.schottky.zener.command.resolver.argument.LowLevelArg;
import com.github.schottky.zener.messaging.Console;
import com.google.common.base.Preconditions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apiguardian.api.API;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@API(status = API.Status.INTERNAL)
public final class MethodBasedSubCommand<T extends CommandBase> extends SubCommand<T> {

    private final Method method;
    private String successMessage;

    public MethodBasedSubCommand(Method method, T parent) {
        super(parent);
        this.method = method;

        final SubCmd subCmd = method.getAnnotation(SubCmd.class);
        Preconditions.checkArgument(subCmd != null, "Illegal method in MethodBasedSubCommand");

        final String name = subCmd.value();
        final String permission = subCmd.permission().isEmpty() ?
                parent.permission.getName() + "." + name :
                subCmd.permission();
        this.name = name;
        this.permission = new Permission(permission, subCmd.permDefault());
        this.aliases.addAll(Arrays.asList(subCmd.aliases()));
        this.simpleDescription = subCmd.desc();
        this.maxArgsLength = Integer.MAX_VALUE;
        this.parameterTypes = method.getParameterTypes();
        this.parameterAnnotations = method.getParameterAnnotations();

        if (method.isAnnotationPresent(SuccessMessage.class))
            successMessage = method.getAnnotation(SuccessMessage.class).value();
    }

    private final Class<?>[] parameterTypes;
    private final Annotation[][] parameterAnnotations;

    @Override
    public boolean onAcceptedCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args)
    {
        final CommandContext context = new CommandContext(sender, command, label, args);
        Object[] resolved;
        try {
            resolved = new ArgumentResolver(args).resolve(parameterTypes, parameterAnnotations, context);
            if (resolved == null)
                return true;
        } catch (CommandException exception) {
            sender.sendMessage(exception.getMessage());
            return true;
        }

        try {
            method.invoke(parentCommand, resolved);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof CommandException) {
                sender.sendMessage(e.getCause().getMessage());
            } else {
                Console.error(e);
            }
            return true;
        } catch (IllegalAccessException e) {
            Console.error(e);
            return true;
        }

        if (successMessage != null)
            context.getSender().sendMessage(successMessage);
        return true;
    }

    @Override
    public ComponentBuilder createCommandSyntax(String rootLabel, String[] labels) {
        final ComponentBuilder builder = super.createCommandSyntax(rootLabel, labels);
        if (subCommands.isEmpty()) {
            for (Argument<?> argument : ArgumentResolver.getActualArgs(parameterTypes, parameterAnnotations)) {
                if (argument.description() != null)
                    builder.append(" " + argument.description()).color(ChatColor.AQUA);
            }
        } else {
            builder.append(" " + name());
        }
        return builder.color(ChatColor.AQUA);
    }

    @Override
    public @NotNull List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args)
    {
        final CommandContext context = new CommandContext(sender, command, label, args);
        final HighLevelArg<?> superArg = new ArgumentResolver(args).computeRoot(parameterTypes, parameterAnnotations);
        final LowLevelArg<?> arg = superArg.findLastArgument(new LinkedList<>(Arrays.asList(args)), context);
        if (arg == null)
            return Collections.emptyList();
        else {
            try {
                return arg.optionsAsString(context)
                        .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                        .collect(Collectors.toList());
            } catch (CommandException e) {
                return Collections.emptyList();
            }
        }
    }
}
