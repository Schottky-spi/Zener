package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.ArgumentNotResolvable;

public interface HighLevelArg<T> extends Argument<T> {

    Argument<?>[] contents(CommandContext context) throws ArgumentNotResolvable;

}
