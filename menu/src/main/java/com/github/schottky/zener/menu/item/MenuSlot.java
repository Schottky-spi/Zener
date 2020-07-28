package com.github.schottky.zener.menu.item;

import com.github.schottky.zener.menu.event.MenuTargetEvent;

public interface MenuSlot extends MenuItem {

    void onTarget(MenuTargetEvent trigger);

}
