package com.github.schottky.zener.menu.item;

import com.github.schottky.zener.menu.event.MenuClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DisplayMenuItem implements MenuItem {

    private final ItemStack stack;

    public DisplayMenuItem(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void onCLick(@NotNull MenuClickEvent trigger) { /* Do nothing */ }

    @Override
    public ItemStack asItemStack() {
        return stack;
    }
}
