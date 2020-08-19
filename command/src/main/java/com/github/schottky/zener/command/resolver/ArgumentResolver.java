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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public interface ArgumentFactory {
        Argument<?> create();
    }

    private final String[] args;
    private int cursor = 0;

    public ArgumentResolver(String[] args) {
        this.args = args;
    }

    public Object[] resolve(Class<?>[] parameterTypes, Annotation[][] parameterAnnotations, CommandContext context) throws ArgumentNotResolvable {
        final Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            final Class<?> type = parameterTypes[i];
            final Annotation[] annotations = parameterAnnotations[i];
            if (containsUnresolved(annotations)) {
                parameters[i] = getUnresolvedFromContext(type, context);
            } else {
                final ArgumentFactory factory = argumentFactories.get(type);
                if (factory == null) {
                    Console.severe("No argument registered fot type %s", type);
                    return null;
                }
                final Argument<?> argument = factory.create();
                resolve(argument, context);
                parameters[i] = argument.value();
            }
        }
        return parameters;
    }

    private Object getUnresolvedFromContext(Class<?> clazz, CommandContext context) {
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

    private boolean containsUnresolved(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Unresolved) return true;
        }
        return false;
    }

    public void resolve(Argument<?> argument, CommandContext context) throws ArgumentNotResolvable {
        Argument<?> arg = resolveAndReturnLast(argument, context);
        if (arg != null) throw ArgumentNotResolvable.withMessage("Too few args!");
    }

    public Argument<?> resolveAndReturnLast(Argument<?> argument, CommandContext context) throws ArgumentNotResolvable {
        if (argument instanceof VarArgsArgument<?>) {
            VarArgsArgument<?> arg = (VarArgsArgument<?>) argument;
            if (args.length - cursor < arg.minArgs()) throw ArgumentNotResolvable.withMessage("Too few args");
            if (args.length - cursor > arg.maxArgs()) throw ArgumentNotResolvable.withMessage("Too many args");
            final Argument<?>[] content = arg.contents();
            final int rest = args.length - cursor;
            for (int i = 0; i < rest; i++) {
                resolve(content[i], context);
            }
        } else if (argument instanceof HighLevelArg) {
            HighLevelArg<?> arg = (HighLevelArg<?>) argument;
            final Argument<?>[] contents = arg.contents();
            for (Argument<?> content: contents) {
                resolve(content, context);
            }
        } else if (argument instanceof LowLevelArg) {
            LowLevelArg<?> arg = (LowLevelArg<?>) argument;
            if (args.length == cursor) return arg;
            arg.resolve(args[cursor], context);
            cursor++;
        } else if (argument instanceof ContextualArgument<?>) {
            ContextualArgument<?> arg = (ContextualArgument<?>) argument;
            arg.resolve(context);
        }
        return null;
    }

    public HighLevelArg<?> computeRoot(Class<?>[] parameterTypes, Annotation[][] parameterAnnotations) {
        final List<Argument<?>> arguments = new ArrayList<>();
        for (int i = 0; i < parameterTypes.length; i++) {
            final Class<?> type = parameterTypes[i];
            final Annotation[] annotations = parameterAnnotations[i];
            if (containsUnresolved(annotations))
                continue;
            final ArgumentFactory factory = argumentFactories.get(type);
            if (factory == null) {
                Console.severe("No argument registered fot type %s", type);
                return null;
            }
            final Argument<?> argument = factory.create();
            arguments.add(argument);
        }
        return new SuperArgument(arguments);
    }

    private static class SuperArgument extends AbstractHighLevelArg<Object> {

        public SuperArgument(List<Argument<?>> allArgs) {
            super(allArgs.toArray(new Argument<?>[0]));
        }

        @Override
        public Object value() { return null; }
    }

}
