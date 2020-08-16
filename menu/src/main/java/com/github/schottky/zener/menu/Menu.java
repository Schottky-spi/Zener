package com.github.schottky.zener.menu;

import com.github.schottky.zener.menu.event.MenuClickEvent;
import com.github.schottky.zener.menu.event.MenuTargetEvent;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an immutable menu that can accept
 * click-actions and can be targeted
 */

public interface Menu {

    /**
     * the maximum amount of rows a menu can have
     */
    int MAX_ROWS = 6;

    /**
     * clicks on a certain slot of this inventory
     * @param slot The slot to click
     * @param trigger The action that triggered the click
     */
    void clickSlot(int slot, @NotNull MenuClickEvent trigger);

    /**
     * called when this inventory is being targeted, meaning
     * a player either shift-clicked an item into this inventory
     * or put it into the menu by clicking a certain slot
     * @param trigger The action that triggered the target
     */
    void onTarget(MenuTargetEvent trigger);

    /**
     * the amount of rows that this inventory has
     * @return The row-count
     */
    int rowCount();

    /**
     * The amount of columns that this inventory has
     * @return The columns-count
     */
    int columnCount();

    /**
     * opens this menu to a player. Always use this method
     * to open a menu (do not open the backing inventory)
     * @param player The player to open this to
     */

    void openTo(@NotNull HumanEntity player);

    /**
     * closes this inventory
     * @param force whether this should be forced. If true,
     *              this will ignore the cancellation-state of the
     *              triggered {@link com.github.schottky.zener.menu.event.MenuCloseEvent},
     *              if false and the resp. close event is cancelled, this will not close.
     *              However, the event will still be triggered
     */
    void close(boolean force);

    /**
     * closes this inventory without forcing it
     */
    default void close() {
        this.close(false);
    }
}
