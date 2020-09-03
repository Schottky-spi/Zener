package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;

/**
 * represents an argument that can be resolved based solely on the
 * {@link CommandContext}
 * @param <T> The type that should be resolved
 */

public interface ContextualArgument<T> extends Argument<T> {

    void resolve();

    @Override
    default boolean isLowLevel() {
        return false;
    }

    @Override
    default boolean isHighLevel() {
        return false;
    }

    @Override
    default boolean isContextualOnly() {
        return true;
    }

    @Override
    default ContextualArgument<T> asContextualArgument() {
        return this;
    }

    @Override
    default boolean isUnresolved() {
        return false;
    }

    @Override
    default boolean isVarArgsArgument() {
        return false;
    }
}
