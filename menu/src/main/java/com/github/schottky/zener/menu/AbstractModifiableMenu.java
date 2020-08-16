package com.github.schottky.zener.menu;

import com.github.schottky.zener.menu.item.MenuItem;
import org.jetbrains.annotations.NotNull;

/**
 * A basic implementation for an inventory that can be modified once
 * after creation
 */

public abstract class AbstractModifiableMenu extends AbstractMenu implements ModifiableMenu {

    /**
     * creates a menu with the given number of rows and the title.
     * The rows may not exceed {@link Menu#MAX_ROWS} and may not be
     * smaller than 1
     *
     * @param rows  The amount of rows that should be allocated
     * @param title The title of this menu
     */
    public AbstractModifiableMenu(int rows, @NotNull String title) {
        super(rows, title);
    }

    @Override
    public void put(int x, int y, MenuItem item) {
        this.setItem(x, y, item);
        this.sync();
    }

    @Override
    public void remove(int x, int y) {
        this.setItem(x, y, null);
        this.sync();
    }
}
