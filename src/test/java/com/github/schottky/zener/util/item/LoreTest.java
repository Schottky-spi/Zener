package com.github.schottky.zener.util.item;

import com.github.schottky.zener.localization.Language;
import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class LoreTest {

    @BeforeAll
    public static void setup() {
        Language.addDefaults(
                "hello", "Hello",
                "world", "World",
                "foo", "foo",
                "bar", "bar");
    }

    @Nested class A_new_lore {

        @Test
        void is_empty_when_meta_is_null() {
            assertEquals(Collections.emptyList(), Lore.of((ItemMeta) null));
        }

        @Test
        void appends_the_reset_string_when_prompted() {
            String reset = ChatColor.RESET.toString();
            Lore lore = new Lore("hello", "world").thatResetsAtStart();
            assertEquals(Arrays.asList(reset + "Hello", reset + "World"), lore);
        }

        @Test
        void does_not_append_the_reset_string_when_prompted() {
            Lore lore = new Lore("hello", "world").thatDoesNotResetAtStart();
            assertEquals(Arrays.asList("Hello", "World"), lore);
        }

        @Test
        void does_not_contain_translated_elements_if_not_desired() {
            // ensure mappings exist that would override
            Language lang = Language.current();
            assertTrue(lang.hasMappingFor("hello"));
            assertTrue(lang.hasMappingFor("world"));
            Lore lore = Lore.newLoreWithRawElements(Arrays.asList("hello", "world")).thatDoesNotResetAtStart();
            assertIterableEquals(Arrays.asList("hello", "world"), lore);
        }
    }

    @Test
    void contains_localized_elements() {
        Lore lore = new Lore("hello", "world").thatDoesNotResetAtStart();
        assertIterableEquals(lore, Arrays.asList("Hello", "World"));
    }

    @Test
    void a_lore_removes_a_valid_range() {
        Lore initial = Lore.newLoreWithRawElements("foo", "bar", "hello", "world");

        Lore next = initial.duplicate();
        next.removeRange(0, 2);
        assertEquals(Lore.newLoreWithRawElements("hello", "world"), next);

        next = initial.duplicate();
        next.removeRange(1, 3);
        assertEquals(Lore.newLoreWithRawElements("foo", "world"), next);
    }

    @Test
    void a_duplicate_lore_is_equal_to_its_original() {
        Lore lore = Lore.newLoreWithRawElements("foo", "bar");
        Lore duplicate = lore.duplicate();
        assertEquals(lore.size(), duplicate.size());
        assertIterableEquals(lore, duplicate);
        assertEquals(lore.resetsAtStart(), duplicate.resetsAtStart());
    }


    @Test
    void removes_a_pattern() {
        Lore lore = Lore.newLoreWithRawElements("Sword of asparagus 1", "Sword of Cucumber", "Sword of asparagus 2");
        Pattern pattern = Pattern.compile(Pattern.quote("Sword of asparagus"));
        lore.remove(pattern);
        assertEquals(Lore.newLoreWithRawElements("Sword of Cucumber"), lore);
    }

    @Nested class A_lore_adds_multiple_elements {

        @Test
        void that_are_translated() {
            Lore lore = new Lore("hello");
            lore.addAll("world", "foo", "bar");
            assertEquals(lore.size(), 4);
            assertEquals(new Lore("hello", "world", "foo", "bar"), lore);
        }

        @Test
        void raw() {
            Lore lore = Lore.newLoreWithRawElements("Hello", "World");
            lore.addAllRaw("Foo", "Bar");
            assertEquals(Lore.newLoreWithRawElements("Hello", "World", "Foo", "Bar"), lore);
        }

        @Test
        void at_an_index() {
            Lore lore = new Lore("hello", "world");
            lore.addAll(1, "foo", "bar");
            assertEquals(new Lore("hello", "foo", "bar", "world"), lore);
        }

        @Test
        void at_an_index_raw() {
            Lore lore = Lore.newLoreWithRawElements("hello", "world");
            lore.addAllRaw(1, "foo", "bar");
            assertEquals(Lore.newLoreWithRawElements("hello", "foo", "bar", "world"), lore);
        }
    }
}