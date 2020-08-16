package com.github.schottky.zener.config.bind;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.UUID;

/**
 * Utility-class that encompasses multiple
 * common converters
 */
public final class Converters {

    private Converters() {}

    /**
     * Converts a string to a UUID
     */
    public static class ToUUID implements Convertible<UUID> {

        @Override
        public UUID convertFrom(Object in) {
            Preconditions.checkArgument(in instanceof String);
            return UUID.fromString((String) in);
        }
    }

    /**
     * Converts a string to a Locale
     */

    public static class ToLocale implements Convertible<Locale> {

        @Override
        public Locale convertFrom(Object in) {
            Preconditions.checkArgument(in instanceof String);
            return Locale.forLanguageTag((String) in);
        }
    }

    /**
     * converts a String to a {@link Material}
     */

    public static class ToMaterial implements Convertible<Material> {

        @Override
        public Material convertFrom(Object in) {
            Preconditions.checkArgument(in instanceof String);
            return Material.getMaterial((String) in);
        }
    }

    /**
     * converts a player, given by his name, to the player-object
     */
    public static class NameToPlayer implements Convertible<Player> {

        @Override
        public Player convertFrom(Object in) {
            Preconditions.checkArgument(in instanceof String);
            return Bukkit.getPlayer((String) in);
        }
    }

    /**
     * converts a player, given by his UUID in string-form, to the player-object
     */
    public static class UUIDToPlayer implements Convertible<Player> {

        @Override
        public Player convertFrom(Object in) {
            Preconditions.checkArgument(in instanceof String);
            return Bukkit.getPlayer(UUID.fromString((String) in));
        }
    }
}
