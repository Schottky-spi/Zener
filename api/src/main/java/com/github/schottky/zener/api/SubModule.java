package com.github.schottky.zener.api;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Provides mechanisms to initialize a sub-module
 * in a known and shared environment.
 * This interface is to be used internally only.
 * Classes that implement this interface may only
 * introduce a public constructor that takes no arguments
 */
@API(status = Status.INTERNAL)
public interface SubModule {

    /**
     * initialized the module.
     * At this point, it can be assumed that
     * the Zener knows about the plugin
     */
    void init();

    /**
     * shut down the module
     */
    void shutdown();
}
