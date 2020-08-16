package com.github.schottky.zener.menu.scrolling;

import com.github.schottky.zener.menu.Menu;
import com.github.schottky.zener.menu.TestMenuItem;
import com.github.schottky.zener.menu.item.MenuItem;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ResizingMenuTest {

    MockResizingMenu dut;

    @BeforeEach
    public void setup() {
        dut = new MockResizingMenu(Menu.MAX_ROWS, "Resizing-menu test");
    }

    @Nested class A_menu_will_grow_in_size {

        @ParameterizedTest
        @CsvSource({"0, 0", "1, 1", "2, 3", "3, 2", "4, 5", "10, 20"})
        public void when_items_are_added_for_the_first_time(int x, int y) {
            dut.put(x, y, new TestMenuItem());
            assertEquals(x + 1, dut.totalSizeX());
            assertEquals(y + 1, dut.totalSizeY());
        }

        @ParameterizedTest
        @CsvSource({"1, 11", "11, 1", "11, 11", "13, 20", "45, 30"})
        public void when_items_are_added_that_have_greater_indices_than_the_menu_size(int x, int y) {
            dut.put(10, 10, new TestMenuItem());
            dut.put(x, y, new TestMenuItem());
            if (x > 10) assertEquals(x + 1, dut.totalSizeX());
            if (y > 10) assertEquals(y + 1, dut.totalSizeY());
        }
    }

    @Nested class A_menu_will_shrink {

        @BeforeEach
        public void setup() {
            dut.put(0, 0, new TestMenuItem());
            dut.put(2, 5, new TestMenuItem());
            dut.put(4, 3, new TestMenuItem());
        }

        @Test
        public void when_the_biggest_item_in_a_row_gets_removed() {
            dut.remove(2, 5);
            assertEquals(5, dut.totalSizeX());
            assertEquals(4, dut.totalSizeY());
        }

        @Test
        public void when_the_biggest_item_in_a_column_gets_removed() {
            dut.remove(4, 3);
            assertEquals(3, dut.totalSizeX());
            assertEquals(6, dut.totalSizeY());
        }

    }

    @Test
    public void when_a_menu_grows_big_enough_axis_will_be_added() {
        assertFalse(dut.scrollAxisHorizontal);
        assertFalse(dut.scrollAxisVertical);
        dut.put(9, 1, new TestMenuItem());
        assertTrue(dut.scrollAxisHorizontal);
        assertFalse(dut.scrollAxisVertical);
        dut.put(0, 5, new TestMenuItem());
        assertTrue(dut.scrollAxisVertical);
        assertTrue(dut.scrollAxisHorizontal);
    }

    @Test
    public void when_a_menu_shrinks_axis_will_be_removed() {
        dut.put(10, 1, new TestMenuItem());
        dut.put(2, 7, new TestMenuItem());
        assertTrue(dut.scrollAxisHorizontal);
        assertTrue(dut.scrollAxisVertical);
        dut.remove(10, 1);
        assertFalse(dut.scrollAxisHorizontal);
        assertTrue(dut.scrollAxisVertical);
        dut.remove(2, 7);
        assertFalse(dut.scrollAxisHorizontal);
        assertFalse(dut.scrollAxisVertical);
    }


    static class MockResizingMenu extends ResizingMenu {

        boolean scrollAxisHorizontal = false;
        boolean scrollAxisVertical = false;

        public MockResizingMenu(int rows, @NotNull String title) {
            super(rows, title);
        }

        public MockResizingMenu(int rows, @NotNull String title, MenuItem[][] initialItems) {
            super(rows, title, initialItems);
        }

        @Override
        protected void updateScrollWheels() {
            scrollAxisVertical = hasAxis(Axis.VERTICAL);
            scrollAxisHorizontal = hasAxis(Axis.HORIZONTAL);
        }
    }
}