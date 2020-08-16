package com.github.schottky.zener.config;

import java.lang.annotation.*;

/**
 * indicates that a certain config-value can be retrieved by following
 * the specified path in the resp. config instead of looking up the field
 * name inside the config
 */

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Path {

    /**
     * The path that that the content of the annotated field will look up
     * the config-value
     * @return The path
     */
    String value();
}
