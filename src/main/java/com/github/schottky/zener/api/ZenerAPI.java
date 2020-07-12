package com.github.schottky.zener.api;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class ZenerAPI {

    private static final API api = new API.Impl();

    public static API api() { return api; }

    public static void start(JavaPlugin plugin) { api.start(plugin); }

    public static void end() { api.end(); }

    public static JavaPlugin providingPlugin() { return api.providingPlugin(); }

    public static boolean isEnabled() { return api.isRunning(); }

    public static NamespacedKey key(String ident) { return api.key(ident); }
}
