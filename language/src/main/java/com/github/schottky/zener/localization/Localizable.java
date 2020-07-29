package com.github.schottky.zener.localization;

import org.apiguardian.api.API;

/**
 * represents a class that may have localizable objects
 */
@API(status = API.Status.EXPERIMENTAL)
public interface Localizable {

    /**
     * the identifier to translate
     * @return The identifier to translate
     */
    String identifier();
}
