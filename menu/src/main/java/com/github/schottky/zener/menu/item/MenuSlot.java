package com.github.schottky.zener.menu.item;

import com.github.schottky.zener.menu.event.MenuClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MenuSlot extends MenuItem {

    /**
     * Offers a certain stack. This method should return {@code true},
     * if this slot <em>accepts</em> the item. This method does not care about
     * whether or not this slot allows this item (e.g. it is stackable and/or empts),
     * but simply if it wants to accept the given stack, or not
     * @param stack The stack that is offered
     * @return true, if the stack can be put into this slot, false otherwise
     */
    default boolean offer(@Nullable ItemStack stack) { return true; }

    /**
     * Decides if the current item can be taken out of the slot.
     * This implementation should does not care about the logic, only the
     * the currently stored item-stack is of importance.
     * <br>For example, A user could specify this way that the stack-size
     * of an item stored in this slot has to be at least 1.
     * @param amount The amount to take
     * @return true, if the current item can be removed, false otherwise
     */
    default boolean take(int amount) { return true; }

    /**
     * The item-stack that this slot currently holds
     * @return The stack
     */

    @Nullable ItemStack currentStorageContent();

    /**
     * sets the stack that this slot currently holds
     * @param stack The stack
     */
    void setStorageContent(ItemStack stack);

    /**
     * returns a priority for an ItemStack. Higher values mean higher priority,
     * lower values mean lower priority.
     * Slots with higher priority for a certain item will get preferred compared
     * to slots with lower priority for that item. Generally, priority 0 means
     * that it doesn't care, anything greater than 0 wants the stack
     * and everything below zero only takes the stack if there is absolutely no other
     * slot that can take this stack.
     * This priority applies if there are multiple slots that are empty.
     * If a slot does not accept the item at all, refer to {@link #offer(ItemStack)}
     * @param stack The stack for which to quarry the priority
     * @return The priority
     */
    default int priorityFor(ItemStack stack) { return 0; }

    @Override
    default @Nullable ItemStack asItemStack() { return currentStorageContent(); }

    @Override
    default void onClick(@NotNull MenuClickEvent trigger) {
        if (this.offer(trigger.currentItem))
            trigger.setCanInteractNormally(true);
    }
}
