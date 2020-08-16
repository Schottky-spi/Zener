package com.github.schottky.zener.menu;

import com.github.schottky.zener.api.SubModule;
import com.github.schottky.zener.api.Zener;
import com.github.schottky.zener.menu.event.MenuListeners;
import org.apiguardian.api.API;
import org.bukkit.Bukkit;


@API(status = API.Status.INTERNAL)
public class MenuSubModule implements SubModule {

    @Override
    public void init() {
        Bukkit.getPluginManager().registerEvents(new MenuListeners(), Zener.providingPlugin());
    }

    @Override
    public void shutdown() {
        MenuRegistry.shutDown();
    }
}
