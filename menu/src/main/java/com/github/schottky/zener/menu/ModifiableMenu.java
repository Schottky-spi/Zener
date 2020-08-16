package com.github.schottky.zener.menu;

import com.github.schottky.zener.menu.item.MenuItem;

/**
 * represents a menu where the single elements may be modifiable after
 * the menu was created
 */

public interface ModifiableMenu extends Menu {

    /**
     * puts an item into this menu at some position x and y. The behavior
     * of out-of bounds indices is not defined. Implementations could
     * choose to allow this and add scrolling feature, or throw exceptions.
     * Refer to the implementations for details
     * @param x The position x that the item should be inserted
     * @param y The position y that the item should be inserted
     * @param item The item to insert
     */

    void put(int x, int y, MenuItem item);

    /**
     * inserts all of the items into this inventory. The items-array is
     * expected to be quadratic.
     * The order is left-to right and top-to bottom.
     * <br>Some implementations may be perform better if bulk-operations like this
     * will be used, compared to a consecutive adding of elements
     * @param startX The starting position x that the first element will be inserted
     * @param startY The starting position y that the second element will be inserted
     * @param items The items to be inserted, starting from that position
     */

    default void putAll(int startX, int startY, MenuItem[][] items) {
        for (int x = startX; x < startX + items.length; x++) {
            for (int y = startY; y < startY + items[0].length; y++) {
                this.put(x, y, items[x][y]);
            }
        }
    }

    /**
     * Removes an item at a certain position
     * @param x The x-position of the item
     * @param y The y-position of the item
     */

    void remove(int x, int y);
}
