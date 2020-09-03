package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.resolver.CommandException;

public interface HighLevelArg<T> extends Argument<T> {

    Argument<?>[] contents() throws CommandException;

    @Override
    default boolean isLowLevel()        { return false; }
    @Override
    default boolean isHighLevel()       { return true; }
    @Override
    default boolean isContextualOnly()  { return false; }
    @Override
    default boolean isUnresolved()      { return false; }
    @Override
    default boolean isVarArgsArgument() { return false; }
    
    @Override
    default HighLevelArg<T> asHighLevelArg() { return this; }
}
