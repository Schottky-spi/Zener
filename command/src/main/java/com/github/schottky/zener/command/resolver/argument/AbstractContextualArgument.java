package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.ArgumentNotResolvable;

public abstract class AbstractContextualArgument<T> implements ContextualArgument<T> {

    public AbstractContextualArgument(T initialValue) {
        this.value = initialValue;
    }

    public AbstractContextualArgument() {}

    private T value;

    public abstract T fromContext(CommandContext context) throws ArgumentNotResolvable;

    @Override
    public void resolve(CommandContext context) throws ArgumentNotResolvable {
        this.value = fromContext(context);
    }

    @Override
    public T value() {
        return value;
    }
}
