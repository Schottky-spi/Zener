package com.github.schottky.zener.command.resolver.argument;

public interface VarArgsArgument<T> extends HighLevelArg<T> {

    default int minArgs() { return 0; }

    default int maxArgs() { return Integer.MAX_VALUE; }

    @Override
    default boolean isVarArgsArgument() {
        return true;
    }

    @Override
    default VarArgsArgument<T> asVarArgsArgument() {
        return this;
    }
}
