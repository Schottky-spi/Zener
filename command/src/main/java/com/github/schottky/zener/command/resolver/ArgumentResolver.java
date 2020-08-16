package com.github.schottky.zener.command.resolver;

public interface ArgumentResolver<T> {

    T resolve(String argument) throws ArgumentNotResolvable;

}
