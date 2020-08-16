package com.github.schottky.zener.menu.scrolling;

import com.github.schottky.zener.menu.Menu;
import com.github.schottky.zener.menu.TestMenuItem;
import com.github.schottky.zener.menu.item.MenuItem;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SingleAxisScrollingMenuTest {

    SingleAxisScrollingMenu dut;

    @Nested class A_vertical_scrolling_menu {

        @Test
        public void lays_out_items_correctly() {
            final MenuItem[] items = new MenuItem[20];
            Arrays.fill(items, new TestMenuItem());
            dut = new MockSingleScrollingAxisMenu(ScrollingMenu.Axis.VERTICAL, items);
            assertEquals(8, dut.totalSizeX());
            assertEquals(3, dut.totalSizeY());
        }
    }

    @Nested class A_horizontal_scrolling_menu {

        @Test
        public void lays_out_items_correctly() {
            final MenuItem[] items = new MenuItem[20];
            Arrays.fill(items, new TestMenuItem());
            dut = new MockSingleScrollingAxisMenu(ScrollingMenu.Axis.HORIZONTAL, items);
            assertEquals(4, dut.totalSizeX());
            assertEquals(5, dut.totalSizeY());
        }
    }

    static class MockSingleScrollingAxisMenu extends SingleAxisScrollingMenu {

        public MockSingleScrollingAxisMenu(Axis axis, MenuItem[] initialItems) {
            super(Menu.MAX_ROWS, "Mock single scrolling-axis menu", axis, initialItems);
        }
    }

}