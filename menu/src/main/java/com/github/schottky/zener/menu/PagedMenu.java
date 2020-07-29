package com.github.schottky.zener.menu;

import com.github.schottky.zener.menu.item.MenuItem;

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

    /**
     * Set the raw contents of a page. The page-number may not be smaller than one, but it may be
     * bigger than the number of pages currently in this menu. If this is the case, empty pages
     * are added until this page can be inserted
     * @param page The page at which to set the contents.
     * @param items The items to set
     * @throws IllegalArgumentException if the items do not fit on the page.
     * It is allowed for the items to be smaller in x-and y direction, but not larger
     */

    void setPage(int page, MenuItem[][] items) throws IllegalArgumentException;
}
