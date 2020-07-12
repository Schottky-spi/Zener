package com.github.schottky.zener.api;

import org.bukkit.plugin.Plugin;

public interface API {

    void start(Plugin plugin);

    void end();

    Plugin providingPlugin();

    class Impl implements API {

        private Plugin plugin;

        @Override
        public void start(Plugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public void end() {
            this.plugin = null;
        }

        @Override
        public Plugin providingPlugin() {
            return plugin;
        }
    }
}
