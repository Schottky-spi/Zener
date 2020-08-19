package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.ArgumentNotResolvable;

import java.util.stream.Stream;

public abstract class AbstractLowLevelArg<T> implements LowLevelArg<T> {

    public AbstractLowLevelArg(T initialValue) {
        this.value = initialValue;
    }

    public AbstractLowLevelArg() { }

    private T value;

    @Override
    public void resolve(String arg, CommandContext context) throws ArgumentNotResolvable {
        this.value = fromArgument(arg, context);
    }

    protected abstract T fromArgument(String arg, CommandContext context) throws ArgumentNotResolvable;

    @Override
    public T value() {
        return value;
    }

    @Override
    public Stream<String> tabCompletions() {
        return Stream.of();
    }
}
