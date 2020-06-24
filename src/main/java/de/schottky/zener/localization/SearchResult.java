package de.schottky.zener.localization;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

// Internal class to represent a result to a certain search
class SearchResult<T> {

    @NotNull
    @Contract(value = " -> new", pure = true)
    public static <T> SearchResult<T> notFound() {
        return new SearchResult<>(null);
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <T> SearchResult<T> of(T value) {
        return new SearchResult<>(value);
    }

    private final T value;

    private SearchResult(T value) {
        this.value = value;
    }

    public boolean isSet() {
        return value != null;
    }

    public T orElseSupply(Supplier<T> other) {
        return isSet() ? value : other.get();
    }

    public <X extends Exception> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isSet()) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }
}
