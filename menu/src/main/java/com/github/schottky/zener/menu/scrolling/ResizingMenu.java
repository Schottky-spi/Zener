package com.github.schottky.zener.menu.scrolling;

import com.github.schottky.zener.menu.ModifiableMenu;
import com.github.schottky.zener.menu.item.MenuItem;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

/**
 * A menu that will automatically add scrolling-axis or remove them
 * on demand
 */
public class ResizingMenu extends AbstractScrollingMenu implements ModifiableMenu {

    public ResizingMenu(int rows, @NotNull String title) {
        super(rows, title, EnumSet.noneOf(Axis.class));
    }

    public ResizingMenu(int rows, @NotNull String title, MenuItem[][] initialItems) {
        this(rows, title);
        this.putAll(0, 0, initialItems);
    }

    private final Table<Integer, Integer, MenuItem> table = HashBasedTable.create();

    private int totalSizeX = 0;

    @Override
    protected int totalSizeX() {
        return totalSizeX;
    }

    private int totalSizeY = 0;

    @Override
    protected int totalSizeY() {
        return totalSizeY;
    }

    @Override
    protected MenuItem itemAt(int x, int y) {
        return table.get(x, y);
    }

    /**
     * Puts a menu-item at position x &amp; y.
     * Negative positions are not allowed, however there are no restrictions
     * on positive values. Behavior for very large values (close to {@code Integer#MAX_VALUE}
     * are not tested and not expected.
     * If the value is greater than what fits currently in the inventory, this
     * menu will add scrolling-axis on-demand
     * @param x The position x that the item should be inserted
     * @param y The position y that the item should be inserted
     * @param item The item to insert
     */

    @Override
    public void put(int x, int y, MenuItem item) {
        this.table.put(x, y, item);
        updateSizeAndSync(x, y);
    }

    @Override
    public void putAll(int startX, int startY, MenuItem[][] items) {
        for (int x = startX; x < items.length; x++) {
            for (int y = startY; y < items[0].length; y++) {
                this.table.put(x, y, items[x][y]);
            }
        }
        updateSizeAndSync(startX + items.length, startX + items[0].length);
    }

    private void updateSizeAndSync(int maxPositionX, int maxPositionY) {
        if (maxPositionX + 1 >= totalSizeX) this.totalSizeX = maxPositionX + 1;
        if (maxPositionY + 1 >= totalSizeY) this.totalSizeY = maxPositionY + 1;

        if (totalSizeX >= availableColumns()) this.addAxis(Axis.HORIZONTAL);
        if (totalSizeY >= availableRows()) this.addAxis(Axis.VERTICAL);

        this.sync();
    }

    @Override
    public void remove(int x, int y) {
        this.table.remove(x, y);

        int highestX = 0;
        int highestY = 0;
        for (Table.Cell<Integer, Integer, MenuItem> cell: table.cellSet()) {
            if (cell.getColumnKey() != null && cell.getColumnKey() > highestY) highestY = cell.getColumnKey();
            if (cell.getRowKey() != null && cell.getRowKey() > highestX) highestX = cell.getRowKey();
        }

        this.totalSizeX = highestX + 1;
        this.totalSizeY = highestY + 1;

        if (totalSizeX < availableColumns()) this.removeAxis(Axis.HORIZONTAL);
        if (totalSizeY < availableRows()) this.removeAxis(Axis.VERTICAL);

        System.out.println(hasAxis(Axis.HORIZONTAL));
        System.out.println(hasAxis(Axis.VERTICAL));

        this.sync();
    }
}
