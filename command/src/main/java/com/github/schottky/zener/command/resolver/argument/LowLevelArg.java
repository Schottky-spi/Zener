package com.github.schottky.zener.command.resolver.argument;

import java.util.stream.Stream;

public interface LowLevelArg<T> extends Argument<T> {

    void setDescription(String description, boolean isOptional);

    Stream<String> optionsAsString();

    void setOptions(Stream<String> options);

    @Override
    default boolean isLowLevel()        { return true; }
    @Override
    default boolean isHighLevel()       { return false; }
    @Override
    default boolean isContextualOnly()  { return false; }
    @Override
    default boolean isUnresolved()      { return false; }
    @Override
    default boolean isVarArgsArgument() { return false; }

    @Override
    default LowLevelArg<T> asLowLevelArg() {
        return this;
    }
}
