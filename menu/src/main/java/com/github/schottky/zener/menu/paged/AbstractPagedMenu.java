package com.github.schottky.zener.menu.paged;


import com.github.schottky.zener.menu.AbstractMenu;
import com.github.schottky.zener.menu.item.MenuItem;
import com.github.schottky.zener.menu.item.SimpleUIItem;
import com.github.schottky.zener.menu.item.Spacer;
import com.github.schottky.zener.util.item.Items;
import com.google.common.base.Preconditions;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that serves as base-class for all paged menus
 */
public abstract class AbstractPagedMenu extends AbstractMenu implements PagedMenu {

    public AbstractPagedMenu(int rows, @NotNull String title) {
        super(rows, title);
        Preconditions.checkArgument(rows > 2,
                "Paged menu cannot be created with less than 2 rows");
        this.setFittingPage(1, newEmptyPage());
    }

    /**
     * sets and updates the navigator-bar.
     * It is assumed that this bar only asserts one slot
     */

    protected void updateNavigatorBar() {
        this.setRow(rowCount() - 1, i -> new Spacer(DyeColor.GRAY));
        this.setItem(1, rowCount() - 1,
                navigatorItem(this::navigateToPreviousPage, "Previous page"));
        this.setItem(7, rowCount() - 1,
                navigatorItem(this::navigateToNextPage, "Next page"));
        this.setItem(4, rowCount() - 1, () ->
                Items.withTitle(Material.NETHER_STAR, "Page " + currentPage + "/" + pageCount()));
    }

    private MenuItem navigatorItem(Runnable navigator, String description) {
        return new SimpleUIItem(navigator, Items.withTitle(Material.PAPER, description));
    }

    private final List<MenuItem[][]> pages = new ArrayList<>();

    @Override
    public int pageCount() {
        return pages.size();
    }

    /**
     * removes a page and shifts all subsequent pages one to the left
     * @param page The page to remove
     */

    protected void remove(int page) {
        Preconditions.checkArgument(page > 0,
                "Page must be greater than zero");
        Preconditions.checkArgument(page <= pageCount(),
                "Page must be less than or equal to the page count");
        if (page == 1) {
            this.pages.remove(0);
            currentPage -= 1;
        } else {
            if (page <= currentPage)
                currentPage -= 1;
            this.pages.remove(page - 1);
        }
        if (currentPage < 1) currentPage = 1;
    }

    private int currentPage = 1;

    @Override
    public int currentPage() {
        return currentPage;
    }

    @Override
    public boolean navigateToPage(int page) {
        if (page < 1 || page > pageCount()) return false;
        this.currentPage = page;
        this.sync();
        return true;
    }

    /**
     * sets an item to a certain position. If the page is greater than the page-count,
     * this will allocate pages until the item can be put into the page
     * @param page the page to insert the item at
     * @param slotX The x-position of the item
     * @param slotY The y-position of the item
     * @param item The item to insert
     */

    protected void setItem(int page, int slotX, int slotY, MenuItem item) {
        Preconditions.checkArgument(page > 0);
        while (pages.size() < page) {
            pages.add(newEmptyPage());
        }
        final MenuItem[][] items = pages.get(page - 1);
        items[slotX][slotY] = item;
    }

    // TODO: move to modifiable paged inventory
    public void setPage(int page, MenuItem[] @NotNull [] items) throws IllegalArgumentException {
        Preconditions.checkArgument(page > 0);
        if (items.length == 0) {
            setFittingPage(page, newEmptyPage());
        } else if (items[0].length == 0) {
                setFittingPage(page, newEmptyPage());
        } else {
            Preconditions.checkArgument(items.length <= columnCount());
            Preconditions.checkArgument(items[0].length <= rowCount() - 1);
            setFittingPage(page, copyToFit(items));
        }
    }

    private void setFittingPage(int page, MenuItem[][] items) {
        while (pages.size() < page) {
            pages.add(newEmptyPage());
        }
        pages.set(page - 1, items);
    }

    private MenuItem[][] copyToFit(MenuItem[] @NotNull [] items) {
        if (items.length == 0 || items[0].length == 0) return newEmptyPage();
        MenuItem[][] copy = newEmptyPage();
        for (int x = 0; x < items.length; x++) {
            System.arraycopy(items[x], 0, copy[x], 0, items[0].length);
        }
        return copy;
    }

    /**
     * returns a new menu-item array that has the size
     * of a single page
     * @return The newly created page
     */

    public MenuItem[][] newEmptyPage() {
        return new MenuItem[columnCount()][rowCount() - 1];
    }

    @Override
    public void sync() {
        final MenuItem[][] page = pages.get(currentPage - 1);
        for (int y = 0; y < rowCount() - 1; y++) {
            for (int x = 0; x < columnCount(); x++) {
                this.setItem(x, y, page[x][y]);
            }
        }
        updateNavigatorBar();
        super.sync();
    }
}
