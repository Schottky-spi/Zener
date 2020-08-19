package com.github.schottky.zener.command.resolver.argument;

public abstract class AbstractHighLevelVarArg<T> extends AbstractHighLevelArg<T> implements VarArgsArgument<T> {

    public AbstractHighLevelVarArg(Argument<?>... arguments) {
        super(arguments);
    }

}
