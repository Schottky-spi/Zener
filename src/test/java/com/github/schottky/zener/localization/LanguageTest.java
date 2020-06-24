package com.github.schottky.zener.localization;

import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LanguageTest {

    @NotNull
    @Contract(" -> new")
    private static Language us() {
        return new Language(rawMap(), Locale.US);
    }

    @NotNull
    private static Map<String,String> rawMap() {
        Map<String, String> rawMap = new HashMap<>();
        rawMap.put("some.identifier", "identifier 1");
        rawMap.put("some.other.identifier", "{identifier} 2");
        rawMap.put("yet.another.identifier", "identifier 3");
        rawMap.put("identifier.with.replacements", "{greeting}, {what}!");
        return rawMap;
    }

    @Nested
    class An_empty_language {

        Language emptyLang = new Language(Collections.emptyMap(), Locale.US);

        @Test
        void always_returns_the_identifier_or_default_value() {
            assertEquals(emptyLang.translate("some.random.identifier"), "some.random.identifier");
            Language.addDefaults("empty.default", "default for empty");
            assertEquals("default for empty", emptyLang.translate("empty.default"));
        }

        @Test
        void returns_its_locale() {
            assertEquals(Locale.US, emptyLang.locale());
        }

    }

    @Nested
    class An_identifier_is_correct {

        @Test
        void if_it_consists_of_lowercase_letters_and_underscores() {
            assertTrue(Language.isValidIdentifier("some"));
            assertTrue(Language.isValidIdentifier("__som__e_"));
        }

        @Test
        void if_it_contains_dot_separated_identifiers() {
            assertTrue(Language.isValidIdentifier("some.random.identifier"),"legal sequence");
            assertTrue(Language.isValidIdentifier("_some_.random.ident_ifier"),"legal sequence with under-bars");
        }
    }

    @Nested
    class An_identifier_is_incorrect {

        @Test
        void if_it_is_empty() {
            assertFalse(Language.isValidIdentifier(""));
        }

        @Test
        void if_it_contains_illegal_characters() {
            assertFalse(Language.isValidIdentifier("some.Identifier"));
            assertFalse(Language.isValidIdentifier("some.identifier&"));
            assertFalse(Language.isValidIdentifier("some-identifier"));
        }

        @Test
        void if_a_dot_is_at_the_start_or_end() {
            assertFalse(Language.isValidIdentifier(".some.identifier"));
            assertFalse(Language.isValidIdentifier("some.identifier."));
        }

        @Test
        void if_there_are_two_ore_more_dots_in_sequence() {
            assertFalse(Language.isValidIdentifier("some..identifier"));
            assertFalse(Language.isValidIdentifier("some.other...identifier"));
        }
    }

    @Nested
    class A_correct_identifier_translates_to {

        @Test
        void the_associated_value() {
            assertEquals("identifier 1", us().translate("some.identifier"));
            assertEquals( "{identifier} 2", us().translate("some.other.identifier"));
            assertEquals("identifier 3", us().translate("yet.another.identifier"));
        }

        @Test
        void the_associated_value_with_color_codes() {
            Language someLang = new Language(Collections.singletonMap("some.identifier", "&cColor&fmatic!"), Locale.US);
            char color = ChatColor.COLOR_CHAR;
            assertEquals(color + "cColor" + color + "fmatic!", someLang.translate("some.identifier"));
        }

        @Test
        void the_associated_value_with_an_extra_value() {
            assertEquals("Replaced 2", us().translateWithExtra("some.other.identifier",
                            "identifier", "Replaced"));
        }

        @Test
        void the_associated_value_with_several_extra_values() {
            assertEquals("Hello, World!", us().translateWithExtra("identifier.with.replacements",
                    "greeting", "Hello",
                    "what", "World"),
                    "Identifier with two replacements");
        }

        @Test
        void the_identifier_itself_or_the_default_value_if_no_mapping_exists() {
            assertEquals("identifier.missing", us().translate("identifier.missing"));
            Language.addDefaults("identifier.missing", "now present");
            assertEquals("now present", us().translate("identifier.missing"));
        }
    }

    @Nested
    class An_incorrect_identifier {

        @Test
        void throws_when_translated() {
            assertThrows(RuntimeException.class, () -> us().translate("incorrect.Identifier"));
        }
    }

    // TODO: refactor to readability
    public static class Throws {

        @Test
        void on_invalid_input() {
            Map<String, String> incorrectMap = new HashMap<>();
            incorrectMap.put("some.wrong-identifier", "incorrect");

            assertThrows(RuntimeException.class, () -> new Language(incorrectMap, Locale.US));
        }
    }

    public static class Defaults {

        @Test
        void are_added_and_do_not_throw_on_retrieve() {
            Language.addDefaults(
                    "default_one", "default value 1",
                    "default_two", "default value 2",
                    "other.value", "default value 3");
            assertDoesNotThrow(() -> {
                Language.getDefault("default_one");
                Language.getDefault("default_two");
                Language.getDefault("other.value");
            });
        }

        @Test
        void throw_if_mapping_is_not_present() {
            assertThrows(RuntimeException.class, () -> Language.getDefault("not_existing"));
        }

        @Test
        void will_be_chosen_if_a_mapping_is_missing_for_an_existing_language() {
            Language.addDefaults("missing.mapping", "default");
            assertEquals(us().translate("missing.mapping"), "default", "default mapping");
        }
    }

    @Nested
    class A_language_can_be_created {

        @Test
        void from_a_json_object() {
            JsonObject object = new JsonObject();
            rawMap().forEach(object::addProperty);
            assertEquals(us(), Language.fromJson(object, Locale.US));
        }

        @Test
        void from_a_configuration_section() {
            ConfigurationSection section = new YamlConfiguration();
            rawMap().forEach(section::set);
            assertEquals(us(), Language.fromConfigurationSection(section, Locale.US));
        }

        @Test
        void from_a_reader() {
            Reader jsonReader = new StringReader("{\"some.identifier\": \"Value 1\"}");
            Reader yamlReader = new StringReader("some.identifier: 'Value 1'");
            Language expected = new Language(Collections.singletonMap("some.identifier", "Value 1"), Locale.US);
            assertEquals(expected, Language.readFrom(jsonReader, Locale.US, LanguageFile.StorageProvider.JSON));
            assertEquals(expected, Language.readFrom(yamlReader, Locale.US, LanguageFile.StorageProvider.YAML));
        }
    }

    @Nested
    class A_language_can_be_stored_to {

        Language language = new Language(Collections.singletonMap("some.identifier", "Value 1"), Locale.US);

        @Test
        void a_json_object() {
            JsonObject expected = new JsonObject();
            expected.addProperty("some.identifier", "Value 1");
            assertEquals(expected, language.toJson());
        }

        @Test
        void a_yaml_configuration() {
            YamlConfiguration expected = new YamlConfiguration();
            expected.set("some.identifier", "Value 1");
            // YAML-configurations can only be compared by string
            assertEquals(expected.saveToString(), language.toYamlConfiguration().saveToString());
        }
    }

    @Nested
    class A_language_cannot_be_created {

        @Test
        void if_the_passed_raw_map_contains_invalid_identifiers() {
            assertThrows(RuntimeException.class, () -> new Language(Collections.singletonMap("invalid-identifier", "foo"), Locale.US));
        }
    }

    @Nested
    class A_language_can_be_loaded {

        @Test
        void from_a_file() throws InvalidLanguageFile, FileNotFoundException {
            System.out.println();
            assertEquals(us(), Language.fromFile(getResource("en-us.lang")));
            assertEquals(new Language(rawMap(), Locale.GERMAN),
                    Language.fromFile(getResource("de.yaml")));
        }

        @Nullable
        @Contract("_ -> new")
        private File getResource(String name) {
            URL url = getClass().getClassLoader().getResource(name);
            if (url != null) return new File(url.getFile());
            return null;
        }
    }

}