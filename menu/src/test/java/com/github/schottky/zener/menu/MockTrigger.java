package com.github.schottky.zener.menu;

import com.github.schottky.zener.menu.event.MenuClickEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;

public class MockTrigger {

    public static MenuClickEvent mockEvent(int slot, Menu dut) {
        return new MenuClickEvent(
                null,
                ClickType.LEFT,
                InventoryType.SlotType.OUTSIDE,
                slot,
                InventoryAction.PICKUP_ONE,
                null,
                dut);
    }
}
