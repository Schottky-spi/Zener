package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.resolver.CommandException;

import java.util.stream.Stream;

public interface LowLevelArg<T> extends Argument<T> {

    Stream<T> options() throws CommandException;

    String toString(T value);

    void resolve(String arg);

    Stream<String> optionsAsString();

}
