package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.CommandException;
import org.jetbrains.annotations.Nullable;

public interface Argument<T> {

    @Nullable String description();

    boolean isOptionalArgument();

    T value();

    default T value(CommandContext context) throws CommandException {
        return value();
    }

    default int asInt() {
        if (value() instanceof Integer)
            return (Integer) value();
        throw new ClassCastException();
    }

    default double asDouble() {
        if (value() instanceof Double)
            return (Double) value();
        throw new ClassCastException();
    }

    default boolean asBoolean() {
        if (value() instanceof Boolean)
            return (Boolean) value();
        throw new ClassCastException();
    }

    default <V> V as(Class<V> vClass) {
        return vClass.cast(value());
    }
}
