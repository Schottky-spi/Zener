package com.github.schottky.zener.menu.event;

import com.github.schottky.zener.menu.Menu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Triggered whenever a player closes a menu
 */

public class MenuCloseEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    private final Menu menu;

    /**
     * The menu that is attempted to be closed
     * @return the menu
     */
    public @NotNull Menu menu() { return menu; }
    private final HumanEntity viewer;

    /**
     * The player that is attempting to close the inventory
     * @return The player
     */
    public @NotNull HumanEntity player() { return viewer; }

    public MenuCloseEvent(@NotNull Menu menu, @NotNull HumanEntity viewer) {
        this.menu = menu;
        this.viewer = viewer;
    }

    private boolean canceled = false;

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    /**
     * set whether or not this inventory may be closed.
     * Compared to {@link org.bukkit.event.inventory.InventoryCloseEvent}, this event
     * allows the cancelling of the event. If this event is cancelled, the menu is immediately
     * re-opened to the player after 1 tick
     * @param cancel true, if the inventory should not close, false otherwise
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.canceled = cancel;
    }
}
