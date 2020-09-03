package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.ArgumentNotResolvable;
import com.github.schottky.zener.command.resolver.CommandException;

import java.util.Optional;
import java.util.Queue;
import java.util.stream.Stream;

/**
 * base class for an argument that is resolved solely based on it's context
 * @param <T> The value to be resolved
 */
public abstract class AbstractContextualArgument<T> extends AbstractArgument<T> implements ContextualArgument<T> {

    /**
     * creates a new argument with the provided value.
     * This should be used in {@link VarArgsArgument VarArgs-Argument}s, where
     * @param context The context that this argument is being used in
     */

    public AbstractContextualArgument(CommandContext context) {
        super(context);
    }

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
    public boolean resolve(Queue<String> arguments) {
        this.value = fromContext();
        return true;
    }

    @Override
    public Stream<String> tabOptions(Queue<String> arguments) {
        return Stream.empty();
    }

    @Override
    public Optional<String> description() {
        return Optional.empty();
    }
}
