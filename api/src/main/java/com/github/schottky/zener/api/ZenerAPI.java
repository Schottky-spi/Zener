package com.github.schottky.zener.api;


import com.github.schottky.zener.messaging.Console;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public interface ZenerAPI {

    void start(@NotNull JavaPlugin plugin);

    void end();

    JavaPlugin providingPlugin();

    boolean isRunning();

    NamespacedKey key(String ident);

    @API(status = Status.INTERNAL)
    class Impl implements ZenerAPI {

        private JavaPlugin plugin;
        private final Set<SubModule> subModules = new HashSet<>();

        @Override
        public void start(@NotNull JavaPlugin plugin) {
            this.plugin = plugin;
            this.initializeSubModules();
        }

        @Override
        public void end() {
            subModules.forEach(SubModule::shutdown);
            subModules.clear();
            this.plugin = null;
        }

        @Override
        public JavaPlugin providingPlugin() {
            checkPluginDependentFunctionality();
            return plugin;
        }

        @SuppressWarnings("unchecked")
        private void initializeSubModules() {
            try {
                String packageName = getClass().getPackage().getName();
                packageName = packageName.substring(0, packageName.length() - "api".length());
                // get the class named "com.github.schottky.zener.menu.MenuBootstrap"
                // since the name can (and should) change via maven-relocation,
                // use this package as a default starting-point
                final Class<SubModule> clazz = (Class<SubModule>) Class.forName(packageName + "menu.MenuSubModule");
                final SubModule bootstrap = clazz.newInstance();
                subModules.add(bootstrap);
                bootstrap.init();
            } catch (ClassNotFoundException ignored) {
                // ignore this exception; The module is not present
            } catch (IllegalAccessException | InstantiationException e) {
                // IllegalAccessException: marks an illegal implementation
                // of the bootstrap-interface
                e.printStackTrace();
            }
        }

        private void checkPluginDependentFunctionality() {
            if (plugin == null) {
                Console.severe("Plugin-dependent functionality called without providing plugin");
                Console.severe("Fix this by calling 'API#start(JavaPlugin)' in your 'onEnable'-method");
                throw new Error();
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
