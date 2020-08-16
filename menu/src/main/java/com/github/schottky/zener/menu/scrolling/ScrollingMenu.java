package com.github.schottky.zener.menu.scrolling;

import com.github.schottky.zener.menu.Menu;

/**
 * A menu that the user can scroll through
 */

public interface ScrollingMenu extends Menu {

    /**
     * scrolls a certain amount along the given axis
     * @param axis The axis to scroll thorough
     * @param modCount The amount to scroll
     * @return true, if the menu changed (the user can perform the scroll),
     * false otherwise (there are no items left in that direction)
     */

    boolean scroll(Axis axis, int modCount);

    /**
     * scrolls one unit up
     * @return true, if successful, false otherwise
     */

    default boolean scrollUp() {
        return this.scroll(Axis.VERTICAL, -1);
    }

    /**
     * scrolls one unit down
     * @return true, if successful, false otherwise
     */

    default boolean scrollDown() {
        return this.scroll(Axis.VERTICAL, 1);
    }

    /**
     * scrolls one unit to the right
     * @return true, if successful, false otherwise
     */

    default boolean scrollRight() {
        return this.scroll(Axis.HORIZONTAL, 1);
    }

    /**
     * scrolls one unit to the left
     * @return true, if successful, false otherwise
     */

    default boolean scrollLeft() {
        return this.scroll(Axis.HORIZONTAL, -1);
    }

    /**
     * The axis that a user can scroll along.
     * It is possible to have both axis at the same time
     */

    enum Axis {
        /**
         * Horizontal axis; user scrolls in
         * positive or negative x-direction
         * (left or right)
         */
        HORIZONTAL,

        /**
         * Vertical axis; user scrolls in
         * positive or negative y-direction
         * (up or down)
         */
        VERTICAL
    }
}
