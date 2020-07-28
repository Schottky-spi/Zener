package com.schottky.github.zener.config;

public interface TypeAdapter<T,Z> {

    Class<T> optionsType();

    Object toOptionsType(Object configValue);
}
