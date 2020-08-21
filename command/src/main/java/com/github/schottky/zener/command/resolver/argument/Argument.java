package com.github.schottky.zener.command.resolver.argument;

import org.jetbrains.annotations.Nullable;

/**
 * represents an Argument
 * @param <T> The type that this argument represents
 */
public interface Argument<T> {

    @Nullable String description();

    T value();

    default int asInt() {
        return as(Integer.class);
    }

    default double asDouble() {
        return as(Double.class);
    }

    default boolean asBoolean() {
        return as(Boolean.class);
    }

    default <V> V as(Class<V> vClass) {
        return vClass.cast(value());
    }
}
