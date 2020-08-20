package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.CommandException;
import com.github.schottky.zener.localization.Language;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public abstract class AbstractLowLevelArg<T> implements LowLevelArg<T> {

    public AbstractLowLevelArg(T initialValue) {
        this.value = initialValue;
    }

    public AbstractLowLevelArg() { }

    private T value;

    @Override
    public void resolve(String arg, CommandContext context) throws CommandException {
        this.value = fromArgument(arg, context);
    }

    protected abstract T fromArgument(String arg, CommandContext context) throws CommandException;

    @Override
    public T value() {
        return value;
    }

    private Stream<T> options = Stream.of();

    @Override
    public Stream<T> options(CommandContext context) throws CommandException {
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

    protected String description;

    @Override
    public @Nullable String description() {
        if (description == null) return null;
        else return optionalArg ? "[" + description + "]" : "<" + description + ">";
    }

    public AbstractLowLevelArg<T> withDescription(String description, boolean optional) {
        this.optionalArg = optional;
        return this.withDescription(description);
    }

    public AbstractLowLevelArg<T> withDescription(String description) {
        if (Language.isValidIdentifier(description))
            this.description = Language.current().translate(description);
        else
            this.description = description;
        return this;
    }

    public AbstractLowLevelArg<T> setOptional(boolean optionalArg) {
        this.optionalArg = optionalArg;
        return this;
    }

    private boolean optionalArg = false;

    @Override
    public boolean isOptionalArgument() {
        return optionalArg;
    }
}
