package com.github.schottky.zener.menu.scrolling;

import com.github.schottky.zener.menu.AbstractMenu;
import com.github.schottky.zener.menu.item.MenuItem;
import com.github.schottky.zener.menu.item.SimpleMenuItem;
import com.github.schottky.zener.menu.item.Spacer;
import com.github.schottky.zener.util.item.Items;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;

/**
 * An abstract implementation of a scrolling-menu.
 * This will set the scroll-bars and sync the inventory correctly.
 * Also handles the scroll-functionality
 */

public abstract class AbstractScrollingMenu extends AbstractMenu implements ScrollingMenu {

    public AbstractScrollingMenu(int rows, @NotNull String title, Set<Axis> axisSet) {
        super(rows, title);
        this.axisSet = axisSet.isEmpty() ? EnumSet.noneOf(Axis.class) : EnumSet.copyOf(axisSet);
    }

    /**
     * Sets the scroll-wheels for one or two axes.
     * Override this to enable a custom scroll-wheel.
     * Note that if the scroll-wheel is bigger than
     * 1 slot, the methods {@link #availableColumns()}
     * and {@link #availableRows()} have to be overridden
     * as well
     */

    protected void updateScrollWheels() {
        int endX = columnCount() - 1;
        int endY = rowCount() - 1;
        if (axisSet.size() >= 2) {
            this.setItem(endX, endY, null);
            endX -= 1;
            endY -= 1;
        }
        if (this.hasAxis(Axis.HORIZONTAL)) {
            this.setRow(this.rowCount() - 1, i -> new Spacer(DyeColor.GRAY));
            this.setItem(0, this.rowCount() - 1,
                    new SimpleMenuItem(this::scrollLeft, Items.withTitle(Material.PAPER, "Left")));
            this.setItem(endX, this.rowCount() - 1,
                    new SimpleMenuItem(this::scrollRight, Items.withTitle(Material.PAPER, "Right")));
        }
        if (this.hasAxis(Axis.VERTICAL)) {
            this.setColumn(this.columnCount() - 1, i -> new Spacer(DyeColor.GRAY));
            this.setItem(this.columnCount() - 1, 0,
                    new SimpleMenuItem(this::scrollUp, Items.withTitle(Material.PAPER, "Up")));
            this.setItem(this.columnCount() - 1, endY,
                    new SimpleMenuItem(this::scrollDown, Items.withTitle(Material.PAPER, "Down")));
        }
    }

    // private, modify via #scroll(Axis, int)
    private int scrollPointerX = 0;

    /**
     * returns the current position of the scroll-pointer
     * int x-direction
     * @return The position
     */
    protected int currentPosX() {
        return scrollPointerX;
    }
    // private, modify via #scroll(Axis, int)
    private int scrollPointerY = 0;

    /**
     * returns the current position of the scroll-pointer
     * in y-direction
     * @return The position
     */
    protected int currentPosY() {
        return scrollPointerY;
    }

    private final Set<Axis> axisSet;

    public boolean hasAxis(Axis axis) {
        return axisSet.contains(axis);
    }

    /**
     * Adds a certain axis to this axis-set.
     * Has no effect, of the axis is already present
     * @param axis The axis to add
     */
    protected void addAxis(Axis axis) {
        if (axisSet.add(axis))
            this.sync();
    }

    /**
     * Removes a certain axis from this
     * axis-set. Has no effect, if this axis-set
     * did not contain the axis
     * @param axis The axis to remove
     */
    protected void removeAxis(Axis axis) {
        if (axisSet.remove(axis))
            this.sync();
    }

    /**
     * returns the amount of rows available rows to display menu-items.
     * This is either the total row-count minus one, if the axis-set
     * contains the horizontal scrolling-axis, or the row cont itself
     * @return The count of available rows
     */

    protected int availableRows() { return hasAxis(Axis.HORIZONTAL) ? rowCount() - 1 : rowCount(); }

    /**
     * returns the amount of rows available columns to display menu-items.
     * This is either the total column-count minus one, if the axis-set
     * contains the vertical scrolling-axis, or the column cont itself
     * @return The count of available columns
     */

    protected int availableColumns() { return hasAxis(Axis.VERTICAL) ? columnCount() - 1 : columnCount(); }

    @Override
    public boolean scroll(Axis axis, int modCount) {
        if (axis == Axis.HORIZONTAL && isInRangeX(modCount) && (totalSizeX() > availableColumns()))  {
            scrollPointerX += modCount;
            this.sync();
            return true;
        } else if (axis == Axis.VERTICAL && isInRangeY(modCount) && (totalSizeY() > availableRows())) {
            scrollPointerY += modCount;
            this.sync();
            return true;
        } else {
            return false;
        }
    }

    private boolean isInRangeX(int modCount) {
        return scrollPointerX + modCount >= 0 && scrollPointerX + availableColumns() + modCount <= totalSizeX();
    }

    private boolean isInRangeY(int modCount) {
        return scrollPointerY + modCount >= 0 && scrollPointerY + availableRows() + modCount <= totalSizeY();
    }

    /**
     * The total size that menu-items can lay out in x-direction.
     * May be smaller, bigger or equivalent to {@link #columnCount()}
     * and defines how far can be scrolled
     * @return The total size
     */

    protected abstract int totalSizeX();

    /**
     * The total size that menu-items can lay out in y-direction.
     * May be smaller, bigger or equivalent to {@link #rowCount()}
     * and defines how far can be scrolled
     * @return The total size
     */

    protected abstract int totalSizeY();

    /**
     * returns the {@code MenuItem} at a specific, absolute
     * point x and y. The points x and y do not have to be
     * inside the enclosing inventory, but they must return a
     * sensible value (null is allowed) for
     * <br>x = 0...totalSizeX()
     * <br>y = 0...totalSizeY()
     * <br>(both including 0, but excluding totalSizeX(), resp.
     * totalSizeY())
     * @param x The x-value to get the item at
     * @param y The y.value to get the item at
     * @return The menu-item at this point
     */

    protected abstract MenuItem itemAt(int x, int y);

    @Override
    public void sync() {
        for (int x = 0; x < Math.min(availableColumns(), totalSizeX()); x++) {
            for (int y = 0; y < Math.min(availableRows(), totalSizeY()); y++) {
                this.setItem(x, y, itemAt(x + scrollPointerX, y + scrollPointerY));
            }
        }
        updateScrollWheels();
        super.sync();
    }
}
