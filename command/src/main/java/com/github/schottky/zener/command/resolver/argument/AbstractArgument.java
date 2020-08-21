package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;

public abstract class AbstractArgument<T> implements Argument<T> {

    public AbstractArgument(CommandContext context) {
        this.context = context;
    }

    protected final CommandContext context;
}
