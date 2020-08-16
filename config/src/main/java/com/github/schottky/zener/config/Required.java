package com.github.schottky.zener.config;

import java.lang.annotation.*;

/**
 * Indicates that a certain value needs to be present in the
 * config. If the value is not present, the config will throw a
 * {@link MissingConfigEntry} during the de-serialization process
 */

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Required {
}
