package com.github.schottky.zener.command;

import com.github.schottky.zener.api.ZenerAPI;
import com.github.schottky.zener.util.messaging.Console;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Commands {

    public static void registerAll(CommandBase @NotNull ... commands) {
        final JavaPlugin plugin = ZenerAPI.providingPlugin();
        for (CommandBase base: commands) {
            final PluginCommand command = plugin.getCommand(base.name);
            if (command == null) {
                Console.severe("Command " + base.name + " is not registered properly in the plugin.yml");
            } else {
                command.setExecutor(base);
                command.setTabCompleter(base);
            }
        }
    }
}
