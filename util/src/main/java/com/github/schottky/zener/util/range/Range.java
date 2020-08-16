package com.github.schottky.zener.util.range;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public interface Range<C extends Comparable<C>> extends Iterable<C> {

    C lowerBound();

    C upperBound();

    default boolean isInRange(C value) {
        return value.compareTo(lowerBound()) >= 0 && value.compareTo(upperBound()) < 1;
    }

    default Stream<C> stream() {
        Collection<C> collection = new ArrayList<>();
        for (C c: this) {
            collection.add(c);
        }
        return collection.stream();
    }


    static IntRange between(int lower, int upper) {
        return new IntRange(lower, upper);
    }

    static IntRange indexing(int[] arr) {
        return new IntRange(0, arr.length);
    }

    static IntRange indexing(byte[] arr) {
        return new IntRange(0, arr.length);
    }

    static IntRange indexing(char[] arr) {
        return new IntRange(0, arr.length);
    }

    static IntRange indexing(long[] arr) {
        return new IntRange(0, arr.length);
    }

    static IntRange indexing(float[] arr) {
        return new IntRange(0, arr.length);
    }

    static IntRange indexing(short[] arr) {
        return new IntRange(0, arr.length);
    }

    static IntRange indexing(double[] arr) {
        return new IntRange(0, arr.length);
    }

    static IntRange indexing(Object[] arr) {
        return new IntRange(0, arr.length);
    }

    static <C extends Comparable<C>> Range<C> of(C singleElement) {
        return new SingletonRange<>(singleElement);
    }

    class SingletonRange<C extends Comparable<C>> implements Range<C> {

        private final C c;

        public SingletonRange(C c) {
            this.c = c;
        }

        @Override
        public C lowerBound() {
            return c;
        }

        @Override
        public C upperBound() {
            return c;
        }

        @Override
        public Stream<C> stream() {
            return Stream.of(c);
        }

        @NotNull
        @Override
        public Iterator<C> iterator() {
            return new Iterator<C>() {
                private boolean first = true;
                @Override
                public boolean hasNext() { return first; }

                @Override
                public C next() {
                    if (!first) throw new NoSuchElementException();
                    first = false;
                    return c;
                }
            };
        }
    }

    static <C extends Comparable<C>> Range<C> empty() {
        return new EmptyRange<>();
    }

    class EmptyRange<C extends Comparable<C>> implements Range<C> {

        @Override
        public C lowerBound() {
            return null;
        }

        @Override
        public C upperBound() {
            return null;
        }

        @Override
        public Stream<C> stream() {
            return Stream.of();
        }

        @NotNull
        @Override
        public Iterator<C> iterator() {
            return new Iterator<C>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public C next() {
                    return null;
                }
            };
        }
    }

    static <C extends Comparable<C>> Range<C> of(C lower, C upper, UnaryOperator<C> incrementer) {
        return new AnyRange<>(lower, upper, incrementer);
    }

    class AnyRange<C extends Comparable<C>> implements Range<C> {

        private final C lowerBound, upperBound;
        private final UnaryOperator<C> incrementer;

        public AnyRange(C lowerBound, C upperBound, UnaryOperator<C> incrementer) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            this.incrementer = incrementer;
        }

        @Override
        public C lowerBound() {
            return lowerBound;
        }

        @Override
        public C upperBound() {
            return upperBound;
        }

        @NotNull
        @Override
        public Iterator<C> iterator() {
            return new Iterator<C>() {

                C current = lowerBound;

                @Override
                public boolean hasNext() {
                    return lowerBound.compareTo(upperBound) < 1;
                }

                @Override
                public C next() {
                    C old = current;
                    current = incrementer.apply(lowerBound);
                    return old;
                }
            };
        }
    }
}
