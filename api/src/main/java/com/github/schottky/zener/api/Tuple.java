package com.github.schottky.zener.api;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * An immutable and unmodifiable structure that holds two elements of some kind
 * @param <A> The type of the first element
 * @param <B> The type of the second element
 */

@API(status = Status.STABLE)
public class Tuple<A,B> {

    /**
     * Appends a collection of tuples to a map. Returns the provided map
     * Since maps cannot contain multiple keys, it cannot be guaranteed that
     * size(map_pre) + size(collection) = size(map_after)
     * @param collection The collection to add to the map
     * @param map The map to append this collection to
     * @param <K> The type of a tuple's first element
     * @param <V> The type of a tuple's second element
     * @param <X> The type of the map
     * @return The modified map
     */

    @Contract(value = "_, _ -> param2", mutates = "param2")
    public static <K,V, X extends Map<K,V>> X appendToMap(@NotNull Collection<Tuple<K,V>> collection, X map) {
        return Tuple.appendToMap(collection, map, Function.identity());
    }

    /**
     * Appends a collection of tuples to a map while transferring the individual
     * elements to a new value
     * Returns the provided map
     * Since maps cannot contain multiple keys, it cannot be guaranteed that
     * size(map_pre) + size(collection) = size(map_after).
     * @param collection The collection to add to the map
     * @param map The map to append this collection to
     * @param remappingFunction The function to apply to each element
     * @param <A> The type of a tuple's first element
     * @param <B> The type of a tuple's second element
     * @param <K> The type of the keys of the map
     * @param <V> The type of the values of the map
     * @param <M> The map
     * @return The modified map
     */

    @Contract("_, _, _ -> param2")
    public static <A,B,K,V, M extends Map<K,V>> M appendToMap(
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

    /**
     * returns a new tuple with the two given elements
     * @param firstElement The first element
     * @param secondElement The second element
     * @param <A> The type of the first element
     * @param <B> The type of the second element
     * @return The newly created tuple
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <A,B> Tuple<A,B> of(A firstElement, B secondElement) {
        return new Tuple<>(firstElement, secondElement);
    }

    /**
     * returns a tuple that has the same entries as the provided
     * map-entry
     * @param entry The entry
     * @param <A> The type of the first element
     * @param <B> The type of the second element
     * @return The newly created tuple
     */

    @Contract("_ -> new")
    public static <A,B> @NotNull Tuple<A,B> of(Map.@NotNull Entry<A,B> entry) {
        return new Tuple<>(entry.getKey(), entry.getValue());
    }

    /**
     * creates a new tuple
     * @param firstElement The first element
     * @param secondElement The second element
     */

    public Tuple(A firstElement, B secondElement) {
        this.firstElement = firstElement;
        this.secondElement = secondElement;
    }

    /**
     * returns the first element
     * @return The first element
     */

    public A first() {
        return firstElement;
    }

    /**
     * returns the second element
     * @return The second element
     */

    public B second() {
        return secondElement;
    }

    /**
     * combines the two entries into one. If this was a tuple
     * of two ints, such a combiner could, for example, add these two ints
     * together:
     * <pre>{@code
     * Tuple<Integer,Integer> tuple = Tuple.of(35,7);
     * // prints out "42"
     * System.out.println(tuple.combine((i1, i2) -> i1 + i2));
     * }</pre>
     * @param combiner The function to apply to the two elements
     * @param <T> The type of the value to return
     * @return The single element that is a combination of both
     */

    public <T> T combine(@NotNull BiFunction<A,B,T> combiner) {
        return combiner.apply(firstElement, secondElement);
    }

    /**
     * returns a new tuple where the elements are permuted, in other
     * words the first element is the second element and the second element
     * is the first element
     * @return The newly created tuple
     */

    public Tuple<B,A> permuted() {
        return new Tuple<>(secondElement, firstElement);
    }

    /**
     * returns a new, unmodifiable map that contains the first element as key
     * and the second element as value
     * @return The singleton-map
     * @see Collections#singletonMap(Object, Object)
     */

    public Map<A,B> asSingletonMap() {
        return Collections.singletonMap(firstElement, secondElement);
    }

    /**
     * tests if a given predicate is true for both elements
     * @param predicate The predicate to test
     * @return true, if the test was evaluated positively, false otherwise
     */

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
