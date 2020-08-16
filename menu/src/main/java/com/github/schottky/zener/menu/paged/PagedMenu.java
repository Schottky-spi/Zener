package com.github.schottky.zener.menu.paged;

import com.github.schottky.zener.menu.Menu;
import com.github.schottky.zener.menu.item.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A menu that has pages that can be navigated through.
 * Most implementations allow adding or removing items after creation
 */
public interface PagedMenu extends Menu {

    /**
     * Returns the current page that this menu displays.
     * Pages begin with number 1
     * @return The current page
     */

    int currentPage();

    /**
     * returns the number of pages that this Menu manages
     * @return How many pages this manages
     */

    int pageCount();

    /**
     * Navigates to the page with a certain number.
     * Pages begin with number '1'. If the page
     * is not a valid page (it is too small or too large)
     * this will return false, but does not throw.
     * <br>Triggers an update of the underlying inventory
     * @param page the page to navigate to
     * @return true, if navigated to the page, false otherwise
     */

    boolean navigateToPage(int page);

    /**
     * Navigates to the next page
     * @return true, if the next page could be navigated towards, false otherwise
     */

    default boolean navigateToNextPage() {
        return navigateToPage(currentPage() + 1);
    }

    /**
     * Navigates to the previous page
     * @return true, if this page is not the first page, false otherwise
     */

    default boolean navigateToPreviousPage() {
        return navigateToPage(currentPage() - 1);
    }

    /**
     * Navigate to the very last page
     * @return always true, false is an error-case
     */

    default boolean navigateToLastPage() {
        return navigateToPage(pageCount());
    }

    /**
     * Navigate to the very first page
     * @return always true, false is an error-case
     */

    default boolean navigateToFirstPage() {
        return navigateToPage(1);
    }


    class Builder {

        public Builder(String title) {
            this.title = title;
        }

        private final String title;
        private FlowStyle flowStyle = FlowStyle.LEFT_TO_RIGHT.then(FlowStyle.TOP_TO_BOTTOM);
        private int rows = Menu.MAX_ROWS;
        private final List<MenuItem> initialItems = new ArrayList<>();

        public Builder flowStyle(FlowStyle flowStyle) {
            this.flowStyle = flowStyle;
            return this;
        }

        public Builder rows(int rows) {
            this.rows = rows;
            return this;
        }

        public Builder addItems(MenuItem... items) {
            return this.addItems(Arrays.asList(items));
        }

        public Builder addItems(Iterable<MenuItem> items) {
            if (items instanceof Collection) {
                initialItems.addAll((Collection<? extends MenuItem>) items);
            } else {
                for (MenuItem item : items) initialItems.add(item);
            }
            return this;
        }

        public PagedMenu build() {
            return new ContainerPagedMenu(
                    rows,
                    title,
                    flowStyle,
                    initialItems.toArray(new MenuItem[0]));
        }

    }
}
