package com.github.schottky.zener.command.util;

import com.github.schottky.zener.api.Zener;
import com.github.schottky.zener.command.CommandBase;
import com.github.schottky.zener.messaging.Console;
import org.apiguardian.api.API;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@API(status = API.Status.EXPERIMENTAL)
public class Commands {

    public static void registerAll(CommandBase @NotNull ... commands) {
        final JavaPlugin plugin = Zener.providingPlugin();
        for (CommandBase base: commands) {
            final PluginCommand command = plugin.getCommand(base.name());
            if (command == null) {
                Console.severe("Command %s is not registered in the plugin.yml", base.name());
            } else {
                command.setExecutor(base);
                command.setTabCompleter(base);
            }
        }
    }
}
