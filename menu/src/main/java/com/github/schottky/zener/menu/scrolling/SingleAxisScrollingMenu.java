package com.github.schottky.zener.menu.scrolling;

import com.github.schottky.zener.menu.item.MenuItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class SingleAxisScrollingMenu extends AbstractScrollingMenu {

    public SingleAxisScrollingMenu(int rows, @NotNull String title, Axis axis, MenuItem[] initialItems) {
        super(rows, title, EnumSet.of(axis));
        this.axis = axis;
        final int arrLength = axis == Axis.VERTICAL ? availableColumns() : availableRows();
        int index = 0;
        while (index < initialItems.length) {
            final MenuItem[] tableEntry = new MenuItem[arrLength];
            for (int i = 0; i < tableEntry.length; i++) {
                if (index >= initialItems.length)
                    break;
                tableEntry[i] = initialItems[index];
                index++;
            }
            this.table.add(tableEntry);
        }
    }

    private final Axis axis;

    private final List<MenuItem[]> table = new ArrayList<>();

    @Override
    protected int totalSizeX() {
        return axis == Axis.VERTICAL ? availableColumns() : table.size();
    }

    @Override
    protected int totalSizeY() {
        return axis == Axis.VERTICAL ? table.size() : availableRows();
    }

    @Override
    protected MenuItem itemAt(int x, int y) {
        return axis == Axis.VERTICAL ? table.get(y)[x] : table.get(x)[y];
    }
}
