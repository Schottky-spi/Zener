package de.schottky.zener.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class CollectionUtil {

    private CollectionUtil() {}

    /**
     * returns a modifiable list that uses the given {@code Iterable} object and applies the given
     * mapper-function to each object that the iterable object provides.
     * @param iterable The iterable object to transform
     * @param mapper The mapper to apply to each element of the iterable
     * @param <V> The type of the list to transform
     * @param <T> The type of the list to return
     * @return A new modifiable list
     */

    @NotNull
    public static <V,T> List<T> modifiableListUsing(@NotNull Iterable<V> iterable, Function<V,T> mapper) {
        List<T> out = new ArrayList<>();
        for (V v: iterable) out.add(mapper.apply(v));
        return out;
    }
    
    public static <T> Optional<T> lastOf(@NotNull List<T> list) {
        if (list.size() == 0) return Optional.empty();
        return Optional.of(list.get(list.size() - 1));
    }
}
