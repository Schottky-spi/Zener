package com.github.schottky.zener.menu.item;

import com.github.schottky.zener.menu.event.MenuClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * A simple menu-item that will suffice for most use-cases.
 * This menu-item will always have one item-stack as its content and
 * always performs the given action if being clicked
 */

public class SimpleMenuItem implements MenuItem {

    private final Consumer<MenuClickEvent> onClick;
    private final ItemStack stack;

    /**
     * returns a new simple menu-item that will perform the given action when clicked
     * and always display the given item-stack
     * @param onClick The action to perform
     * @param stack The stack to display
     */

    public SimpleMenuItem(@NotNull Consumer<MenuClickEvent> onClick, @Nullable ItemStack stack) {
        this.onClick = onClick;
        this.stack = stack == null ? null : stack.clone();
    }

    /**
     * returns a new simple menu-item that will perform the given action when clicked
     * and always display the given item-stack.
     * @param onClick The action to perform
     * @param stack The stack to display
     */

    public SimpleMenuItem(@NotNull Runnable onClick, ItemStack stack) {
        this.onClick = e -> onClick.run();
        this.stack = stack == null ? null : stack.clone();
    }

    @Override
    public void onClick(@NotNull MenuClickEvent trigger) {
        onClick.accept(trigger);
    }

    @Override
    public ItemStack asItemStack() {
        return stack;
    }
}
