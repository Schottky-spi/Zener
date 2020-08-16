package com.github.schottky.zener.menu.paged;

import com.github.schottky.zener.menu.item.MenuItem;
import com.google.common.base.Preconditions;

/**
 * Represents a paged menu that is modifiable after creation
 */
public interface ModifiablePagedMenu extends PagedMenu {

    /**
     * puts a certain item at the specific position. If the menu is
     * not large enough, it will grow to enable multiple pages
     * @param page The number of the page to add the item to
     * @param slotX The slot x to add the item at
     * @param slotY The slot y to add the item at
     * @param item The item to add
     */
    void put(int page, int slotX, int slotY, MenuItem item);

    /**
     * puts a whole page into the menu. This will replace an old page,
     * if the page already existed, or grow to add this page to the specified poition
     * @param page The page-number to add this page to
     * @param items The contents of the page. Will be appended or trimmed accordingly
     */
    void putPage(int page, MenuItem[][] items);

    /**
     * adds an empty page to the tail of this menu
     */
    default void addEmptyPage() {
        this.putPage(pageCount() + 1, new MenuItem[0][0]);
    }

    /**
     * adds a certain number of empty pages to this menu
     * @param amount The amount to add
     */

    default void addEmptyPages(int amount) {
        Preconditions.checkArgument(amount > 0);
        this.putPage(amount + pageCount(), new MenuItem[0][0]);
    }

    /**
     * removes an item
     * @param page The page to remove the item at
     * @param slotX The slot x in that page
     * @param slotY The slot y in that page
     */
    void remove(int page, int slotX, int slotY);

    /**
     * removes a page. Shifts all subsequent pages and
     * sets the current page, if needed
     * @param page The page to remove
     */
    void removePage(int page);

    void clearPage(int page);
}
