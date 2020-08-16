package com.github.schottky.zener.config;

import com.github.schottky.zener.config.bind.Convertible;

import java.lang.annotation.*;

/**
 * Marks that a certain field should be converted using the
 * given converter
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConvertWith {

    Class<? extends Convertible<?>> value();
}
