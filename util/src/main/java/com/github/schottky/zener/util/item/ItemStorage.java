package com.github.schottky.zener.util.item;

import com.github.schottky.zener.api.Zener;
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
 * Integer value = Objects.requireNonNull(stack.getItemMeta()).getPersistentDataContainer().get(PersistentDataType.INTEGER, new NamespacedKey(plugin, ident));
 * if (value != null) // work with the value
 * }</pre>
 * to
 * <pre>{@code
 * ItemStack stack;
 * ItemStorage.getInt(stack.getItemMeta(), ident).ifPresent(integer -> // work with the value)
 * }</pre>
 * or other, equivalently simple versions that use default-values:
 * <pre>{@code
 * ItemStack stack;
 * int i = ItemStorage.get(stack.getItemMeta(), ident, 0)
 * }</pre>
 */

public final class ItemStorage {

    private ItemStorage() {}

    //---------------------------------------------------Primitives-----------------------------------------------------

    /**
     * get an optional int for a certain meta and a certain key
     * @param meta The meta to get the int for
     * @param ident The key
     * @return An OptionalInt containing the desired int if the meta isn't null and the int is present,
     * an empty optional else
     */

    public static OptionalInt getInt(@Nullable ItemMeta meta, String ident) {
        Integer value = get(meta, PersistentDataType.INTEGER, ident);
        if (value == null) return OptionalInt.empty();
        return OptionalInt.of(value);
    }

    /**
     * get an optional double for a certain meta and a certain key
     * @param meta The meta to get the double for
     * @param ident The key
     * @return An OptionalDouble containing the desired double if the meta isn't null and the double is present,
     * an empty optional else
     */

    public static OptionalDouble getDouble(@Nullable ItemMeta meta, String ident) {
        Double value = get(meta, PersistentDataType.DOUBLE, ident);
        if (value == null) return OptionalDouble.empty();
        return OptionalDouble.of(value);
    }

    /**
     * get an optional long for a certain meta and a certain key
     * @param meta The meta to get the long for
     * @param ident The key
     * @return An OptionalLong containing the desired long if the meta isn't null and the long is present,
     * an empty optional else
     */

    public static OptionalLong getLong(@Nullable ItemMeta meta, String ident) {
        Long value = get(meta, PersistentDataType.LONG, ident);
        if (value == null) return OptionalLong.empty();
        return OptionalLong.of(value);
    }

    public static Optional<String> getString(@Nullable ItemMeta meta, String ident) {
        return Optional.ofNullable(get(meta, PersistentDataType.STRING, ident));
    }

    public static Optional<Byte> getByte(@Nullable ItemMeta meta, String ident) {
        return Optional.ofNullable(get(meta, PersistentDataType.BYTE, ident));
    }

    //----------------------------------------------------Defaults------------------------------------------------------

    /**
     * get an int for a certain meta and key
     * @param meta The meta to get the int for
     * @param ident The key
     * @param def The default-value, if it wasn't present
     * @return The int inside the meta, if it isn't null and contains the int, the default-value otherwise
     */

    public static int getInt(@Nullable ItemMeta meta, String ident, int def) {
        return getInt(meta, ident).orElse(def);
    }

    public static double getDouble(@Nullable ItemMeta meta, String ident, double def) {
        return getDouble(meta, ident).orElse(def);
    }

    public static long getLong(@Nullable ItemMeta meta, String ident, long def) {
        return getLong(meta, ident).orElse(def);
    }

    public static String getString(@Nullable ItemMeta meta, String ident, String def) {
        return getString(meta, ident).orElse(def);
    }

    public static Byte getByte(@Nullable ItemMeta meta, String ident, byte def) {
        return getByte(meta, ident).orElse(def);
    }

    //-----------------------------------------------------Setters------------------------------------------------------

    public static void set(@Nullable ItemMeta meta, int i, String ident) {
        set(meta, PersistentDataType.INTEGER, ident, i);
    }

    public static void set(@Nullable ItemMeta meta, double d, String ident) {
        set(meta, PersistentDataType.DOUBLE, ident, d);
    }

    public static void set(@Nullable ItemMeta meta, long l, String ident) {
        set(meta, PersistentDataType.LONG, ident, l);
    }

    public static void set(@Nullable ItemMeta meta, String s, String ident) {
        set(meta, PersistentDataType.STRING, ident, s);
    }

    public static void set(@Nullable ItemMeta meta, byte b, String ident) {
        set(meta, PersistentDataType.BYTE, ident, b);
    }

    //-----------------------------------------------------Checkers-----------------------------------------------------

    public static boolean hasInt(@Nullable ItemMeta meta, String ident) {
        return has(meta, PersistentDataType.INTEGER, ident);
    }

    public static boolean hasDouble(@Nullable ItemMeta meta, String ident) {
        return has(meta, PersistentDataType.DOUBLE, ident);
    }

    public static boolean hasLong(@Nullable ItemMeta meta, String ident) {
        return has(meta, PersistentDataType.LONG, ident);
    }

    public static boolean hasString(@Nullable ItemMeta meta, String ident) {
        return has(meta, PersistentDataType.STRING, ident);
    }

    public static boolean hasByte(@Nullable ItemMeta meta, String ident) {
        return has(meta, PersistentDataType.BYTE, ident);
    }

    //--------------------------------------------------Non-primitives--------------------------------------------------

    public static <T,Z> @Nullable Z get(@Nullable ItemMeta meta, PersistentDataType<T,Z> type, String ident) {
        return meta == null ?
                null : meta.getPersistentDataContainer().get(Zener.key(ident), type);
    }

    public static <T,Z> void set(@Nullable ItemMeta meta, PersistentDataType<T,Z> type, String ident, Z value) {
        if (meta != null) meta.getPersistentDataContainer().set(Zener.key(ident), type, value);
    }

    public static <T,Z> boolean has(@Nullable ItemMeta meta, PersistentDataType<T,Z> type, String ident) {
        return meta != null && meta.getPersistentDataContainer().has(Zener.key(ident), type);
    }

    public static void remove(@Nullable ItemMeta meta, String ident) {
        if (meta != null) meta.getPersistentDataContainer().remove(Zener.key(ident));
    }

}
