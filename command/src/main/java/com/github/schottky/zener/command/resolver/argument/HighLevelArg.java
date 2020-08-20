package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.CommandException;

import java.util.Deque;

public interface HighLevelArg<T> extends Argument<T> {

    Argument<?>[] contents(CommandContext context) throws CommandException;

    LowLevelArg<?> findLastArgument(Deque<String> arguments, CommandContext context);

}
