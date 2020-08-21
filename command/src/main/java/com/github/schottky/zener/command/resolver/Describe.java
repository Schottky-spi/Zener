package com.github.schottky.zener.command.resolver;

import java.lang.annotation.*;

/**
 * Can be used on a parameter to signal that it should be described
 * in a certain way when the usage is shown.
 * This is primarily used to describe low-level arguments, such as Strings
 * that cannot be described in another way
 */

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Describe {

    /**
     * the value to describe this with
     * @return The description
     */
    String value();
}
