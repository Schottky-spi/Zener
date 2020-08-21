package com.github.schottky.zener.command.resolver;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.argument.*;
import com.github.schottky.zener.messaging.Console;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ArgumentResolver {

    private static final Map<Class<?>,ArgumentFactory> argumentFactories = new HashMap<>();

    static {
        argumentFactories.put(Integer.class, Arguments.IntArgument::new);
        argumentFactories.put(int.class, Arguments.IntArgument::new);
        argumentFactories.put(Double.class, Arguments.DoubleArgument::new);
        argumentFactories.put(double.class, Arguments.DoubleArgument::new);
        argumentFactories.put(String.class, Arguments.StringArgument::new);
        argumentFactories.put(ItemStack.class, Arguments.ItemStackArgument::new);
        argumentFactories.put(boolean.class, Arguments.BooleanArgument::new);
        argumentFactories.put(Boolean.class, Arguments.BooleanArgument::new);
        argumentFactories.put(Player.class, Arguments.PlayerArg::new);
        argumentFactories.put(OfflinePlayer.class, Arguments.OfflinePlayerArg::new);
        argumentFactories.put(Material.class, Arguments.MaterialArgument::new);
    }

    public static void registerArgument(Class<?> clazz, ArgumentFactory factory) {
        argumentFactories.put(clazz, factory);
    }

    @FunctionalInterface
    public interface ArgumentFactory {

        @NotNull Argument<?> create(@Nullable CommandContext context);

    }

    private final String[] args;
    private final CommandContext context;
    private int cursor = 0;

    public ArgumentResolver(String @NotNull [] args, @NotNull CommandContext context) {
        this.args = args;
        this.context = context;
    }

    public @Nullable Object[] resolve(Parameter @NotNull [] parameterTypes) throws CommandException {
        final Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            final Parameter parameterType = parameterTypes[i];
            if (parameterType.isAnnotationPresent(Unresolved.class)) {
                parameters[i] = getUnresolvedFromContext(parameterType.type);
            } else {
                final ArgumentFactory factory = argumentFactories.get(parameterType.type);
                if (factory == null) {
                    Console.severe("No argument registered fot type %s", parameterType.type);
                    return null;
                }
                final Argument<?> argument = factory.create(context);
                resolve(argument);
                parameters[i] = argument.value();
            }
        }
        return parameters;
    }

    private Object getUnresolvedFromContext(Class<?> clazz) throws CommandException {
        if (clazz.isAssignableFrom(Player.class)) {
            return context.getPlayer();
        } else if (clazz.isAssignableFrom(ConsoleCommandSender.class)) {
            return context.getConsole();
        } else if (clazz.isAssignableFrom(CommandSender.class)) {
            return context.getSender();
        } else if (clazz.isAssignableFrom(String.class)) {
            return context.getLabel();
        } else if (clazz.isAssignableFrom(Command.class)) {
            return context.getCommand();
        }
        throw new UnsupportedOperationException("Type " + clazz + " cannot be injected unresolved");
    }

    public void resolve(@NotNull Argument<?> argument) throws CommandException {
        Argument<?> arg = resolveAndReturnLast(argument);
        if (arg != null) throw ArgumentNotResolvable.withMessage("Too few args!");
    }

    public @Nullable Argument<?> resolveAndReturnLast(@NotNull Argument<?> argument) throws CommandException {
        if (argument instanceof VarArgsArgument<?>) {
            VarArgsArgument<?> arg = (VarArgsArgument<?>) argument;
            if (args.length - cursor < arg.minArgs()) throw ArgumentNotResolvable.withMessage("Too few args");
            if (args.length - cursor > arg.maxArgs()) throw ArgumentNotResolvable.withMessage("Too many args");
            final Argument<?>[] content = arg.contents();
            final int rest = args.length - cursor;
            for (int i = 0; i < rest; i++) {
                resolve(content[i]);
            }
        } else if (argument instanceof HighLevelArg) {
            HighLevelArg<?> arg = (HighLevelArg<?>) argument;
            final Argument<?>[] contents = arg.contents();
            for (Argument<?> content: contents) {
                resolve(content);
            }
        } else if (argument instanceof LowLevelArg) {
            LowLevelArg<?> arg = (LowLevelArg<?>) argument;
            if (args.length == cursor) return arg;
            arg.resolve(args[cursor]);
            cursor++;
        } else if (argument instanceof ContextualArgument<?>) {
            ContextualArgument<?> arg = (ContextualArgument<?>) argument;
            arg.resolve();
        }
        return null;
    }

    public HighLevelArg<?> computeRoot(Parameter[] parameterTypes) {
        final List<Argument<?>> arguments = new ArrayList<>();
        for (Parameter parameter : parameterTypes) {
            getResolvedOnly(parameter, context)
                    .ifPresent(arguments::add);
        }
        return new SuperArgument(context, arguments);
    }

    private static Optional<Argument<?>> getResolvedOnly(Parameter parameterType, CommandContext context) {
        if (parameterType.isAnnotationPresent(Unresolved.class))
            return Optional.empty();
        else
            return Optional.ofNullable(argumentFactories.get(parameterType.type))
                .map(factory -> factory.create(context));
    }

    public static List<String> getCommandArguments(Parameter[] parameterTypes, CommandContext context) {
        final List<String> arguments = new ArrayList<>();
        for (Parameter parameterType: parameterTypes) {
            getResolvedOnly(parameterType, context)
                    .filter(argument -> !(argument instanceof ContextualArgument<?>))
                    .map(arg -> {
                        try {
                            return parameterType.getAnnotation(Describe.class)
                                    .map(anno -> "<" + anno.value() + ">")
                                    .orElse(arg.description());
                        } catch (CommandException ignored) {
                            return null;
                        }
                    })
                    .ifPresent(arguments::add);
        }
        return arguments;
    }

    private static class SuperArgument extends AbstractHighLevelArg<Object> {

        public SuperArgument(CommandContext context, List<Argument<?>> allArgs) {
            super(context, allArgs.toArray(new Argument<?>[0]));
        }

        @Override
        public Object value() { return null; }
    }

}
