package com.github.schottky.zener.config;

import java.lang.annotation.*;

/**
 * Used to signal that a certain field in the configuration-
 * class should be ignored and should not be injected
 * by the Injector.
 * Note that this only needs to be present
 * for methods that are {@code public} and {@code static}.
 * Every method that does not have this identifier will already
 * be ignored
 */

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {

}
