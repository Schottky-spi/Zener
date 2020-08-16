package com.github.schottky.zener.menu;

import com.github.schottky.zener.menu.event.MenuClickEvent;
import com.github.schottky.zener.menu.event.MenuCloseEvent;
import com.github.schottky.zener.menu.event.MenuOpenEvent;
import com.github.schottky.zener.menu.event.MenuTargetEvent;
import com.github.schottky.zener.menu.item.MenuItem;
import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

/**
 * An abstract superclass that fulfills all of the requirements for
 * a {@link Menu}
 */
public abstract class AbstractMenu implements Menu {

    private static final int columns = 9;
    @Override
    public int columnCount() { return columns; }

    private final int rows;
    @Override
    public int rowCount() { return rows; }

    private final String title;

    /**
     * returns the title of this inventory
     * @return The title
     */
    public String title() { return title; }

    private final MenuItem[][] contents;
    private Inventory backend = null;

    /**
     * creates a menu with the given number of rows and the title.
     * The rows may not exceed {@link Menu#MAX_ROWS} and may not be
     * smaller than 1
     * @param rows The amount of rows that should be allocated
     * @param title The title of this menu
     */

    public AbstractMenu(int rows, @NotNull String title) {
        Preconditions.checkArgument(rows <= MAX_ROWS,
                "Menu " + this.getClass().getSimpleName() + " requests more rows than allowed");
        Preconditions.checkArgument(rows >= 1, "cannot allocate less than one row");
        this.rows = rows;
        this.contents = new MenuItem[columns][rows];
        this.title = title;
    }

    @Override
    public void clickSlot(int slot, @NotNull MenuClickEvent trigger) {
        int y = slot / columns;
        int x = slot - y * columns;
        clickSlot(x, y, trigger);
    }

    /**
     * clicks the specified slot with x/y indexing compared to
     * absolute
     * @param slotX The x-index of the slot to click on
     * @param slotY The y-index of the slot to click on
     * @param trigger The event that triggered the click
     */

    public void clickSlot(int slotX, int slotY, MenuClickEvent trigger) {
        if (slotX > columns || slotX < 0 || slotY > rows || slotY < 0) return;
        if (contents[slotX][slotY] != null) contents[slotX][slotY].onClick(trigger);
    }

    @Override
    public void onTarget(@NotNull MenuTargetEvent trigger) {
        trigger.setCancelled(true);
    }

    /**
     * sets an item at the specific position of this menu
     * @param slotX The x-coordinate of the item
     * @param slotY The y.coordinate of the item
     * @param item The item to set at the given position
     */

    protected void setItem(int slotX, int slotY, MenuItem item) {
        Preconditions.checkArgument(slotX >= 0 && slotX < contents.length,
                "Slot X out of range for add (%s, max is %s)", slotX, contents.length);
        Preconditions.checkArgument(slotY >= 0 && slotY < contents[0].length,
                "Slot Y out of range for add (%s, max is %s)", slotY, contents[0].length);
        this.contents[slotX][slotY] = item;
    }

    /**
     * sets an item at the specified absolute position of this menu
     * @param slot The x-coordinate of the item
     * @param item The y-coordinate of this item
     */

    protected void setItem(int slot, MenuItem item) {
        int y = slot / columns;
        int x = slot - y * columns;
        setItem(x, y, item);
    }

    /**
     * sets a complete row to a certain menu-item
     * @param row The row to set
     * @param supplier The function that generates a menu-item at a given
     *                 x-coordinate
     */

    protected void setRow(int row, IntFunction<MenuItem> supplier) {
        for (int i = 0; i < columns; i++) {
            setItem(i, row, supplier.apply(i));
        }
    }

    /**
     * sets a complete row to a certain menu-item
     * @param column The column to set
     * @param supplier The function that generates a menu-item at a given y-coordinate
     */

    protected void setColumn(int column, IntFunction<MenuItem> supplier) {
        for (int i = 0; i < rows; i++) {
            setItem(column, i, supplier.apply(i));
        }
    }

    private HumanEntity player;

    @Override
    public int hashCode() {
        return player == null ? super.hashCode() : player.getUniqueId().hashCode();
    }

    @Override
    public void openTo(@NotNull HumanEntity player) {
        final MenuOpenEvent menuOpenEvent = new MenuOpenEvent(this, player);
        Bukkit.getPluginManager().callEvent(menuOpenEvent);
        if (!menuOpenEvent.isCancelled()) {
            final Inventory inv = this.createBackend();
            player.openInventory(inv);
            MenuRegistry.registerNewMenu(inv, this);
            this.player = player;
        }
    }

    public void close(boolean force) {
        if (player == null) return;
        final MenuCloseEvent menuCloseEvent = new MenuCloseEvent(this, player);
        Bukkit.getPluginManager().callEvent(menuCloseEvent);
        if (force) menuCloseEvent.setCancelled(false);
        else if (!menuCloseEvent.isCancelled())
            player.closeInventory();
    }

    /**
     * creates the backend for this inventory
     * @return The inventory backing this menu
     */

    public @NotNull Inventory createBackend() {
        this.backend = Bukkit.createInventory(null, columns * rows, title);
        this.sync();
        return backend;
    }

    public void sync() {
        if (this.backend == null) return;
        for (int y = 0; y < contents[0].length; y++) {
            for (int x = 0; x < contents.length; x++) {
                final MenuItem item = contents[x][y];
                this.backend.setItem(y * columnCount() + x,
                        item == null ? null : item.asItemStack());
            }
        }
    }
}
