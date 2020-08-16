package com.github.schottky.zener.menu.paged;

import com.github.schottky.zener.menu.Menu;
import com.github.schottky.zener.menu.MockTrigger;
import com.github.schottky.zener.menu.TestMenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class AbstractModifiablePagedMenuTest {

    private ModifiablePagedMenu dut;
    private TestMenuItem testItem;

    @BeforeEach
    public void setup() {
        dut = new MockModifiablePagedMenu();
        testItem = new TestMenuItem();
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 5, 10})
    public void the_inventory_will_grow_when_an_item_is_added(int page) {
        assertTrue(dut.pageCount() < page);
        dut.put(page, 0, 0, new TestMenuItem());
        assertEquals(page, dut.pageCount());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 10})
    public void the_inventory_will_shrink_when_a_page_is_removed(int page) {
        for (int i = 1; i < 10; i++) dut.addEmptyPage();
        assertEquals(10, dut.pageCount());
        dut.removePage(page);
        assertEquals(9, dut.pageCount());
    }

    @Test
    public void an_item_can_no_longer_be_clicked_when_its_removed() {
        dut.put(1, 0, 0, testItem);
        dut.remove(1, 0, 0);
        dut.clickSlot(0, MockTrigger.mockEvent(0, dut));
        assertFalse(testItem.clicked);
    }

    @Test
    public void an_item_can_no_longer_be_clicked_when_its_page_gets_removed() {
        dut.addEmptyPage();
        dut.put(2, 0, 0, testItem);
        dut.navigateToPage(2);
        assertEquals(2, dut.currentPage());
        dut.removePage(2);
        dut.clickSlot(0, MockTrigger.mockEvent(0, dut));
        assertFalse(testItem.clicked);
        assertEquals(1, dut.currentPage());
    }

    @ParameterizedTest
    @CsvSource({"8, 7, 9", "8, 8, 9", "8, 1, 9", "6, 4, 9"})
    public void the_current_page_updates_when_a_page_smaller_than_it_gets_removed(int oldPage, int pageToRemove, int pageCount) {
        dut.addEmptyPages(pageCount);
        dut.navigateToPage(oldPage);
        dut.removePage(pageToRemove);
        assertEquals(oldPage - 1, dut.currentPage());
    }

    @Test
    public void the_first_page_can_be_removed() {
        dut.addEmptyPages(1);
        dut.removePage(1);
        assertEquals(1, dut.currentPage());
    }

    @ParameterizedTest
    @CsvSource({"7, 8, 9", "1, 8, 9", "2, 5, 8", "1, 2, 2"})
    public void the_current_page_does_not_update_when_a_page_greater_than_it_gets_removed(int oldPage, int pageToRemove, int pageCount) {
        dut.addEmptyPages(9);
        dut.navigateToPage(oldPage);
        dut.removePage(pageToRemove);
        assertEquals(oldPage, dut.currentPage());
    }

    static class MockModifiablePagedMenu extends AbstractModifiablePagedMenu {

        public MockModifiablePagedMenu() {
            super(Menu.MAX_ROWS, "Test modifiable menu");
        }

        @Override
        protected void updateNavigatorBar() { /* do nothing to prevent items from being added during sync */ }
    }

}