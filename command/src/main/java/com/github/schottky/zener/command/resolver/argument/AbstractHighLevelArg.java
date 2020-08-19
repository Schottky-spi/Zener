package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;

public abstract class AbstractHighLevelArg<T> implements HighLevelArg<T> {

    protected final Argument<?>[] contents;

    public AbstractHighLevelArg(Argument<?>... contents) {
        this.contents = contents;
    }

    @Override
    public Argument<?>[] contents(CommandContext context) {
        return contents;
    }
}
