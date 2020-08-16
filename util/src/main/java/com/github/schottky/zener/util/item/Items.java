package com.github.schottky.zener.util.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class Items {

    public static @NotNull ItemStack withoutTitle(Material material) {
        return withTitle(material, " ");
    }

    public static @NotNull ItemStack withTitle(Material material, String title) {
        final ItemStack stack = new ItemStack(material);
        final ItemMeta meta = stack.getItemMeta();
        if (meta == null) return stack;
        meta.setDisplayName(ChatColor.RESET + title);
        stack.setItemMeta(meta);
        return stack;
    }
}
