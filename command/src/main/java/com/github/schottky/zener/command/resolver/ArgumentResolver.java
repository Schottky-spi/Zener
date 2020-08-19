package com.github.schottky.zener.command.resolver;

import com.github.schottky.zener.command.CommandBase;
import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.argument.*;
import com.github.schottky.zener.messaging.Console;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ArgumentResolver {

    private static final Map<Class<?>,ArgumentFactory> arguments = new HashMap<>();

    static {
        arguments.put(Integer.class, Arguments.IntArgument::new);
        arguments.put(int.class, Arguments.IntArgument::new);
        arguments.put(Double.class, Arguments.DoubleArgument::new);
        arguments.put(double.class, Arguments.DoubleArgument::new);
        arguments.put(String.class, Arguments.StringArgument::new);
        arguments.put(ItemStack.class, Arguments.ItemStackArgument::new);
        arguments.put(boolean.class, Arguments.BooleanArgument::new);
        arguments.put(Boolean.class, Arguments.BooleanArgument::new);
    }

    public static void registerArgument(Class<?> clazz, ArgumentFactory factory) {
        arguments.put(clazz, factory);
    }

    public interface ArgumentFactory {
        Argument<?> create();
    }

    private final String[] args;
    private int cursor = 0;

    public ArgumentResolver(String[] args) {
        this.args = args;
    }

    public void execute(Method method, CommandContext context, CommandBase command) throws ArgumentNotResolvable, InvocationTargetException, IllegalAccessException {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Object[] parameters = new Object[parameterTypes.length];
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterTypes.length; i++) {
            final Class<?> type = parameterTypes[i];
            final Annotation[] annotations = parameterAnnotations[i];
            if (containsUnresolved(annotations)) {
                parameters[i] = getUnresolvedFromContext(type, context);
            } else {
                final ArgumentFactory factory = arguments.get(type);
                if (factory == null) {
                    Console.severe("No argument registered fot type %s", type);
                    return;
                }
                final Argument<?> argument = factory.create();
                resolve(argument, context);
                parameters[i] = argument.value();
            }
        }
        method.invoke(command, parameters);
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
        if (argument instanceof VarArgsArgument<?>) {
            VarArgsArgument<?> arg = (VarArgsArgument<?>) argument;
            if (args.length - cursor < arg.minArgs()) throw ArgumentNotResolvable.withMessage("Too few args");
            if (args.length - cursor > arg.maxArgs()) throw ArgumentNotResolvable.withMessage("Too many args");
            final Argument<?>[] content = arg.contents(context);
            final int rest = args.length - cursor;
            for (int i = 0; i < rest; i++) {
                resolve(content[i], context);
            }
        } else if (argument instanceof HighLevelArg) {
            HighLevelArg<?> arg = (HighLevelArg<?>) argument;
            final Argument<?>[] contents = arg.contents(context);
            for (Argument<?> content: contents) {
                resolve(content, context);
            }
        } else if (argument instanceof LowLevelArg) {
            LowLevelArg<?> arg = (LowLevelArg<?>) argument;
            if (args.length == cursor) throw ArgumentNotResolvable.withMessage("Too few args!");
            arg.resolve(args[cursor], context);
            cursor++;
        } else if (argument instanceof ContextualArgument<?>) {
            ContextualArgument<?> arg = (ContextualArgument<?>) argument;
            arg.resolve(context);
        }
    }

}
