package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.ArgumentNotResolvable;
import com.github.schottky.zener.command.resolver.CommandException;

/**
 * base class for an argument that is resolved solely based on it's context
 * @param <T> The value to be resolved
 */
public abstract class AbstractContextualArgument<T> implements ContextualArgument<T> {

    /**
     * creates a new argument with the provided value.
     * This should be used in {@link VarArgsArgument VarArgs-Argument}s, where
     *
     * @param initialValue The initial argument to pass
     */

    public AbstractContextualArgument(T initialValue) {
        this.value = initialValue;
    }

    public AbstractContextualArgument() {}

    private T value;

    /**
     * resolves and returns a value based on the context
     * @param context The context to resolve from
     * @return The value that was resolved
     * @throws ArgumentNotResolvable When the value cannot be resolved
     */
    public abstract T fromContext(CommandContext context) throws CommandException;

    @Override
    public void resolve(CommandContext context) throws CommandException {
        this.value = fromContext(context);
    }

    @Override
    public T value() {
        return value;
    }
}
