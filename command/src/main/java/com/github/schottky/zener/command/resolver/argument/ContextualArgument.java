package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;

/**
 * represents an argument that can be resolved based solely on the
 * {@link CommandContext}
 * @param <T> The type that should be resolved
 */

public interface ContextualArgument<T> extends Argument<T> {

    void resolve();
}
