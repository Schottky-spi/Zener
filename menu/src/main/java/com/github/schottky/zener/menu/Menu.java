package com.github.schottky.zener.menu;

import com.github.schottky.zener.menu.event.MenuClickEvent;
import com.github.schottky.zener.menu.event.MenuTargetEvent;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

public interface Menu {

    int MAX_ROWS = 6;

    void clickSlot(int slot, @NotNull MenuClickEvent trigger);

    void onTarget(MenuTargetEvent trigger);

    int rows();

    int columns();

    void openTo(@NotNull HumanEntity player);

    void close();
}
