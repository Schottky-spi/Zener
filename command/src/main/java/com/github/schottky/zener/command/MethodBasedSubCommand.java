package com.github.schottky.zener.command;

import com.github.schottky.zener.command.resolver.ArgumentNotResolvable;
import com.github.schottky.zener.command.resolver.ArgumentResolver;
import com.github.schottky.zener.command.resolver.ArgumentResolvers;
import com.github.schottky.zener.localization.Language;
import com.github.schottky.zener.messaging.Console;
import com.google.common.base.Preconditions;
import org.apiguardian.api.API;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@API(status = API.Status.INTERNAL)
public final class MethodBasedSubCommand<T extends CommandBase> extends SubCommand<T> {

    private final T embedded;
    private final Method caller;
    private final ArgumentResolver<?>[] resolvers;
    private final Class<?> sender;

    public MethodBasedSubCommand(Method method, T parent) {
        super(true, parent);
        this.caller = method;
        this.embedded = parent;
        final Class<?>[] parameters = method.getParameterTypes();
        Preconditions.checkArgument(parameters.length >= 1, "Parameters must contain sender");

        this.sender = parameters[0];
        this.resolvers = new ArgumentResolver<?>[parameters.length - 1];
        for (int i = 1; i < parameters.length; i++) {
            final ArgumentResolver<?> resolver = ArgumentResolvers.forClass(parameters[i]);
            if (resolver == null) {
                Console.severe("Cannot find resolver for type %s", parameters[i].getName());
                Console.severe("Verify that you have registered a resolver for exactly that type");
            }
            resolvers[i - 1] = resolver;
        }
        this.minArgsLength = parameters.length - 1;
        this.maxArgsLength = parameters.length - 1;

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
    }

    @Override
    public boolean onAcceptedCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args)
    {
        if (this.sender.isAssignableFrom(sender.getClass())) {
            final Object[] arguments = new Object[args.length + 1];
            arguments[0] = sender;
            for (int i = 0; i < resolvers.length; i++) {
                try {
                    arguments[i + 1] = resolvers[i].resolve(args[i]);
                } catch (ArgumentNotResolvable notResolvable) {
                    sender.sendMessage(notResolvable.getMessage());
                    return true;
                }
            }
            invokeWith(arguments);
        } else {
            // not executable as that sender
            sender.sendMessage(Language.current().translate("command.not_executable_as"));
        }
        return true;
    }

    private void invokeWith(Object[] arguments) {
        try {
            caller.invoke(embedded, arguments);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Console.error(e);
        }
    }
}
