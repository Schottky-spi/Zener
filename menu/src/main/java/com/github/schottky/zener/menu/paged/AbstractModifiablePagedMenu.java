package com.github.schottky.zener.menu.paged;

import com.github.schottky.zener.menu.item.MenuItem;
import org.jetbrains.annotations.NotNull;

/**
 * A paged menu that can be modified after creation
 */
public abstract class AbstractModifiablePagedMenu extends AbstractPagedMenu implements ModifiablePagedMenu {

    public AbstractModifiablePagedMenu(int rows, @NotNull String title) {
        super(rows, title);
    }

    @Override
    public void put(int page, int slotX, int slotY, MenuItem item) {
        this.setItem(page, slotX, slotY, item);
        if (page == currentPage())
            this.sync();
    }

    @Override
    public void putPage(int page, MenuItem[][] items) {
        this.setPage(page, items);
        if (page == currentPage())
            this.sync();
    }

    @Override
    public void remove(int page, int slotX, int slotY) {
        put(page, slotX, slotY, null);
        if (page == currentPage())
            this.sync();
    }

    @Override
    public void removePage(int page) {
        this.remove(page);
        this.sync();
    }

    @Override
    public void clearPage(int page) {
        this.setPage(page, newEmptyPage());
    }
}
