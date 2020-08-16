package com.github.schottky.zener.menu;

import org.apiguardian.api.API;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@API(status = API.Status.INTERNAL)
public class MenuRegistry {

    private static final Map<Inventory,Menu> activeMenus = new HashMap<>();

    public static void registerNewMenu(Inventory inv, Menu menu) {
        activeMenus.put(inv, menu);
    }

    public static Optional<Menu> forInventory(Inventory inv) {
        return Optional.ofNullable(activeMenus.get(inv));
    }

    public static boolean isMenu(Inventory inventory) {
        return activeMenus.containsKey(inventory);
    }

    public static void shutDown() {
        activeMenus.values().forEach(menu -> menu.close(true));
        activeMenus.clear();
    }
}
