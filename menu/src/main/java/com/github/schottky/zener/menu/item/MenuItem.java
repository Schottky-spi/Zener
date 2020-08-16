package com.github.schottky.zener.menu.item;

import com.github.schottky.zener.menu.event.MenuClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an item of a menu.
 * This item is characterized by two functionalities;
 * the display-stack ({@link #asItemStack()}) and the
 * action that will be performed if triggered by a {@link
 * MenuClickEvent}. Per default, the onClick-method
 * will do nothing; only the item-stack will be displayed.
 * This way, this can also be seen as a functional interface:
 * <pre>{@code
 * final Menu menu = ...
 * menu.setItem(2, 2, () -> new ItemStack(Material.SNOWBALL));
 * }</pre>
 * @see DisplayMenuItem
 * @see SimpleMenuItem
 * @see Spacer
 */

@FunctionalInterface
public interface MenuItem {

    default void onClick(@NotNull MenuClickEvent trigger) { /* Do nothing */ }

    @Nullable ItemStack asItemStack();
}
