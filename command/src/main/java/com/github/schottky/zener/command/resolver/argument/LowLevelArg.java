package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.ArgumentNotResolvable;

import java.util.stream.Stream;

public interface LowLevelArg<T> extends Argument<T> {

    void resolve(String arg, CommandContext context) throws ArgumentNotResolvable;

    Stream<String> tabCompletions();

}
