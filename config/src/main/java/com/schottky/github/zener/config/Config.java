package com.schottky.github.zener.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;

public class Config {

    private final Class<?> configClass;
    private final FileConfiguration configuration;

    public Config(Class<?> configClass, FileConfiguration configuration) {
        this.configClass = configClass;
        this.configuration = configuration;
    }

    public void reload() {

    }

    private void injectAll() {
        for (Field field: configClass.getFields()) {

        }
    }
}
