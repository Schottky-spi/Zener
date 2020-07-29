package com.github.schottky.zener.api;


import com.github.schottky.zener.messaging.Console;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public interface ZenerAPI {

    void start(@NotNull JavaPlugin plugin);

    void end();

    JavaPlugin providingPlugin();

    boolean isRunning();

    NamespacedKey key(String ident);

    @API(status = Status.INTERNAL)
    class Impl implements ZenerAPI {

        private JavaPlugin plugin;

        @Override
        public void start(@NotNull JavaPlugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public void end() {
            this.plugin = null;
        }

        @Override
        public JavaPlugin providingPlugin() {
            checkPluginDependentFunctionality();
            return plugin;
        }

        public void checkPluginDependentFunctionality() {
            if (plugin == null) {
                Console.severe("Plugin-dependent functionality called without providing plugin");
                Console.severe("Fix this by calling 'API#start(JavaPlugin)' in your 'onEnable'-method");
                throw new RuntimeException();
            }
        }

        @Override
        public boolean isRunning() {
            return plugin != null;
        }

        @Override
        public NamespacedKey key(String ident) {
            checkPluginDependentFunctionality();
            return new NamespacedKey(plugin, ident);
        }
    }
}
