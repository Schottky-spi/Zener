package com.github.schottky.zener.util.messaging;

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
        return asBuilder().toString();
    }

    public @NotNull StringBuilder asBuilder() {
        StringBuilder builder = new StringBuilder("[");
        if (color != null) builder.append(color);
        builder.append(prefix).append(ChatColor.RESET).append(']');
        return builder;
    }

    @Nullable
    final ChatColor color;

    Level(String prefix, @Nullable ChatColor color) {
        this.prefix = prefix;
        this.color = color;
    }
}
