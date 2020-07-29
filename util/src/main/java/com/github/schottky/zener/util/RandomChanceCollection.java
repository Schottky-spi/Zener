package com.github.schottky.zener.util;

import com.github.schottky.zener.api.Tuple;
import com.google.common.base.Preconditions;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A collection that allows weighted elements and has the special function
 * {@link RandomChanceCollection#randomElement()} to retrieve a randomly chosen
 * element based on it's weight. There are two views to this collection.
 * <br> The external view is that this collection represents a {@link Collection} of {@link Tuple}'s.
 * Each tuple contains the weight (a double) and and the element that is assigned to this weight.
 * This view is reflected by typical collection-calls such as {@link Collection#add(Object)},
 * {@link Collection#remove(Object)},...
 * <br> The internal view is that of a {@link NavigableMap} that maps doubles to elements. These doubles are
 * not the weights themselves, but a sum of all weights, in order that they were inserted.
 * <br> The importance of this distinction is that insertion and retrieving an element is directly done on the map
 * and can therefore have expected map-behavior concerning computation time. Retrieving a random element can be done
 * in <i>O(log(N))</i>, where N is the amount of stored tuples.
 * <br> Iterating over the collection and removing elements, however, is not directly done on the map and can therefore
 * not be performed in a manner that would be expected for a map. Especially remove-operations are expensive since they
 * have to re-populate the internal map and should thus be avoided.
 * The only exception to this rule is the removal of the last element wince that only removes the last element and does
 * not have to clear the internal map.
 * <br> The only place where the internal view is the external view is during serialization
 * @param <E> The type of elements to store / receive
 */

public class RandomChanceCollection<E> extends AbstractCollection<Tuple<Double,E>> implements ConfigurationSerializable {

    private final Random random;

    private double total = 0.0;

    private final NavigableMap<Double,E> internalMap;

    // constructors

    /**
     * constructs a new RandomChanceCollection with a given Random
     * @param random The random to use
     */

    public RandomChanceCollection(Random random) {
        this.random = random;
        this.internalMap = new TreeMap<>();
    }

    /**
     * constructs a new RandomChanceCollection using {@link ThreadLocalRandom#current()}
     */

    public RandomChanceCollection() {
        this(ThreadLocalRandom.current());
    }

    /**
     * constructs a new RandomChanceCollection using the given random and an initial collection
     * of weighted elements
     * @param random The random to use
     * @param initialValues The initial values of this collection
     */

    public RandomChanceCollection(Random random, Collection<Tuple<Double,E>> initialValues) {
        this(random);
        this.addAll(initialValues);
    }

    /**
     * constructs a new RandomChanceCollection using the given initial collection
     * and the {@link ThreadLocalRandom#current()}
     * @param initialValues The initial values of this collection
     */

    public RandomChanceCollection(Collection<Tuple<Double,E>> initialValues) {
        this(ThreadLocalRandom.current(), initialValues);
    }

    /**
     * static factory-methods that is equivalent to calling
     * <pre>{@code
     * RandomChanceCollection<E> randomChanceCollection = new RandomChanceCollection<>(initialValues);
     * }</pre>
     * @param initialValues The initial values to add
     * @param <E> The type of the collection
     * @return THe newly created collection
     */

    @Contract("_ -> new")
    public static <E> @NotNull RandomChanceCollection<E> of(Collection<Tuple<Double,E>> initialValues) {
        return new RandomChanceCollection<>(initialValues);
    }

    // satisfy AbstractCollection

    /**
     * Returns an Iterator that contains the elements in order they were inserted
     * with the weight that they were inserted with
     * @return The iterator
     */

    @Override
    public @NotNull Iterator<Tuple<Double, E>> iterator() {
        return new Itr(internalMap.entrySet().iterator());
    }

    // iterator-implementation
    private class Itr implements Iterator<Tuple<Double,E>> {

        private final Iterator<Map.Entry<Double,E>> entryIterator;
        private Map.Entry<Double,E> current;
        private double total = 0.0;

        Itr(Iterator<Map.Entry<Double,E>> entryIterator) {
            this.entryIterator = entryIterator;
        }

        @Override
        public boolean hasNext() {
            return entryIterator.hasNext();
        }

        @Override
        public Tuple<Double, E> next() {
            this.current = entryIterator.next();
            double weight = current.getKey() - total;
            this.total = current.getKey();
            return Tuple.of(weight, current.getValue());
        }

        @Override
        public void remove() {
            // remove the last element, update not necessary
            if (!this.hasNext()) {
                entryIterator.remove();
                RandomChanceCollection.this.total = internalMap.lowerKey(current.getKey());
            } else {
                // copy this map and clear it
                NavigableMap<Double,E> copy = new TreeMap<>(internalMap);
                clear();
                double oldTotal = 0.0;
                double newTotal = 0.0;
                for (Map.Entry<Double, E> next : copy.entrySet()) {
                    double weight = next.getKey() - oldTotal;
                    oldTotal = next.getKey();
                    if (!next.equals(current)) {
                        newTotal += weight;
                        internalMap.put(newTotal, next.getValue());
                    }
                }
                RandomChanceCollection.this.total = newTotal;
            }
        }
    }

    /**
     * returns the number of elements that this collection manages
     * @return The size of this collection
     */

    @Override
    public int size() {
        return internalMap.size();
    }

    @Override
    public void clear() {
        this.internalMap.clear();
        this.total = 0.0;
    }

    /**
     * adds a new weighted element to this collection
     * @param weight The weight of the element
     * @param value The value associated with this weight
     * @return true, if the collection changed after adding an element, false otherwise
     * @throws IllegalArgumentException if the weight is strictly negative
     */

    public boolean add(double weight, E value) {
        Preconditions.checkArgument(weight >= 0, "weights must be greater than or equal to 0");
        if (weight == 0) return false;
        total += weight;
        return this.internalMap.put(total, value) == null;
    }

    /**
     * adds a weighted element to this collection.
     * This is equivalent to calling {@code #add(tuple.first(), tuple.second())}
     * @param tuple The weighted element to add
     * @return true, if the collection changed after adding an element, false otherwise
     * @throws IllegalArgumentException if the weight is strictly negative
     */

    @Override
    public boolean add(@NotNull Tuple<Double,E> tuple) {
        return this.add(tuple.first(), tuple.second());
    }

    /**
     * removes a weighted element from this collection
     * @param weight The weight of the element
     * @param element The element associated to this weight
     * @return true, if the collection changes as a result of this call
     */

    public boolean remove(double weight, E element) {
        return remove(new Tuple<>(weight, element));
    }

    /**
     * returns true if, and only if this collection contains a certain value. The weight of this
     * value is arbitrary
     * @param value The value to check for
     * @return true, if the collection contained the value, false otherwise
     */

    public boolean containsValue(E value) {
        return internalMap.containsValue(value);
    }

    /**
     * applies the given action for each weighted element of this collection
     * @param action The action to perform
     * @see #forEachElement(Consumer)
     */

    public void forEach(BiConsumer<? super Double,? super E> action) {
        this.forEach(tuple -> action.accept(tuple.first(), tuple.second()));
    }

    /**
     * applies the given action for each element of this collection
     * @param action The action to apply
     */

    public void forEachElement(Consumer<? super E> action) {
        this.internalMap.values().forEach(action);
    }

    /**
     * returns a random element from this collection using this collection's {@link Random}.
     * This element is chosen based on it's weight. Weights that are greater have a higher chance to be
     * chosen than those with lower weights.
     * @return the randomly chosen element. Returns {@code null}, if the collection is empty,
     * or the element is actually {@code null}
     */

    public @Nullable E randomElement() {
        final double actualValue = random.nextDouble() * total;
        if (this.isEmpty()) return null;
        return this.internalMap.higherEntry(actualValue).getValue();
    }

    /**
     * returns an unmodifiable collection of <i>n</i> randomly chosen elements.
     * @param amount The count of elements to get
     * @return A collection containing n randomly chosen elements from this collection.
     * Duplicate elements are allowed; the returned collection will have a size of n
     * @throws IllegalArgumentException if the amount is smaller than 0
     */

    // all elements are of type 'E'
    @SuppressWarnings("unchecked")
    public Collection<E> randomElements(int amount) {
        Preconditions.checkArgument(amount >= 0, "negative amount");
        Object[] values = new Object[amount];
        for (int i = 0; i < amount; i++) {
            values[i] = randomElement();
        }
        return (Collection<E>) Arrays.asList(values);
    }

    /**
     * returns the sum of all weights. This can be seen as equivalent to the following:
     * <pre>{@code randomChanceCollection.stream().mapToDouble(Tuple::first).sum()}</pre>
     * @return all weights
     */

    public double sumOfWeights() {
        return total;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        RandomChanceCollection<?> that = (RandomChanceCollection<?>) object;
        return internalMap.equals(that.internalMap);
    }

    @Override
    public int hashCode() {
        return internalMap.hashCode();
    }

    /**
     * serializes this collection returning a map that contains the sum of probabilities
     * as keys and the associated objects as values
     * @return The serialized map
     */

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String,Object> map = new LinkedHashMap<>();
        this.internalMap.forEach((accumulatedWeight, value) -> map.put(accumulatedWeight.toString(), value));
        return map;
    }

    /**
     * constructs a new RandomChanceCollection from a raw map that contains already summed
     * up probabilities as keys and the associated objects as values.
     * Objects are assumed to be compatible with E
     * @param rawMap The raw map to deserialize from
     */

    // cannot make a checked cast (we do not have any information about the elements)
    // used via reflection
    @SuppressWarnings({"unchecked", "unused"})
    private RandomChanceCollection(@NotNull Map<String,Object> rawMap) {
        this();
        for (Map.Entry<String,Object> entry: rawMap.entrySet()) {
            double d;
            try {
                d = Double.parseDouble(entry.getKey());
            } catch (NumberFormatException e) {
                throw new Error(e);
            }
            this.internalMap.put(d, (E) entry.getValue());
        }
        this.total = internalMap.lastKey();
    }
}
