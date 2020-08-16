package com.github.schottky.zener.config;

import com.google.common.collect.Sets;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    Config config;

    @BeforeEach
    public void setup() {
        final Reader reader = new InputStreamReader(getClass().getResourceAsStream("/config.yml"));
        this.config = new Config(ExampleOptions.class, YamlConfiguration.loadConfiguration(reader));
    }

    @Test
    public void a_single_config_value_gets_injected() throws MissingConfigEntry{
        final Field field = fieldForName("enable");
        config.injectInto(field);
        assertTrue(ExampleOptions.enable);
    }

    @Test
    public void an_ignored_value_is_not_injected() throws MissingConfigEntry {
        final Field field = fieldForName("x");
        config.injectInto(field);
        assertNull(ExampleOptions.x);
    }

    @Test
    public void a_field_using_a_custom_type_adapter_is_injected() throws MissingConfigEntry {
        config.injectInto(fieldForName("person"));
        assertEquals(new ExampleOptions.Person("Peter", "Zumthor"), ExampleOptions.person);
    }

    @Test
    public void a_field_with_custom_path_is_injected() throws MissingConfigEntry {
        config.injectInto(fieldForName("anInt"));
        assertEquals(23.0, ExampleOptions.anInt);
    }

    @Test
    public void a_field_that_is_configuration_serializable_is_injected() throws MissingConfigEntry {
        config.injectInto(fieldForName("vector"));
        assertEquals(new Vector(1, 2, 3), ExampleOptions.vector);
    }

    @Test
    public void a_required_field_throws_when_not_present() {
        assertThrows(MissingConfigEntry.class, () -> config.injectInto(fieldForName("forteTwo")));
        assertDoesNotThrow(() -> config.injectInto(fieldForName("forteOne")));
    }

    @Test
    public void an_enum_can_be_converted_using_the_enum_converter() throws MissingConfigEntry {
        config.injectInto(fieldForName("material"));
        assertEquals(Material.GOLDEN_APPLE, ExampleOptions.material);
    }

    @Test
    public void a_list_can_be_converted() throws MissingConfigEntry {
        config.injectInto(fieldForName("list"));
        assertEquals(Sets.newHashSet("first", "second", "nth"), ExampleOptions.list);
    }

    private Field fieldForName(String name) {
        try {
            return config.configClass().getField(name);
        } catch (NoSuchFieldException e) {
            throw new Error(e);
        }
    }
}