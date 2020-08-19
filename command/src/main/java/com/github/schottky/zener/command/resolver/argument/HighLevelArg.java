package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.ArgumentNotResolvable;

import java.util.Deque;

public interface HighLevelArg<T> extends Argument<T> {

    Argument<?>[] contents() throws ArgumentNotResolvable;

    LowLevelArg<?> findLastArgument(Deque<String> arguments, CommandContext context);

}
