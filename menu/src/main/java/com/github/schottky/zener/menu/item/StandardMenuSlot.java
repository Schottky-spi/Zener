package com.github.schottky.zener.menu.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class StandardMenuSlot implements MenuSlot {

    private ItemStack content;

    @Override
    public @Nullable ItemStack currentStorageContent() {
        return content;
    }

    @Override
    public void setStorageContent(ItemStack stack) {
        this.content = stack;
    }
}
