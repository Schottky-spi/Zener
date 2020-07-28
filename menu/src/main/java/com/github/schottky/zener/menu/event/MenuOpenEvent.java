package com.github.schottky.zener.menu.event;

import com.github.schottky.zener.menu.Menu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MenuOpenEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList() { return handlers; }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    private final Menu menu;
    public Menu menu() { return menu; }
    private final HumanEntity openedFor;
    public HumanEntity target() { return openedFor; }
    private boolean canceled = false;

    public MenuOpenEvent(Menu menu, HumanEntity openedFor) {
        this.menu = menu;
        this.openedFor = openedFor;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean canceled) {
        this.canceled = canceled;
    }
}
