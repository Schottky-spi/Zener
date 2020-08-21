package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.CommandException;
import com.github.schottky.zener.localization.Language;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public abstract class AbstractLowLevelArg<T> extends AbstractArgument<T> implements LowLevelArg<T> {

    public AbstractLowLevelArg(CommandContext context, T initialValue) {
        super(context);
        this.value = initialValue;
    }

    public AbstractLowLevelArg(CommandContext context) {
        super(context);
    }

    private Stream<T> options = Stream.empty();

    @Override
    public Stream<T> options() throws CommandException {
        checkPermission();
        return options;
    }

    @Override
    public Stream<String> optionsAsString() {
        checkPermission();
        return options.map(this::toString);
    }

    @Override
    public String toString(T value) {
        return value.toString();
    }

    public AbstractLowLevelArg<T> withOptions(Stream<T> options) {
        this.options = options;
        return this;
    }

    private T value;

    @Override
    public T value() {
        return value;
    }

    @Override
    public void resolve(String arg) {
        checkPermission();
        this.value = fromArgument(arg);
    }

    protected abstract T fromArgument(String arg);

    protected String description;

    @Override
    public @Nullable String description() {
        if (description == null) return null;
        checkPermission();
        return optionalArg ? "[" + description + "]" : "<" + description + ">";
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

    private boolean optionalArg = false;

    public AbstractLowLevelArg<T> setOptional(boolean optionalArg) {
        this.optionalArg = optionalArg;
        return this;
    }

    private String permission;

    public AbstractLowLevelArg<T> requirePermission(String permission) {
        this.permission = permission;
        return this;
    }

    private void checkPermission() {
        if (permission != null && !context.getSender().hasPermission(permission))
            throw new CommandException("You do not have permission");
    }

}
