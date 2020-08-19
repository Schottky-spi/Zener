package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.CommandException;

import java.util.Deque;

public abstract class AbstractHighLevelArg<T> implements HighLevelArg<T> {

    protected final Argument<?>[] contents;

    public AbstractHighLevelArg(Argument<?>... contents) {
        this.contents = contents;
    }

    @Override
    public Argument<?>[] contents() {
        return contents;
    }

    public LowLevelArg<?> findLastArgument(Deque<String> arguments, CommandContext context) {
        for (Argument<?> arg: contents) {
            if (arg instanceof HighLevelArg<?>) {
                return ((HighLevelArg<?>) arg).findLastArgument(arguments, context);
            } else if (arg instanceof LowLevelArg<?>) {
                final LowLevelArg<?> lowLevelArg = (LowLevelArg<?>) arg;
                if (arguments.size() == 0) {
                    return null;
                } else if (arguments.size() == 1)
                    return lowLevelArg;
                else {
                    try {
                        lowLevelArg.resolve(arguments.pop(), context);
                    } catch (CommandException notResolvable) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
