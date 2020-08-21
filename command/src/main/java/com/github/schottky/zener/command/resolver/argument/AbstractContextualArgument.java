package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.ArgumentNotResolvable;
import com.github.schottky.zener.command.resolver.CommandException;
import org.jetbrains.annotations.Nullable;

/**
 * base class for an argument that is resolved solely based on it's context
 * @param <T> The value to be resolved
 */
public abstract class AbstractContextualArgument<T> extends AbstractArgument<T> implements ContextualArgument<T> {

    /**
     * creates a new argument with the provided value.
     * This should be used in {@link VarArgsArgument VarArgs-Argument}s, where
     * @param context The context that this argument is being used in
     * @param initialValue The initial argument to pass
     */

    public AbstractContextualArgument(CommandContext context, T initialValue) {
        super(context);
        this.value = initialValue;
    }

    public AbstractContextualArgument(CommandContext context) {
        super(context);
    }

    private T value;

    /**
     * resolves and returns a value based on the context
     * @return The value that was resolved
     * @throws ArgumentNotResolvable When the value cannot be resolved
     */
    public abstract T fromContext() throws ArgumentNotResolvable;

    @Override
    public void resolve() throws CommandException {
        this.value = fromContext();
    }

    @Override
    public T value() {
        return value;
    }

    @Override
    public @Nullable String description() {
        return null;
    }
}
