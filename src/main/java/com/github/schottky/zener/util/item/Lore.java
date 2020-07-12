package com.github.schottky.zener.util.item;

import com.github.schottky.zener.localization.Language;
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
    private final String reset = ChatColor.RESET.toString();

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
     * returns a new lore that contains the given raw (unlocalized) elements
     * @param rawElements The raw elements to add
     * @return The newly created lore containing the given elements
     */

    public static @NotNull Lore newLoreWithRawElements(String @NotNull ... rawElements) {
        Lore lore = new Lore();
        for (String element: rawElements) lore.addRaw(element);
        return lore;
    }

    /**
     * returns a new lore that contains the given raw (unlocalized) elements
     * @param rawElements The raw elements to add
     * @return The newly created lore containing the given elements
     */

    public static @NotNull Lore newLoreWithRawElements(@NotNull Iterable<String> rawElements) {
        Lore lore = new Lore();
        rawElements.forEach(lore::addRaw);
        return lore;
    }

    /**
     * returns this lore where no element will start with a 'reset'-special string.
     * Every subsequent add or set-command will append the equivalent {@link ChatColor#RESET}-string
     * @return This lore
     */

    public Lore thatDoesNotResetAtStart() {
        if (size() > 0 && this.resetAtStart)
            contents.replaceAll(s -> s.substring(reset.length()));
        this.resetAtStart = false;
        return this;
    }

    /**
     * returns this lore where every element will not start with a 'reset'-special string.
     * Every add or set-command will append the string as it is (resp. translate it)
     * @return This lore
     */

    public Lore thatResetsAtStart() {
        if (size() > 0 && !this.resetAtStart)
            contents.replaceAll(s -> reset + s);
        this.resetAtStart = true;
        return this;
    }

    public Lore(@NotNull Iterable<String> lore) {
        lore.forEach(this::add);
    }

    public Lore(String... lore) {
        this(Arrays.asList(lore));
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
    public String set(int index, String translationKey) {
        return setRaw(index, Language.current().translate(translationKey));
    }

    /**
     * sets the contents of the lore at the specified index to a certain value.
     * This value will not be translated
     * @param index The index to set the element to
     * @param element The element to set
     * @return The element previously at the specified position
     */

    public String setRaw(int index, String element) {
        return contents.set(index, resetAtStart ? reset + element : element);
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
     * adds all of the specified elements to this collection
     * @param elements The elements to add
     */

    public void addAll(String @NotNull ... elements) {
        Collections.addAll(this, elements);
    }

    /**
     * adds all of the specified elements raw to this collection (without translating them first)
     * @param elements The elements to add
     */

    public void addAllRaw(String @NotNull... elements) {
        for (String content: elements) {
            this.addRaw(content);
        }
    }

    /**
     * adds all of the specified elements to this collection at a certain index
     * @param elements The elements to add
     */

    public void addAll(int index, String @NotNull ... elements) {
        this.addAll(index, Arrays.asList(elements));
    }

    /**
     * adds all of the specified elements raw to this collection (without translating them first) at a certain index
     * @param elements The elements to add
     */

    public void addAllRaw(int index, String @NotNull... elements) {
        for (String content: elements) {
            this.add(index++, content);
        }
    }

    @Override
    public void add(int index, String element) {
        addRaw(index, Language.current().translate(element));
    }

    /**
     * Adds a certain element to this list at a certain position
     * @param index The index to add the element to
     * @param element The element to add
     * @see List#add(Object)
     */

    public void addRaw(int index, String element) {
        if (resetAtStart) contents.add(index, reset + element);
        else contents.add(index, element);
    }

    @Override
    public boolean add(String s) {
        return addRaw(Language.current().translate(s));
    }

    /**
     * Adds a certain element to the head of this list
     * @param s The element to add
     * @see List#add(Object)
     */

    public boolean addRaw(String s) {
        if (resetAtStart) return contents.add(reset + s);
        else return contents.add(s);
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
