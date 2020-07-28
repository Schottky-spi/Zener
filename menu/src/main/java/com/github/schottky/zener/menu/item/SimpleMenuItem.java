package com.github.schottky.zener.menu.item;

import com.github.schottky.zener.menu.event.MenuClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SimpleMenuItem implements MenuItem {

    private final Consumer<MenuClickEvent> onClick;
    private final ItemStack stack;

    public SimpleMenuItem(@NotNull Consumer<MenuClickEvent> onClick, @Nullable ItemStack stack) {
        this.onClick = onClick;
        this.stack = stack;
    }

    @Override
    public void onCLick(@NotNull MenuClickEvent trigger) {
        onClick.accept(trigger);
    }

    @Override
    public ItemStack asItemStack() {
        return stack;
    }
}
