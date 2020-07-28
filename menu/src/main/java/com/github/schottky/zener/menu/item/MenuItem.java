package com.github.schottky.zener.menu.item;

import com.github.schottky.zener.menu.event.MenuClickEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface MenuItem {

    default void onCLick(@NotNull MenuClickEvent trigger) {
        if (trigger.clicker instanceof Player) {
            ((Player) trigger.clicker).playSound(trigger.clicker.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
        }
    }

    @Nullable ItemStack asItemStack();
}
