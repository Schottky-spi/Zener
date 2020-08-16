package com.github.schottky.zener.menu.event;

import com.github.schottky.zener.api.Zener;
import com.github.schottky.zener.menu.Menu;
import com.github.schottky.zener.menu.MenuRegistry;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

public class MenuListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        final InventoryView view = event.getView();

        MenuRegistry.forInventory(view.getTopInventory()).ifPresent(menu -> {
            if (event.getClickedInventory() == view.getTopInventory()) {
                switch (event.getAction()) {
                    case PLACE_ALL:
                    case PLACE_SOME:
                    case PLACE_ONE:
                    case SWAP_WITH_CURSOR:
                        handleMenuTarget(menu, event);
                        break;
                    default:
                        handleMenuClick(menu, event);
                }
            } else {
                handleMenuTarget(menu, event);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryDrag(@NotNull InventoryDragEvent event) {

    }

    private void handleMenuClick(Menu menu, InventoryClickEvent event) {
        final MenuClickEvent menuClickEvent = post(new MenuClickEvent(event, menu));
        if (!menuClickEvent.isCancelled())
            menu.clickSlot(event.getSlot(), menuClickEvent);
        event.setCancelled(!menuClickEvent.canInteractNormally());
    }

    private void handleMenuTarget(Menu menu, @NotNull InventoryClickEvent event) {
        final MenuTargetEvent menuTargetEvent = post(new MenuTargetEvent(event, menu));
        menu.onTarget(menuTargetEvent);
        if (menuTargetEvent.isCancelled()) event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(@NotNull InventoryCloseEvent event) {
        MenuRegistry.forInventory(event.getView().getTopInventory()).ifPresent(menu -> {
            final MenuCloseEvent closeEvent = post(new MenuCloseEvent(menu, event.getPlayer()));
            if (closeEvent.isCancelled()) {
                Bukkit.getScheduler().runTaskLater(Zener.providingPlugin(),
                        () -> menu.openTo(event.getPlayer()), 1);
            }
        });
    }

    private <T extends Event> T post(T event) {
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
}
