package com.github.schottky.zener.menu;

import com.github.schottky.zener.menu.event.MenuClickEvent;
import com.github.schottky.zener.menu.item.MenuItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestMenuItem implements MenuItem {

    public boolean clicked = false;

    @Override
    public void onClick(@NotNull MenuClickEvent trigger) {
        clicked = true;
    }

    @Override
    public @Nullable ItemStack asItemStack() { return null; }

    public void reset() {
        clicked = false;
    }

    @Override
    public String toString() {
        return "TestMenuItem{}";
    }
}
