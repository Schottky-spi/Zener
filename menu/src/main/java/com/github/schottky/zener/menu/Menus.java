package com.github.schottky.zener.menu;

import com.github.schottky.zener.menu.item.DisplayMenuItem;
import com.github.schottky.zener.menu.item.MenuItem;
import com.github.schottky.zener.menu.item.Spacer;
import com.github.schottky.zener.menu.paged.PagedMenu;
import com.github.schottky.zener.menu.scrolling.ScrollingMenu;
import com.github.schottky.zener.menu.scrolling.SingleAxisScrollingMenu;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

public final class Menus {

    private Menus() {}

    @Contract("_, _, _ -> new")
    public static @NotNull Menu newConfirmationDialog(
            @NotNull String title,
            @NotNull MenuItem accept,
            @NotNull MenuItem deny) {
        return new ConfirmationDialog(title, accept, deny);
    }

    private static class ConfirmationDialog extends AbstractMenu {
        public ConfirmationDialog(
                @NotNull String title,
                @NotNull MenuItem onAccept,
                @NotNull MenuItem onDeny)
        {
            super(1, title);
            this.setRow(0, i -> new Spacer(DyeColor.GRAY));
            this.setItem(3, 0, onAccept);
            this.setItem(5, 0, onDeny);
            this.setItem(4, 0, new DisplayMenuItem(Material.NETHER_STAR));
        }
    }

    public static @NotNull PagedMenu newPagedMenuWithElements(
            @NotNull String title,
            int rows,
            Stream<MenuItem> items)
    {
        return new PagedMenu.Builder(title)
                .rows(rows)
                .addItems(items.toArray(MenuItem[]::new))
                .build();
    }

    public static <T> @NotNull PagedMenu newPagedMenuWithElements(
            @NotNull String title,
            int rows,
            Collection<T> items,
            Function<T,MenuItem> mapper)
    {
        return newPagedMenuWithElements(title, rows, items.stream().map(mapper));
    }

    public static ScrollingMenu newVerticalScrollingMenu(
            @NotNull String title,
            int rows,
            MenuItem[] initialItems)
    {
        return new SingleAxisScrollingMenu(rows, title, ScrollingMenu.Axis.VERTICAL, initialItems);
    }

    public static ScrollingMenu newHorizontalScrollingMenu(
            @NotNull String title,
            int rows,
            MenuItem[] initialItems)
    {
        return new SingleAxisScrollingMenu(rows, title, ScrollingMenu.Axis.HORIZONTAL, initialItems);
    }
}
