package com.github.schottky.command.mock;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collections;
import java.util.Set;

public class MockPluginManager implements PluginManager {
    @Override
    public void registerInterface(@NotNull Class<? extends PluginLoader> loader) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Plugin getPlugin(@NotNull String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Plugin[] getPlugins() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPluginEnabled(@NotNull String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPluginEnabled(@Nullable Plugin plugin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Plugin loadPlugin(@NotNull File file) throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Plugin[] loadPlugins(@NotNull File directory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void disablePlugins() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearPlugins() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void callEvent(@NotNull Event event) throws IllegalStateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void registerEvents(@NotNull Listener listener, @NotNull Plugin plugin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void registerEvent(@NotNull Class<? extends Event> event, @NotNull Listener listener, @NotNull EventPriority priority, @NotNull EventExecutor executor, @NotNull Plugin plugin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void registerEvent(@NotNull Class<? extends Event> event, @NotNull Listener listener, @NotNull EventPriority priority, @NotNull EventExecutor executor, @NotNull Plugin plugin, boolean ignoreCancelled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void enablePlugin(@NotNull Plugin plugin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void disablePlugin(@NotNull Plugin plugin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Permission getPermission(@NotNull String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addPermission(@NotNull Permission perm) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removePermission(@NotNull Permission perm) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removePermission(@NotNull String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Set<Permission> getDefaultPermissions(boolean op) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void recalculatePermissionDefaults(@NotNull Permission perm) {
        // do nothing
    }

    @Override
    public void subscribeToPermission(@NotNull String permission, @NotNull Permissible permissible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unsubscribeFromPermission(@NotNull String permission, @NotNull Permissible permissible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Set<Permissible> getPermissionSubscriptions(@NotNull String permission) {
        return Collections.emptySet();
    }

    @Override
    public void subscribeToDefaultPerms(boolean op, @NotNull Permissible permissible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unsubscribeFromDefaultPerms(boolean op, @NotNull Permissible permissible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Set<Permissible> getDefaultPermSubscriptions(boolean op) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Set<Permission> getPermissions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean useTimings() {
        throw new UnsupportedOperationException();
    }
}
