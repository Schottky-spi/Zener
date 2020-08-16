package com.github.schottky.zener.util.range;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

public class IntRange implements Iterable<Integer> {
    int lower, upper;

    public IntRange(int lower, int upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public int lowerBound() {
        return lower;
    }

    public int upperBound() {
        return upper;
    }

    public IntStream stream() {
        return IntStream.range(lower, upper);
    }

    @NotNull
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {

            int current = lower;

            @Override
            public boolean hasNext() {
                return current < upper;
            }

            @Override
            public Integer next() {
                int old = current;
                current += 1;
                if (current > upper) throw new NoSuchElementException();
                return old;
            }
        };
    }
}
