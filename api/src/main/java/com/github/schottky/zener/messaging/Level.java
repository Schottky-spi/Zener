package com.github.schottky.zener.messaging;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum Level {

    INFO("INFO", null),
    WARNING("WARNING", ChatColor.YELLOW),
    SEVERE("SEVERE", ChatColor.RED),
    SUCCESS("SUCCESS", ChatColor.GREEN),
    DEBUG("DEBUG", ChatColor.AQUA);

    private final String prefix;

    public @NotNull String prefix() {
        return prefix;
    }

    Level(String prefix, @Nullable ChatColor color) {
        final StringBuilder builder = new StringBuilder("[");
        if (color != null) builder.append(color);
        builder.append(prefix).append(ChatColor.RESET).append(']');
        this.prefix = builder.toString();
    }
}
