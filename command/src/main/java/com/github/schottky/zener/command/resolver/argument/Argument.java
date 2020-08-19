package com.github.schottky.zener.command.resolver.argument;

public interface Argument<T> {

    T value();

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
