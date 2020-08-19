package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.ArgumentNotResolvable;

public interface ContextualArgument<T> extends Argument<T> {

    void resolve(CommandContext context) throws ArgumentNotResolvable;
}
