package com.github.schottky.zener.command.resolver;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class ArgumentResolvers {

    private static final Map<Class<?>, ArgumentResolver<?>> argumentResolvers = new HashMap<>();

    public static ArgumentResolver<Boolean> BOOLEAN_RESOLVER = arg -> {
        if ("true".equalsIgnoreCase(arg))
            return true;
        else if ("false".equalsIgnoreCase(arg))
            return false;
        else
            throw ArgumentNotResolvable.withMessage("arg not a boolean");
    };

    public static ArgumentResolver<Integer> INT_RESOLVER = arg -> {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw ArgumentNotResolvable.withMessage("arg not of type int");
        }
    };

    public static ArgumentResolver<Double> DOUBLE_RESOLVER = arg -> {
        try {
            return Double.parseDouble(arg);
        } catch (NumberFormatException e) {
            throw ArgumentNotResolvable.withMessage("arg not of type double");
        }
    };

    public static ArgumentResolver<String> STRING_RESOLVER = arg -> arg;

    // there's no way around this
    @SuppressWarnings("deprecation")
    public static ArgumentResolver<OfflinePlayer> OFFLINE_PLAYER_RESOLVER = arg -> {
        final OfflinePlayer op = Bukkit.getOfflinePlayer(arg);
        if (!op.hasPlayedBefore())
            throw ArgumentNotResolvable.withMessage("Offline player not found");
        return op;
    };

    public static ArgumentResolver<Player> PLAYER_RESOLVER = arg -> {
        final Player player = Bukkit.getPlayer(arg);
        if (player == null)
            throw ArgumentNotResolvable.withMessage("Player not online");
        return player;
    };

    public static ArgumentResolver<NamespacedKey> NAMESPACED_KEY_RESOLVER = arg -> {
        final String[] components = arg.split(":");
        if (components.length != 2)
            throw ArgumentNotResolvable.withMessage("Argument not a namespacedKey");
        if (components[0].equalsIgnoreCase("minecraft")) {
            try {
                return NamespacedKey.minecraft(components[1].toLowerCase());
            } catch (IllegalArgumentException e) {
                throw ArgumentNotResolvable.fromException(e);
            }
        } else {
            final Plugin plugin = Bukkit.getPluginManager().getPlugin(components[0]);
            if (plugin == null)
                throw ArgumentNotResolvable.withMessage("Plugin does not exist");
            try {
                return new NamespacedKey(plugin, components[1]);
            } catch (IllegalArgumentException e) {
                throw ArgumentNotResolvable.fromException(e);
            }
        }
    };

    static {
        register(Boolean.class, BOOLEAN_RESOLVER);
        register(boolean.class, BOOLEAN_RESOLVER);
        register(Integer.class, INT_RESOLVER);
        register(int.class, INT_RESOLVER);
        register(Double.class, DOUBLE_RESOLVER);
        register(double.class, DOUBLE_RESOLVER);
        register(String.class, STRING_RESOLVER);
        register(OfflinePlayer.class, OFFLINE_PLAYER_RESOLVER);
        register(Player.class, PLAYER_RESOLVER);
        register(NamespacedKey.class, NAMESPACED_KEY_RESOLVER);
    }

    public static void register(Class<?> clazz, ArgumentResolver<?> argumentResolver) {
        argumentResolvers.put(clazz, argumentResolver);
    }

    public static ArgumentResolver<?> forClass(Class<?> clazz) {
        return argumentResolvers.get(clazz);
    }
}
