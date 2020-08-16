package com.github.schottky.zener.menu.item;

import com.github.schottky.zener.menu.event.MenuClickEvent;
import com.github.schottky.zener.util.item.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * A simple menu-item that only displays it's content (the {@code ItemStack}
 * and does nothing on-click
 */

public class DisplayMenuItem implements MenuItem {

    private final ItemStack stack;

    public DisplayMenuItem(ItemStack stack) {
        this.stack = stack;
    }

    public DisplayMenuItem(Material material) {
        this.stack = Items.withoutTitle(material);
    }

    @Override
    public void onClick(@NotNull MenuClickEvent trigger) { /* Do nothing */ }

    @Override
    public ItemStack asItemStack() {
        return stack;
    }
}
