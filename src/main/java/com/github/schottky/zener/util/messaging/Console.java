package com.github.schottky.zener.util.messaging;

import com.github.schottky.zener.api.ZenerAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class Console {

    private static String prefix;

    public static void setPrefix(String prefix) {
        Console.prefix = '[' + prefix + ChatColor.RESET + ']';
    }

    static {
        setPrefix(ZenerAPI.isEnabled() ? ZenerAPI.providingPlugin().getName() : "Zener");
    }

    private static final ConsoleCommandSender console = Bukkit.getConsoleSender();

    public static void info(String message) {
        log(Level.INFO, message);
    }

    public static void severe(String message) {
        log(Level.SEVERE, message);
    }

    public static void success(String message) {
        log(Level.SUCCESS, message);
    }

    public static void debug(String message) {
        log(Level.DEBUG, message);
    }

    public static void warning(String message) {
        log(Level.WARNING, message);
    }

    public static void error(@NotNull Exception e) {
        severe("An unexpected exception occurred:");
        e.printStackTrace();
    }

    public static void log(@NotNull Level level, String message) {
        console.sendMessage(prefix + level.prefix() + message);
    }
}
