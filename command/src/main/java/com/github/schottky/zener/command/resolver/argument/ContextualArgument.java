package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.ArgumentNotResolvable;
import com.github.schottky.zener.command.resolver.CommandException;

/**
 * represents an argument that can be resolved based solely on the
 * {@link CommandContext}
 * @param <T> The type that should be resolved
 */

public interface ContextualArgument<T> extends Argument<T> {

    /**
     * resolves based on the command-context
     * @param context The context used to resolve this
     * @throws ArgumentNotResolvable When the resolution fails
     */

    void resolve(CommandContext context) throws CommandException;
}
