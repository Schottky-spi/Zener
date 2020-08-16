package com.github.schottky.zener.menu.item;

import com.github.schottky.zener.menu.event.MenuClickEvent;
import com.github.schottky.zener.util.item.Items;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * A simple menu-item that does nothing on click
 * and will display a nameless stained glass-pane
 * with the color defined in the constructor
 */

public class Spacer implements MenuItem {

    private final Material spacerMaterial;

    /**
     * creates a new spacer with a defined color
     * @param color The color that this spacer will have
     */
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
    public void onClick(@NotNull MenuClickEvent trigger) { /* Do nothing */ }

    @Override
    public ItemStack asItemStack() {
        return Items.withoutTitle(spacerMaterial);
    }
}
