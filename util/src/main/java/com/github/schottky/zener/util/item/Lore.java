package com.github.schottky.zener.util.item;

import com.github.schottky.zener.util.range.Range;
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
public class Lore extends AbstractList<String> implements Collection<String> {

    private final List<String> contents = new ArrayList<>();
    private boolean resetAtStart = true;

    private final static String reset = ChatColor.RESET.toString();

    public boolean resetsAtStart() {
        return resetAtStart;
    }

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

    /**
     * returns this lore where no element will start with a 'reset'-special string.
     * Every subsequent add or set-command will append the equivalent {@link ChatColor#RESET}-string
     * <br> A typical application could be
     * <pre>{@code
     * Lore lore = new Lore("foo", "bar").thatDoesNotResetAtStart();
     * }</pre>
     * @return This lore
     */

    public Lore thatDoesNotResetAtStart() {
        if (this.resetAtStart)
            contents.replaceAll(s -> s.startsWith(reset) ? s.substring(reset.length()) : s);
        this.resetAtStart = false;
        return this;
    }

    /**
     * returns this lore where every element will not start with a 'reset'-special string.
     * Every add or set-command will append the string as it is (resp. translate it)
     * @return This lore
     */

    public Lore thatResetsAtStart() {
        if (!this.resetAtStart)
            contents.replaceAll(s -> s.startsWith(reset) ? s : reset + s);
        this.resetAtStart = true;
        return this;
    }

    public Lore(@NotNull Iterable<String> lore) {
        lore.forEach(this::add);
    }

    public Lore(String... lore) {
        this(Arrays.asList(lore));
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public String get(int index) { return contents.get(index); }

    /**
     * {@inheritDoc}
     */

    @Override
    public void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public boolean remove(Object o) {
        return contents.remove(o);
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public String remove(int index) {
        return contents.remove(index);
    }

    /**
     * removes a certain (possibly pre-compiled) pattern from this lore. For example, removing the pattern
     * <pre>{@code
     * Pattern pattern = Pattern.compile(Pattern.quote("Sword of asparagus"));
     * }</pre>
     * from the lore
     * <pre>{@code
     * Lore lore = new Lore("Sword of asparagus 1", "Sword of cucumbers", "Sword of asparagus 2");
     * }</pre>
     * would result in the new lore ["Sword of cucumbers"]
     * @param pattern The pattern to remove each lore-line of
     */

    public void remove(Pattern pattern) {
        contents.removeIf(s -> pattern.matcher(s).find());
    }

    /**
     * Replaces all occurrences of a given pattern to the resulting string
     * @param from The pattern to replace
     * @param to The string to replace it with
     */
    public void replaceAll(Pattern from, String to) {
        this.replaceAll(s -> from.matcher(s).find() ? to : s);
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public String set(int index, String element) {
        return contents.set(index, resetAtStart && !element.startsWith(reset) ? reset + element : element);
    }

    /**
     * adds all of the specified elements to this collection
     * @param elements The elements to add
     */

    public void addAll(String @NotNull ... elements) {
        Collections.addAll(this, elements);
    }

    /**
     * adds all of the specified elements to this collection at a certain index
     * @param index where to add the elements
     * @param elements The elements to add
     */

    public void addAll(int index, String @NotNull ... elements) {
        this.addAll(index, Arrays.asList(elements));
    }

    /**
     * {@inheritDoc}
     */

    @NotNull
    @Override
    public String[] toArray() {
        String[] arr = new String[contents.size()];
        for (int i: Range.indexing(arr)) {
            arr[i] = contents.get(i);
        }
        return arr;
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public void add(int index, String element) {
        contents.add(index, resetAtStart && !element.startsWith(reset) ? reset + element : element);
    }

    /**
     * applies this lore to a certain meta. This has the same effect as calling
     * {@link ItemMeta#setLore(List)} using this as a list
     * @param meta The meta to set the lore of
     */

    public void applyTo(@NotNull ItemMeta meta) {
        meta.setLore(contents);
    }

    /**
     * applies this lore to a certain item-stack.
     * If the stack does not have an {@link ItemMeta}, this method will
     * not change the stack
     * @param stack The stack to add the lore to
     */

    public void applyTo(@NotNull ItemStack stack) {
        if (stack.getItemMeta() == null) return;
        final ItemMeta meta = stack.getItemMeta();
        applyTo(meta);
        stack.setItemMeta(meta);
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public int size() {
        return contents.size();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Lore lore = (Lore) object;
        return contents.equals(lore.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contents);
    }

    /**
     * returns a duplicate lore
     * @return A new lore that is equivalent to this lore
     */

    public Lore duplicate() {
        Lore duplicateLore = new Lore();
        duplicateLore.resetAtStart = this.resetAtStart;
        duplicateLore.contents.addAll(this);
        return duplicateLore;
    }
}
