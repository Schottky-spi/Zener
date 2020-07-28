package com.github.schottky.zener.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class Tuple<A,B> {

    @Contract(value = "_, _ -> param2", mutates = "param2")
    public static <K,V, X extends Map<K,V>> X toMap(@NotNull Collection<Tuple<K,V>> collection, X map) {
        return Tuple.toMap(collection, map, Function.identity());
    }

    @Contract("_, _, _ -> param2")
    public static <A,B,K,V, M extends Map<K,V>> M toMap(
            @NotNull Collection<Tuple<A,B>> collection,
            M map,
            Function<Tuple<A,B>,Tuple<K,V>> remappingFunction)
    {
        for (Tuple<A,B> tuple: collection) {
            Tuple<K,V> mapped = remappingFunction.apply(tuple);
            map.put(mapped.first(), mapped.second());
        }
        return map;
    }

    private final A firstElement;
    private final B secondElement;

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <A,B> Tuple<A,B> of(A firstElement, B secondElement) {
        return new Tuple<>(firstElement, secondElement);
    }

    @Contract("_ -> new")
    public static <A,B> @NotNull Tuple<A,B> of(Map.@NotNull Entry<A,B> entry) {
        return new Tuple<>(entry.getKey(), entry.getValue());
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

    public <T> T combine(@NotNull BiFunction<A,B,T> combiner) {
        return combiner.apply(firstElement, secondElement);
    }

    public Tuple<B,A> permuted() {
        return new Tuple<>(secondElement, firstElement);
    }

    public boolean testBoth(@NotNull BiPredicate<A,B> predicate) {
        return predicate.test(firstElement, secondElement);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) object;
        return Objects.equals(firstElement, tuple.firstElement) &&
                Objects.equals(secondElement, tuple.secondElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstElement, secondElement);
    }

    @Override
    public String toString() {
        return "(" + firstElement + ", " + secondElement + ")";
    }
}
