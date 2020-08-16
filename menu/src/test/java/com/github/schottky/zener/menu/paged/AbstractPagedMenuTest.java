package com.github.schottky.zener.menu.paged;

import com.github.schottky.zener.menu.Menu;
import com.github.schottky.zener.menu.item.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class AbstractPagedMenuTest {

    private AbstractPagedMenu dut;

    @Nested class An_empty_paged_menu {

        @BeforeEach
        public void setup_dut() {
            dut = new MockPagedMenu();
        }

        @Test
        public void has_one_single_page() {
            assertEquals(1, dut.pageCount());
        }

        @Test
        public void is_at_page_1() {
            assertEquals(1, dut.currentPage());
        }

        @Test
        public void when_navigated_to_next_page_will_stay_at_first() {
            assertFalse(dut.navigateToNextPage());
            assertEquals(1, dut.currentPage());
        }

        @Test
        public void when_navigated_to_previous_page_will_stay_at_first() {
            assertFalse(dut.navigateToPreviousPage());
            assertEquals(1, dut.currentPage());
        }

        @Test
        public void when_navigated_to_first_page_will_stay_at_first() {
            assertTrue(dut.navigateToFirstPage());
            assertEquals(1, dut.currentPage());
        }

        @Test
        public void when_navigated_to_last_page_will_stay_at_first() {
            assertTrue(dut.navigateToLastPage());
            assertEquals(1, dut.currentPage());
        }

        @ParameterizedTest
        @ValueSource(ints = {-10, -5, -1, 0, 1, 2, 3, 10, 1000})
        public void when_navigated_to_random_page_stays_at_page_1(int page) {
            dut.navigateToPage(page);
            if (page == 1) assertTrue(dut.navigateToPage(page));
            else assertFalse(dut.navigateToPage(page));
            assertEquals(1, dut.currentPage());
        }
    }

    @Nested class A_paged_menu_with_multiple_pages {

        @BeforeEach
        public void setupDut() {
            dut = new MockPagedMenu();
            dut.setPage(2, new MenuItem[0][0]);
        }

        @Test
        public void can_be_navigated_to_the_next_page() {
            assertEquals(1, dut.currentPage());
            assertTrue(dut.navigateToNextPage());
            assertEquals(2, dut.currentPage());
        }

    }

    static class MockPagedMenu extends AbstractPagedMenu {

        public MockPagedMenu() {
            super(Menu.MAX_ROWS, "Mock paged menu");
        }
    }

}