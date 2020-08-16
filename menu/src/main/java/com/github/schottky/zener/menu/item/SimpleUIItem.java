package com.github.schottky.zener.menu.item;

import com.github.schottky.zener.menu.event.MenuClickEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * An item that will play a click-sound, additionally to performing a given action
 */
public class SimpleUIItem extends SimpleMenuItem {

    public SimpleUIItem(@NotNull Consumer<MenuClickEvent> onClick, @Nullable ItemStack stack) {
        super(onClick, stack);
    }

    public SimpleUIItem(@NotNull Runnable onClick, ItemStack stack) {
        super(onClick, stack);
    }

    @Override
    public void onClick(@NotNull MenuClickEvent event) {
        if (event.clicker instanceof Player) {
            ((Player) event.clicker).playSound(event.clicker.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
        }
        super.onClick(event);
    }
}
