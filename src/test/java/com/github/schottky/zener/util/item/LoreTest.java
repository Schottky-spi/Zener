package com.github.schottky.zener.util.item;

import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoreTest {

    @Nested class A_new_lore {

        @Test
        void contains_its_initial_elements() {
            List<String> expected = Arrays.asList("Hello", "World");
            assertEquals(expected, new Lore(false, "Hello", "World"));

            Iterable<String> contents = Arrays.asList("Hello", "World");
            assertEquals(expected, new Lore(false, contents));
        }

        @Test
        void is_empty_when_meta_is_null() {
            assertEquals(Collections.emptyList(), Lore.of((ItemMeta) null));
        }
    }

    @Test
    void removes_a_valid_range() {
        Lore initial = new Lore(false, "foo", "bar", "Hello", "World");

        Lore next = initial.duplicate();
        next.removeRange(0, 2);
        assertEquals(Arrays.asList("Hello", "World"), next);

        next = initial.duplicate();
        next.removeRange(1, 3);
        assertEquals(Arrays.asList("foo", "World"), next);
    }
}