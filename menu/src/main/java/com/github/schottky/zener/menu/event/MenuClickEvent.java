package com.github.schottky.zener.menu.event;

import com.github.schottky.zener.menu.Menu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a slot inside the menu is clicked. This does not trigger,
 * if a slot outside the inventory if this menu is clicked. To detect when
 * this is the case, either use the {@link InventoryClickEvent} or the
 * {@link MenuTargetEvent}
 * <br> The latter one is preferred since this event has a high priority
 * and does not care about the cancellation-state of the inventory-click
 * event
 */

public class MenuClickEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList() { return handlers; }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    /**
     * The player that initiated this click
     */
    public final HumanEntity clicker;
    /**
     * The type of the click
     */
    public final ClickType clickType;
    /**
     * The menu that this click was performed in
     */
    public final Menu menu;
    /**
     * The type of slot; anything other than 'CONTAINER' is unexpected
     */
    public final SlotType slotType;
    /**
     * The slot that was clicked (refers to the relative slot of the
     * inventory; not the absolute slot)
     */
    public final int slot;
    /**
     * The action that was clicked with
     */
    public final InventoryAction action;

    public final ItemStack currentItem;

    public MenuClickEvent(
            HumanEntity clicker,
            ClickType clickType,
            SlotType slotType,
            int slot,
            InventoryAction action,
            ItemStack currentItem,
            Menu menu)
    {
        this.clicker = clicker;
        this.clickType = clickType;
        this.menu = menu;
        this.slotType = slotType;
        this.slot = slot;
        this.action = action;
        this.currentItem = currentItem;
    }

    public MenuClickEvent(@NotNull InventoryClickEvent event, Menu menu) {
        this(event.getWhoClicked(),
                event.getClick(),
                event.getSlotType(),
                event.getSlot(),
                event.getAction(),
                event.getCurrentItem(),
                menu);
    }

    private boolean canceled = false;

    /**
     * returns whether or not this event is cancelled
     * @return true, if the event is cancelled, false otherwise
     */
    @Override
    public boolean isCancelled() { return canceled; }

    /**
     * Sets the cancellation-state of this event.
     * If this event is cancelled, the click will not be performed;
     * in other words the underlying action of the menu will not trigger.
     * This is completely distinct from whether or not the item at this
     * place is allowed to be taken or not (the underlying InventoryClickEvent should
     * be cancelled or not)
     * @param canceled true, if this should be canceled, false otherwise
     */
    @Override
    public void setCancelled(boolean canceled) { this.canceled = canceled; }

    private boolean interactNormally = false;

    /**
     * Set this to true, if it is desired that this menu can respond normally to
     * click-actions, such as taking an item or shift-clicking the item out of the inventory
     * @param interactNormally true, if normal interaction is desired, false otherwise
     */

    public void setCanInteractNormally(boolean interactNormally) {
        this.interactNormally = interactNormally;
    }

    /**
     * true, if normal interaction like in an inventory is desired for this event,
     * false otherwise
     * @return whether or not a player can interact normally with this menu. Defaults to false
     */

    public boolean canInteractNormally() {
        return interactNormally;
    }
}
