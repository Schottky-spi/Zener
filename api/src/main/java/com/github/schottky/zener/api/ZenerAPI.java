package com.github.schottky.zener.api;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main entry-point for version and plugin-dependant functionality
 * and wrapper for the {@link API}.
 * Typically, in the on-enable method of the main-plugin,
 * the {@link #start(JavaPlugin)}-method should be called
 * and in the on-disable method of the main-plugin,
 * the {@link #end()}-method should be called
 */

public final class ZenerAPI {

    private ZenerAPI() {}

    private static final API api = new API.Impl();

    /**
     * Returns the API-Object for this version
     * @return The API
     */
    public static API api() { return api; }

    /**
     * Start all relevant tasks that are related to a plugin.
     * If this method is not called, functionality such as
     * registering commands (in the zener-command module) or setting
     * an item-storage (in the zener-util module)
     * will not work and throw a {@code NullPointerException}
     * @param plugin The plugin that this API should use
     */

    public static void start(JavaPlugin plugin) { api.start(plugin); }

    /**
     * The endpoint in the lifecycle of the API. After a call of this,
     * All plugin-dependent functions will no longer work.
     * <br>This should be called inside {@link JavaPlugin#onDisable()}
     * to safely disable this plugin and enable any clean-up work.
     * Currently, it is not necessary to call this method, however this
     * could be the case in the future
     */

    public static void end() { api.end(); }

    /**
     * Returns the plugin that this API is currently working for
     * @return The plugin
     */

    public static JavaPlugin providingPlugin() { return api.providingPlugin(); }

    /**
     * return if the API is enabled. This is {@code true}, if
     * {@link #start(JavaPlugin)} has been called and is {@code false}
     * per default or when the {@link #end()} method has been called
     * @return If the API is running or not
     */

    public static boolean isEnabled() { return api.isRunning(); }

    /**
     * returns the {@code NameSpacedKey} for a given {@code String} and
     * can be used as a short-hand expression for
     * <pre>{@code
     * NamespacedKey key = new NamespacedKey(JavaPlugin.getInstance("somePlugin"), "identifier");
     * }</pre>
     * @param ident the identifier that this namespaced-key has
     * @return The NamespacedKey
     */

    public static NamespacedKey key(String ident) { return api.key(ident); }
}
