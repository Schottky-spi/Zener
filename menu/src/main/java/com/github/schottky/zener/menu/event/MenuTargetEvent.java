package com.github.schottky.zener.menu.event;

import com.github.schottky.zener.menu.Menu;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

public class MenuTargetEvent extends InventoryClickEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    public static @NotNull HandlerList getHandlerList() { return handlers; }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    private final Menu menu;
    public Menu menu() { return menu; }

    public MenuTargetEvent(
            @NotNull InventoryView view,
            @NotNull SlotType type,
            int slot,
            @NotNull ClickType click,
            @NotNull InventoryAction action,
            int key,
            Menu menu)
    {
        super(view, type, slot, click, action, key);
        this.menu = menu;
    }

    public MenuTargetEvent(@NotNull InventoryClickEvent event, Menu menu) {
        this(event.getView(),
                event.getSlotType(),
                event.getSlot(),
                event.getClick(),
                event.getAction(),
                event.getHotbarButton(),
                menu);
    }

    private boolean cancel = true;

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
