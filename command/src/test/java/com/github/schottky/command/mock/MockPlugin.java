package com.github.schottky.command.mock;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

public class MockPlugin extends PluginBase {
    @Override
    public @NotNull File getDataFolder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull PluginDescriptionFile getDescription() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull FileConfiguration getConfig() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable InputStream getResource(@NotNull String filename) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveConfig() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveDefaultConfig() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveResource(@NotNull String resourcePath, boolean replace) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reloadConfig() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull PluginLoader getPluginLoader() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Server getServer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onDisable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onLoad() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEnable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isNaggable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNaggable(boolean canNag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Logger getLogger() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        throw new UnsupportedOperationException();
    }
}
