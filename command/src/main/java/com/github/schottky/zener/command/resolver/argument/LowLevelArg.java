package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.CommandException;

import java.util.stream.Stream;

public interface LowLevelArg<T> extends Argument<T> {

    void resolve(String arg, CommandContext context) throws CommandException;

    Stream<T> options(CommandContext context) throws CommandException;

    String toString(T value);

    default Stream<String> optionsAsString(CommandContext context) throws CommandException {
        return options(context).map(this::toString);
    }

}
