package com.github.schottky.zener.api;

import com.github.schottky.zener.util.messaging.Console;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public interface API {

    void start(JavaPlugin plugin);

    void end();

    JavaPlugin providingPlugin();

    boolean isRunning();

    default NamespacedKey key(String ident) {
        return new NamespacedKey(providingPlugin(), ident);
    }


    class Impl implements API {

        private JavaPlugin plugin;

        @Override
        public void start(JavaPlugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public void end() {
            this.plugin = null;
        }

        @Override
        public JavaPlugin providingPlugin() {
            if (plugin == null) {
                Console.severe("Plugin-dependent functionality called without providing plugin");
                Console.severe("Fix this by calling 'API#start(JavaPlugin)' in your 'onEnable'-method");
                throw new RuntimeException();
            } else {
                return plugin;
            }
        }

        @Override
        public boolean isRunning() {
            return plugin != null;
        }
    }
}
