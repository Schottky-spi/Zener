package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;

import java.util.Queue;

public abstract class AbstractHighLevelVarArg<T> extends AbstractHighLevelArg<T> implements VarArgsArgument<T> {

    public AbstractHighLevelVarArg(CommandContext context, Argument<?>... arguments) {
        super(context, arguments);
    }

    public AbstractHighLevelVarArg(CommandContext context, ArgumentBuilder<?>... builders) {super(context, builders);}

    @Override
    public boolean resolve(Queue<String> arguments) {
        return super.resolve(arguments);
    }
}
