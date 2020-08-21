package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;

public abstract class AbstractHighLevelVarArg<T> extends AbstractHighLevelArg<T> implements VarArgsArgument<T> {

    public AbstractHighLevelVarArg(CommandContext context, Argument<?>... arguments) {
        super(context, arguments);
    }

}
