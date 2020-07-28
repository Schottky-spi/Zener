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

public abstract class AbstractMenu implements Menu {

    private static final int columns = 9;
    public int columns() { return columns; }

    private final int rows;
    public int rows() { return rows; }

    private final String title;
    public String title() { return title; }

    private final MenuItem[][] contents;
    private Inventory backend = null;

    public AbstractMenu(int rows, @NotNull String title) {
        Preconditions.checkArgument(rows <= MAX_ROWS,
                "Menu " + this.getClass().getSimpleName() + " requests more rows than allowed");
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

    public void clickSlot(int slotX, int slotY, MenuClickEvent trigger) {
        if (slotX > columns || slotX < 0 || slotY > rows || slotY < 0) return;
        if (contents[slotX][slotY] != null) contents[slotX][slotY].onCLick(trigger);
    }

    @Override
    public void onTarget(@NotNull MenuTargetEvent trigger) {
        trigger.setCancelled(true);
    }

    protected void setItem(int slotX, int slotY, MenuItem item) {
        Preconditions.checkArgument(slotX >= 0 && slotX < contents.length,
                "Slot X out of range for add (%s, max is %s)", slotX, contents.length);
        Preconditions.checkArgument(slotY >= 0 && slotY < contents[0].length,
                "Slot Y out of range for add (%s, max is %s)", slotY, contents[0].length);
        this.contents[slotX][slotY] = item;
    }

    protected void setItem(int slot, MenuItem item) {
        int y = slot / columns;
        int x = slot - y * columns;
        setItem(x, y, item);
    }

    protected void setRow(int row, IntFunction<MenuItem> supplier) {
        for (int i = 0; i < columns; i++) {
            setItem(i, row, supplier.apply(i));
        }
    }

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
            player.openInventory(this.createBackend());
            this.player = player;
        }
    }

    public void close() {
        if (player == null) return;
        final MenuCloseEvent menuCloseEvent = new MenuCloseEvent(this, player);
        Bukkit.getPluginManager().callEvent(menuCloseEvent);
        if (!menuCloseEvent.isCancelled())
            player.closeInventory();
    }

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
                if (item != null) {
                    this.backend.setItem(y * columns() + x, item.asItemStack());
                }
            }
        }
    }
}
