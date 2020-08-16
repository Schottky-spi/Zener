package com.github.schottky.zener.menu.scrolling;

import com.github.schottky.zener.menu.Menu;
import com.github.schottky.zener.menu.MockTrigger;
import com.github.schottky.zener.menu.TestMenuItem;
import com.github.schottky.zener.menu.item.MenuItem;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractScrollingMenuTest {

    AbstractScrollingMenu dut;

    @Nested class A_menu_cannot_be_scrolled_through {

        @Test
        public void when_it_is_empty() {
            dut = new MockScrollingMenu(new MenuItem[0][0]);
            assertDoesNotScroll();
        }

        @ParameterizedTest
        @CsvSource({"1, 1", "4, 5", "3, 4"})
        public void when_it_is_smaller_than_the_critical_size(int sizeX, int sizeY) {
            dut = new MockScrollingMenu(new MenuItem[sizeX][sizeY]);
            assertDoesNotScroll();
        }

        @Test
        public void when_it_is_exactly_the_critical_size() {
            dut = new MockScrollingMenu(new MenuItem[8][5]);
            assertDoesNotScroll();
        }

        @Test
        public void in_negative_direction_when_at_origin() {
            dut = new MockScrollingMenu(new MenuItem[10][20]);
            assertFalse(dut.scrollUp());
            assertFalse(dut.scrollLeft());
        }

        @ParameterizedTest
        @CsvSource({"9, 1", "15, 7"})
        public void to_the_right_when_it_is_at_the_end(int sizeX, int modX) {
            dut = new MockScrollingMenu(new MenuItem[sizeX][0]);
            assertTrue(dut.scroll(ScrollingMenu.Axis.HORIZONTAL, modX));
            assertFalse(dut.scrollRight());
        }

        @ParameterizedTest
        @CsvSource({"2, 3, 2", "2, 4, 3", "4, 5, 2", "4, 10, 7", "6, 7, 2", "6, 32, 27"})
        public void down_when_it_is_at_the_end(int rows, int sizeY, int modY) {
            dut = new MockScrollingMenu(rows, new MenuItem[1][sizeY]);
            assertTrue(dut.scroll(ScrollingMenu.Axis.VERTICAL, modY));
            assertFalse(dut.scrollDown());
        }

        private void assertDoesNotScroll() {
            assertFalse(dut.scrollDown(), "Scrolls down");
            assertFalse(dut.scrollUp(), "Scrolls up");
            assertFalse(dut.scrollLeft(), "Scrolls left");
            assertFalse(dut.scrollRight(), "Scrolls right");
        }
    }

    @Nested class A_menu_can_be_scrolled_to_the_right {

        @ParameterizedTest
        @ValueSource(ints = {9, 10, 20})
        public void when_it_is_greater_than_the_critical_size_and_at_origin(int sizeX) {
            dut = new MockScrollingMenu(new MenuItem[sizeX][0]);
            assertTrue(dut.scrollRight());
        }

        @ParameterizedTest
        @CsvSource({"10, 1", "11, 1", "11, 2", "20, 6"})
        public void when_it_is_greater_than_the_critical_size_and_not_at_the_end(int sizeX, int scrollTo) {
            dut = new MockScrollingMenu(new MenuItem[sizeX][0]);
            assertTrue(dut.scroll(ScrollingMenu.Axis.HORIZONTAL, scrollTo));
            assertTrue(dut.scrollRight());
        }
    }

    @Nested class A_menu_can_be_scrolled_downwards {

        @ParameterizedTest
        @ValueSource(ints = {9, 10, 20})
        public void when_it_is_greater_than_the_critical_size_and_at_origin(int sizeY) {
            dut = new MockScrollingMenu(new MenuItem[1][sizeY]);
            assertTrue(dut.scrollDown());
        }

        @ParameterizedTest
        @CsvSource({"10, 1", "11, 1", "11, 2", "20, 6"})
        public void when_it_is_greater_than_the_critical_size_and_not_at_the_end(int sizeY, int scrollTo) {
            dut = new MockScrollingMenu(new MenuItem[1][sizeY]);
            assertTrue(dut.scroll(ScrollingMenu.Axis.VERTICAL, scrollTo));
            assertTrue(dut.scrollDown());
        }
    }

    @Nested class when_a_menu_is_being_scrolled {

        @Test
        public void the_click_slot_changes_in_x_direction() {
            final MenuItem[][] menuItems = new MenuItem[10][1];
            final TestMenuItem testItem = new TestMenuItem();
            menuItems[1][0] = testItem;
            dut = new MockScrollingMenu(menuItems);
            dut.clickSlot(1, 0, MockTrigger.mockEvent(1, dut));
            assertTrue(testItem.clicked);

            testItem.reset();
            dut.scrollRight();
            dut.clickSlot(0, 0, MockTrigger.mockEvent(0, dut));
            assertTrue(testItem.clicked);
        }

        @Test
        public void the_click_slot_changes_in_y_direction() {
            final MenuItem[][] menuItems = new MenuItem[1][10];
            final TestMenuItem testItem = new TestMenuItem();
            menuItems[0][1] = testItem;
            dut = new MockScrollingMenu(menuItems);
            dut.clickSlot(0, 1, MockTrigger.mockEvent(9, dut));
            assertTrue(testItem.clicked);

            testItem.reset();
            dut.scrollDown();
            dut.clickSlot(0, 0, MockTrigger.mockEvent(0, dut));
            assertTrue(testItem.clicked);
        }
    }

    static class MockScrollingMenu extends AbstractScrollingMenu {

        public MockScrollingMenu(MenuItem[][] items) {
            this(Menu.MAX_ROWS, items);
        }

        public MockScrollingMenu(int rows, MenuItem[][] items) {
            super(rows, "Scroll-menu test", EnumSet.allOf(Axis.class));
            this.items = items;
            this.sync();
        }

        @Override
        protected void updateScrollWheels() { /* prevent item creation */ }

        @Override
        protected int totalSizeX() {
            return items.length;
        }

        @Override
        protected int totalSizeY() {
            return items.length == 0 ? 0 : items[0].length;
        }

        private final MenuItem[][] items;
        @Override
        protected MenuItem itemAt(int x, int y) {
            return items[x][y];
        }
    }

}