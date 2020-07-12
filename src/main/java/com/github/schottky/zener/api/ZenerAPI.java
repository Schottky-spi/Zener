package com.github.schottky.zener.api;

import org.bukkit.plugin.Plugin;

public class ZenerAPI {

    private static final API api = new API.Impl();

    public static void start(Plugin plugin) {
        api.start(plugin);
    }

    public static void end() {
        api.end();
    }

    public static Plugin providingPlugin() {
        return api.providingPlugin();
    }
}
