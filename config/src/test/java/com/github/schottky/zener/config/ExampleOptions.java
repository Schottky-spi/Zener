package com.github.schottky.zener.config;

import com.schottky.github.zener.config.Path;
import com.schottky.github.zener.config.Required;
import com.schottky.github.zener.config.TypeAdapter;
import org.bukkit.configuration.ConfigurationSection;

public class ExampleOptions {

    private static TypeAdapter<?,?>[] typeAdapters = new TypeAdapter<?,?>[] {new TypeAdapter<ConfigurationSection, Object>() {
        @Override
        public Class<ConfigurationSection> optionsType() {
            return ConfigurationSection.class;
        }

        @Override
        public Object toOptionsType(Object configValue) {
            return null;
        }
    }};

    @Path("some.boolean.value")
    public static boolean aBoolean;

    @Required
    public static Person person;

    public static int notRequired;

    public static class Person {

        private String name;
        private String surname;
    }
}
