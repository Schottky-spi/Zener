package com.github.schottky.zener.util.item;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;

/**
 * provides utility-methods to easily work with the lore of an {@link ItemMeta}
 * A {@code Lore} can be created using the constructor or the static factory Methods
 * {@link Lore#of(ItemMeta)} resp. {@link Lore#of(ItemStack)}. Note that the lore is not
 * backed by the stack, so you will have to call either {@link ItemMeta#setLore(List)} or
 * {@link Lore#applyTo(ItemMeta)} resp. {@link Lore#applyTo(ItemStack)} in order to see the effects
 * of the lore.
 */
public class Lore extends AbstractList<String> {

    private final List<String> contents;
    private final boolean resetAtStart;

    /**
     * creates a new lore that is either a copy of the lore of the stack's meta if it is present
     * or a new lore, else
     * @param stack The stack to get the lore for
     * @return The lore, if it is present or an empty lore else
     */
    @Contract("_ -> new")
    public static @NotNull Lore of(@NotNull ItemStack stack) {
        return Lore.of(stack.getItemMeta());
    }

    /**
     * creates a new lore that is either the meta's lore if it is not null and the lore is present,
     * or a new lore else
     * @param meta The meta to get the lore for
     * @return The lore, if it is present or an empty lore else
     */

    @Contract("_ -> new")
    public static @NotNull Lore of(@Nullable ItemMeta meta) {
        if (meta == null || meta.getLore() == null) return new Lore();
        else return new Lore(meta.getLore());
    }

    public Lore(Iterable<String> lore) {
        this(true, lore);
    }

    public Lore(String... lore) {
        this(true, lore);
    }

    public Lore(boolean resetAtStart, @NotNull Iterable<String> lore) {
        this.resetAtStart = resetAtStart;
        this.contents = new ArrayList<>();
        lore.forEach(this::add);
    }

    public Lore(boolean resetAtStart, String... lore) {
        this(resetAtStart, Arrays.asList(lore));
    }

    @Override
    public String get(int index) { return contents.get(index); }

    @Override
    public void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
    }

    @Override
    public boolean remove(Object o) {
        return contents.remove(o);
    }

    @Override
    public String remove(int index) {
        return contents.remove(index);
    }

    @Override
    public String set(int index, String element) {
        return contents.set(index, resetAtStart ? ChatColor.RESET + element : element);
    }

    public void remove(Pattern pattern) {
        contents.removeIf(s -> pattern.matcher(s).matches());
    }

    public void addAll(String @NotNull ... contents) {
        Collections.addAll(this, contents);
    }

    public void addAll(int index, String @NotNull ... contents) {
        this.addAll(index, Arrays.asList(contents));
    }

    @Override
    public void add(int index, String element) {
        if (resetAtStart) contents.add(index, ChatColor.RESET + element);
        else contents.add(index, element);
    }

    @Override
    public boolean add(String s) {
        if (resetAtStart) return contents.add(ChatColor.RESET + s);
        else return contents.add(s);
    }

    public void applyTo(@NotNull ItemMeta meta) {
        meta.setLore(contents);
    }

    public void applyTo(@NotNull ItemStack stack) {
        if (stack.getItemMeta() == null) return;
        final ItemMeta meta = stack.getItemMeta();
        applyTo(meta);
        stack.setItemMeta(meta);
    }

    @Override
    public int size() {
        return contents.size();
    }

    public Lore duplicate() {
        return new Lore(this.resetAtStart, this);
    }
}
