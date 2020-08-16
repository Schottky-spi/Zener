package com.github.schottky.zener.config.bind;

import com.google.common.base.Preconditions;

import java.util.List;
import java.util.stream.Stream;

/**
 * convenience method to convert from a list to a certain type.
 * The type of the object that is converted is a list of
 * unknown elements
 * @param <T> The type to convert to
 */
public interface ListConvertible<T> extends Convertible<T> {

    @Override
    default T convertFrom(Object in) {
        Preconditions.checkArgument(in instanceof List);
        return convertFromStream(((List<?>) in).stream());
    }

    /**
     * convenience-method to convert from a stream to the type
     * @param stream The stream to convert from
     * @return The desired type
     */
    T convertFromStream(Stream<?> stream);

}
