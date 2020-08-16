package com.github.schottky.zener.messaging;

import com.github.schottky.zener.api.Zener;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

@API(status = Status.STABLE,
        since = "1.0")
public class Console {

    private static String prefix;

    public static void setPrefix(String prefix) {
        Console.prefix = '[' + prefix + ChatColor.RESET + ']';
    }

    static {
        setPrefix(Zener.isEnabled() ? Zener.providingPlugin().getName() : "Zener");
    }

    private static final ConsoleCommandSender console = Bukkit.getConsoleSender();

    public static void info(Object message, Object... args) {
        log(Level.INFO, message, args);
    }

    public static void severe(Object message, Object... args) {
        log(Level.SEVERE, message, args);
    }

    public static void success(Object message, Object... args) {
        log(Level.SUCCESS, message, args);
    }

    public static void debug(Object message, Object... args) {
        log(Level.DEBUG, message, args);
    }

    public static void warning(Object message, Object... args) {
        log(Level.WARNING, message, args);
    }

    public static void error(@NotNull Throwable e) {
        severe("An unexpected exception occurred:");
        e.printStackTrace();
    }

    public static void log(@NotNull Level level, String message) {
        console.sendMessage(prefix + level.prefix() + message);
    }

    public static void log(@NotNull Level level, Object message, Object... args) {
        log(level, String.format(String.valueOf(message), args));
    }
}
