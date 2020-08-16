package com.github.schottky.zener.config.bind;

import com.github.schottky.zener.config.ConvertWith;

/**
 * A type may declare himself convertible
 * to message that it can be converted from an object.
 * This is used in accordance with {@link ConvertWith}
 * If this is the case, the type that declares this interface has to adhere
 * to some rules:
 * <ol>
 *     <li> The type either has an empty constructor or</li>
 *     <li> The type has a constructor that accepts the class ({@code Class<T>}) as single argument</li>
 * </ol>If these criteria are met, conversion from a raw config-type
 * may be performed succesfully
 * @param <T> The type that is convertible
 */
public interface Convertible<T> {

    /**
     * convert to this type from a certain object.
     * No restrictions apply to the object;
     * it can be {@code null} resp. any type
     * @param in The object to be converted
     * @return The object that this object should be converted to
     */
    T convertFrom(Object in);
}
