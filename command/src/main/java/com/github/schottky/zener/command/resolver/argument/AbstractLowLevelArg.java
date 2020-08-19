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

    private Stream<T> options = Stream.of();

    @Override
    public Stream<T> options(CommandContext context) {
        return options;
    }

    @Override
    public String toString(T value) {
        return value.toString();
    }

    public AbstractLowLevelArg<T> withOptions(Stream<T> options) {
        this.options = options;
        return this;
    }
}
