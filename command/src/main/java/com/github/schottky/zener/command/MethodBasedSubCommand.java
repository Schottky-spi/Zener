package com.github.schottky.zener.command;

import com.github.schottky.zener.command.resolver.ArgumentNotResolvable;
import com.github.schottky.zener.command.resolver.ArgumentResolver;
import com.github.schottky.zener.command.resolver.SuccessMessage;
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
        if (method.isAnnotationPresent(SuccessMessage.class))
            successMessage = method.getAnnotation(SuccessMessage.class).value();
    }

    @Override
    public boolean onAcceptedCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args)
    {
        final CommandContext context = new CommandContext(sender, command, label);
        try {
            new ArgumentResolver(args).execute(method, context, parentCommand);
        } catch (ArgumentNotResolvable notResolvable) {
            sender.sendMessage(notResolvable.getMessage());
            return true;
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return true;
        }
        if (successMessage != null)
            context.getSender().sendMessage(successMessage);
        return true;
    }
}
