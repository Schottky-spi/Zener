package com.github.schottky.zener.util.item;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * Static utility methods that simplify calls like
 * <pre>{@code
 * ItemStack stack;
 * Integer value = Objects.requireNonNull(stack.getItemMeta()).getPersistentDataContainer().get(PersistentDataType.INTEGER, key);
 * if (value != null) // work with the value
 * }</pre>
 * to
 * <pre>{@code
 * ItemStack stack;
 * ItemStorage.getInt(stack.getItemMeta(), key).ifPresent(integer -> // work with the value)
 * }</pre>
 */

public final class ItemStorage {

    private ItemStorage() {}

    //---------------------------------------------------Primitives-----------------------------------------------------

    /**
     * get an optional int for a certain meta and a certain key
     * @param meta The meta to get the int for
     * @param key The key
     * @return An OptionalInt containing the desired int if the meta isn't null and the int is present,
     * an empty optional else
     */

    public static OptionalInt getInt(@Nullable ItemMeta meta, NamespacedKey key) {
        Integer value = get(meta, PersistentDataType.INTEGER, key);
        if (value == null) return OptionalInt.empty();
        return OptionalInt.of(value);
    }

    /**
     * get an optional double for a certain meta and a certain key
     * @param meta The meta to get the double for
     * @param key The key
     * @return An OptionalDouble containing the desired double if the meta isn't null and the double is present,
     * an empty optional else
     */

    public static OptionalDouble getDouble(@Nullable ItemMeta meta, NamespacedKey key) {
        Double value = get(meta, PersistentDataType.DOUBLE, key);
        if (value == null) return OptionalDouble.empty();
        return OptionalDouble.of(value);
    }

    /**
     * get an optional long for a certain meta and a certain key
     * @param meta The meta to get the long for
     * @param key The key
     * @return An OptionalLong containing the desired long if the meta isn't null and the long is present,
     * an empty optional else
     */

    public static OptionalLong getLong(@Nullable ItemMeta meta, NamespacedKey key) {
        Long value = get(meta, PersistentDataType.LONG, key);
        if (value == null) return OptionalLong.empty();
        return OptionalLong.of(value);
    }

    public static Optional<String> getString(@Nullable ItemMeta meta, NamespacedKey key) {
        return Optional.ofNullable(get(meta, PersistentDataType.STRING, key));
    }

    //----------------------------------------------------Defaults------------------------------------------------------

    /**
     * get an int for a certain meta and key
     * @param meta The meta to get the int for
     * @param key The key
     * @param def The default-value, if it wasn't present
     * @return The int inside the meta, if it isn't null and contains the int, the default-value otherwise
     */

    public static int getInt(@Nullable ItemMeta meta, NamespacedKey key, int def) {
        return getInt(meta, key).orElse(def);
    }

    public static double getDouble(@Nullable ItemMeta meta, NamespacedKey key, double def) {
        return getDouble(meta, key).orElse(def);
    }

    public static long getLong(@Nullable ItemMeta meta, NamespacedKey key, long def) {
        return getLong(meta, key).orElse(def);
    }

    public static String getString(@Nullable ItemMeta meta, NamespacedKey key, String def) {
        return getString(meta, key).orElse(def);
    }

    //-----------------------------------------------------Setters------------------------------------------------------

    public static void set(@Nullable ItemMeta meta, int i, NamespacedKey key) {
        set(meta, PersistentDataType.INTEGER, key, i);
    }

    public static void set(@Nullable ItemMeta meta, double d, NamespacedKey key) {
        set(meta, PersistentDataType.DOUBLE, key, d);
    }

    public static void set(@Nullable ItemMeta meta, long l, NamespacedKey key) {
        set(meta, PersistentDataType.LONG, key, l);
    }

    public static void set(@Nullable ItemMeta meta, String s, NamespacedKey key) {
        set(meta, PersistentDataType.STRING, key, s);
    }

    //-----------------------------------------------------Checkers-----------------------------------------------------

    public static boolean hasInt(@Nullable ItemMeta meta, NamespacedKey key) {
        return has(meta, PersistentDataType.INTEGER, key);
    }

    public static boolean hasDouble(@Nullable ItemMeta meta, NamespacedKey key) {
        return has(meta, PersistentDataType.DOUBLE, key);
    }

    public static boolean hasLong(@Nullable ItemMeta meta, NamespacedKey key) {
        return has(meta, PersistentDataType.LONG, key);
    }

    public static boolean hasString(@Nullable ItemMeta meta, NamespacedKey key) {
        return has(meta, PersistentDataType.STRING, key);
    }

    //--------------------------------------------------Non-primitives--------------------------------------------------

    public static <T,Z> @Nullable Z get(@Nullable ItemMeta meta, PersistentDataType<T,Z> type, NamespacedKey key) {
        return meta == null ? null : meta.getPersistentDataContainer().get(key, type);
    }

    public static <T,Z> void set(@Nullable ItemMeta meta, PersistentDataType<T,Z> type, NamespacedKey key, Z value) {
        if (meta != null) meta.getPersistentDataContainer().set(key, type, value);
    }

    public static <T,Z> boolean has(@Nullable ItemMeta meta, PersistentDataType<T,Z> type, NamespacedKey key) {
        return meta != null && meta.getPersistentDataContainer().has(key, type);
    }

    public static void remove(@Nullable ItemMeta meta, NamespacedKey key) {
        if (meta != null) meta.getPersistentDataContainer().remove(key);
    }

}
