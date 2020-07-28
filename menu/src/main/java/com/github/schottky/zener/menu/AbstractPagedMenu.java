package com.github.schottky.zener.menu;


import com.github.schottky.zener.menu.item.MenuItem;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPagedMenu extends AbstractMenu implements PagedMenu {

    public AbstractPagedMenu(int rows, @NotNull String title) {
        super(rows, title);
        this.setFittingPage(1, newEmptyPage());
    }

    private final List<MenuItem[][]> pages = new ArrayList<>();

    @Override
    public int pageCount() {
        return pages.size();
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

    @Override
    public void setPage(int page, MenuItem[] @NotNull [] items) throws IllegalArgumentException {
        Preconditions.checkArgument(page > 0);
        if (items.length == 0) {
            setFittingPage(page, newEmptyPage());
        } else if (items[0].length == 0) {
                setFittingPage(page, newEmptyPage());
        } else {
            Preconditions.checkArgument(items.length <= columns());
            Preconditions.checkArgument(items[0].length <= rows() - 1);
            setFittingPage(page, copyToFit(items));
        }
    }

    private void setFittingPage(int page, MenuItem[][] items) {
        while (pages.size() < page) {
            pages.add(newEmptyPage());
        }
        pages.set(page - 1, items);
        if (page == currentPage) this.sync();
    }

    private MenuItem[][] copyToFit(MenuItem[] @NotNull [] items) {
        if (items.length == 0 || items[0].length == 0) return newEmptyPage();
        MenuItem[][] copy = newEmptyPage();
        for (int x = 0; x < items.length; x++) {
            System.arraycopy(items[x], 0, copy[x], 0, items[0].length);
        }
        return copy;
    }

    public MenuItem[][] newEmptyPage() {
        return new MenuItem[columns()][rows() - 1];
    }

    @Override
    public void sync() {
        final MenuItem[][] page = pages.get(currentPage - 1);
        for (int y = 0; y < rows() - 1; y++) {
            for (int x = 0; x < columns(); x++) {
                this.setItem(x, y, page[x][y]);
            }
        }
        super.sync();
    }
}
