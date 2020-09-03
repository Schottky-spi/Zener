package com.github.schottky.zener.command.resolver;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.argument.*;
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
        registerArgument(Integer.class, Arguments.IntArgument::new);
        registerArgument(int.class, Arguments.IntArgument::new);
        registerArgument(Double.class, Arguments.DoubleArgument::new);
        registerArgument(double.class, Arguments.DoubleArgument::new);
        registerArgument(String.class, Arguments.StringArgument::new);
        registerArgument(ItemStack.class, Arguments.ItemStackArgument::new);
        registerArgument(boolean.class, Arguments.BooleanArgument::new);
        registerArgument(Boolean.class, Arguments.BooleanArgument::new);
        registerArgument(Player.class, Arguments.PlayerArg::new);
        registerArgument(OfflinePlayer.class, Arguments.OfflinePlayerArg::new);
        registerArgument(Material.class, Arguments.MaterialArgument::new);
        registerArgument(CommandContext.class, Arguments.ContextArgument::new);
    }

    public static void registerArgument(Class<?> clazz, ArgumentFactory factory) {
        argumentFactories.put(clazz, factory);
    }

    public static Optional<ArgumentFactory> get(Class<?> clazz) {
        return Optional.ofNullable(argumentFactories.get(clazz));
    }

    @FunctionalInterface
    public interface ArgumentFactory {

        @NotNull Argument<?> create(@Nullable CommandContext context);

    }

    private final Queue<String> args;
    private final CommandContext context;

    public ArgumentResolver(String @NotNull [] args, @NotNull CommandContext context) {
        this.args = new LinkedList<>(Arrays.asList(args));
        this.context = context;
    }

    public @Nullable Object[] resolve(Parameter @NotNull [] parameterTypes) throws CommandException {
        final HighLevelArg<Object[]> root = rootArgument(parameterTypes, context);
        if (root.resolve(args)) {
            if (!args.isEmpty())
                throw ArgumentNotResolvable.withMessage("Too many args");
            return root.value();
        } else {
            throw ArgumentNotResolvable.withMessage("Too few args!");
        }
    }

    private static Object getUnresolvedFromContext(Class<?> clazz, CommandContext context) throws CommandException {
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
        } else if (clazz.isAssignableFrom(String[].class)) {
            return context.getRawArgs();
        }
        throw new UnsupportedOperationException("Type " + clazz + " cannot be injected unresolved");
    }

    public static HighLevelArg<Object[]> rootArgument(Parameter[] parameterTypes, CommandContext context) {
        final List<Argument<?>> arguments = new ArrayList<>();
        for (Parameter parameter : parameterTypes) {
            if (parameter.isAnnotationPresent(Unresolved.class)) {
                arguments.add(new UnresolvedArg<>(getUnresolvedFromContext(parameter.type, context)));
            } else {
                Argument<?> arg = get(parameter.type)
                        .orElseThrow(() -> new RuntimeException("No argument registered for type " + parameter.type))
                        .create(context);
                if (parameter.isAnnotationPresent(Describe.class)) {
                    arg = ArgumentBuilder.of(parameter.type)
                            .description(parameter.annotationValue(Describe.class, Describe::value))
                            .create(context);
                }
                arguments.add(arg);
            }
        }
        return new SuperArgument(context, arguments);
    }

    public static Optional<String> description(Parameter[] parameterTypes, CommandContext context) {
        return rootArgument(parameterTypes, context).description();
    }

    private static class SuperArgument extends AbstractHighLevelArg<Object[]> {

        public SuperArgument(CommandContext context, List<Argument<?>> allArgs) {
            super(context, allArgs.toArray(new Argument<?>[0]));
        }

        @Override
        public Object[] value() {
            final Object[] values = new Object[contents.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = contents[i].value();
            }
            return values;
        }
    }

    private static class UnresolvedArg<T> implements UnresolvedArgument<T> {

        private final T value;

        public UnresolvedArg(T value) {
            this.value = value;
        }

        @Override
        public T value() { return value; }
    }

}
