package com.github.schottky.zener.util.item;

import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class LoreTest {

    @Nested class A_new_lore {

        @Test
        void is_empty_when_meta_is_null() {
            assertEquals(Collections.emptyList(), Lore.of((ItemMeta) null));
        }

        @Test
        void appends_the_reset_string_when_prompted() {
            String reset = ChatColor.RESET.toString();
            Lore lore = new Lore("hello", "world").thatResetsAtStart();
            assertEquals(Arrays.asList(reset + "hello", reset + "world"), lore);
        }

        @Test
        void does_not_append_the_reset_string_when_prompted() {
            Lore lore = new Lore("hello", "world").thatDoesNotResetAtStart();
            assertEquals(Arrays.asList("hello", "world"), lore);
        }
    }

    @Test
    void removes_a_valid_range() {
        Lore initial = new Lore("foo", "bar", "hello", "world");

        Lore next = initial.duplicate();
        next.removeRange(0, 2);
        assertEquals(new Lore("hello", "world"), next);

        next = initial.duplicate();
        next.removeRange(1, 3);
        assertEquals(new Lore("foo", "world"), next);
    }

    @Test
    void a_duplicate_lore_is_equal_to_its_original() {
        Lore lore = new Lore("foo", "bar");
        Lore duplicate = lore.duplicate();
        assertEquals(lore.size(), duplicate.size());
        assertEquals(lore, duplicate);
        assertEquals(lore.resetsAtStart(), duplicate.resetsAtStart());
        assertNotSame(lore, duplicate);
    }


    @Test
    void removes_a_pattern() {
        Lore lore = new Lore("Sword of asparagus 1", "Sword of Cucumber", "Sword of asparagus 2");
        Pattern pattern = Pattern.compile(Pattern.quote("Sword of asparagus"));
        lore.remove(pattern);
        assertEquals(new Lore("Sword of Cucumber"), lore);
    }

    @Test
    void a_lore_does_not_append_reset_when_already_reset() {
        List<String> someList = Arrays.asList(ChatColor.RESET + "Hello", ChatColor.RESET + "World");
        Lore lore = new Lore(someList).thatResetsAtStart();
        assertIterableEquals(someList, lore);
    }

    @Nested class A_lore_adds_multiple_elements {

        @Test
        void at_an_index() {
            Lore lore = new Lore("hello", "world");
            lore.addAll(1, "foo", "bar");
            assertEquals(new Lore("hello", "foo", "bar", "world"), lore);
        }

        @Test
        void with_variable_args() {
            Lore lore = new Lore("hello", "world");
            lore.addAll("foo", "bar");
            assertEquals(new Lore("hello", "world", "foo", "bar"), lore);
        }
    }

    @Test
    public void can_be_converted_to_an_array() {
        Lore lore = new Lore("Hello", "World").thatDoesNotResetAtStart();
        assertArrayEquals(new String[] {"Hello", "World"}, lore.toArray());
        assertArrayEquals(new String[] {"Hello", "World"}, lore.toArray(new String[0]));
    }
}