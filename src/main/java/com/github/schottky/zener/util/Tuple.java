package com.github.schottky.zener.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Tuple<A,B> {

    private final A firstElement;
    private final B secondElement;

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <A,B> Tuple<A,B> of(A firstElement, B secondElement) {
        return new Tuple<>(firstElement, secondElement);
    }

    public Tuple(A firstElement, B secondElement) {
        this.firstElement = firstElement;
        this.secondElement = secondElement;
    }

    public A first() {
        return firstElement;
    }

    public B second() {
        return secondElement;
    }
}
