package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;

import java.util.Optional;
import java.util.Queue;
import java.util.stream.Stream;

/**
 * internal usage only, marks an argument that should not be resolved
 * @param <T> The type of this argument
 */
public interface UnresolvedArgument<T> extends Argument<T> {

    @Override
    default Optional<String> description() {
        return Optional.empty();
    }

    @Override
    default boolean resolve(Queue<String> arguments) {
        return true;
    }

    @Override
    default Stream<String> tabOptions(Queue<String> arguments) {
        throw new UnsupportedOperationException("Unresolved");
    }

    @Override
    default CommandContext context() {
        throw new UnsupportedOperationException("Unresolved");
    }

    @Override
    default void setValue(T initial) {
        throw new UnsupportedOperationException("Unresolved");
    }

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
        return false;
    }

    @Override
    default boolean isUnresolved() {
        return true;
    }

    @Override
    default UnresolvedArgument<T> asUnresolvedArgument() {
        return this;
    }

    @Override
    default boolean isVarArgsArgument() {
        return false;
    }
}
