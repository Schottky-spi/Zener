package com.github.schottky.zener.menu.item;

import com.github.schottky.zener.menu.event.MenuClickEvent;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Spacer implements MenuItem {

    private final Material spacerMaterial;

    @Contract(pure = true)
    public Spacer(@NotNull DyeColor color) {
        switch (color) {
            case WHITE:
                this.spacerMaterial = Material.WHITE_STAINED_GLASS_PANE;
                break;
            case ORANGE:
                this.spacerMaterial = Material.ORANGE_STAINED_GLASS_PANE;
                break;
            case MAGENTA:
                this.spacerMaterial = Material.MAGENTA_STAINED_GLASS_PANE;
                break;
            case LIGHT_BLUE:
                this.spacerMaterial = Material.LIGHT_BLUE_STAINED_GLASS_PANE;
                break;
            case YELLOW:
                this.spacerMaterial = Material.YELLOW_STAINED_GLASS_PANE;
                break;
            case LIME:
                this.spacerMaterial = Material.LIME_STAINED_GLASS_PANE;
                break;
            case PINK:
                this.spacerMaterial = Material.PINK_STAINED_GLASS_PANE;
                break;
            case GRAY:
                this.spacerMaterial = Material.GRAY_STAINED_GLASS_PANE;
                break;
            case LIGHT_GRAY:
                this.spacerMaterial = Material.LIGHT_GRAY_STAINED_GLASS_PANE;
                break;
            case CYAN:
                this.spacerMaterial = Material.CYAN_STAINED_GLASS_PANE;
                break;
            case PURPLE:
                this.spacerMaterial = Material.PURPLE_STAINED_GLASS_PANE;
                break;
            case BLUE:
                this.spacerMaterial = Material.BLUE_STAINED_GLASS_PANE;
                break;
            case BROWN:
                this.spacerMaterial = Material.BROWN_STAINED_GLASS_PANE;
                break;
            case GREEN:
                this.spacerMaterial = Material.GREEN_STAINED_GLASS_PANE;
                break;
            case RED:
                this.spacerMaterial = Material.RED_STAINED_GLASS_PANE;
                break;
            case BLACK:
            default:
                this.spacerMaterial = Material.BLACK_STAINED_GLASS_PANE;
                break;
        }
    }

    @Override
    public void onCLick(@NotNull MenuClickEvent trigger) { /* Do nothing */ }

    @Override
    public ItemStack asItemStack() {
        ItemStack stack = new ItemStack(spacerMaterial);
        final ItemMeta meta = Objects.requireNonNull(stack.getItemMeta());
        meta.setDisplayName(" ");
        stack.setItemMeta(meta);
        return stack;
    }
}
