package com.github.schottky.zener.menu.slotted;

import com.github.schottky.zener.menu.AbstractMenu;
import com.github.schottky.zener.menu.event.MenuTargetEvent;
import com.github.schottky.zener.menu.item.MenuItem;
import com.github.schottky.zener.menu.item.MenuSlot;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class AbstractSlottedMenu extends AbstractMenu implements SlottedMenu {

    /**
     * creates a menu with the given number of rows and the title.
     * The rows may not exceed {@link com.github.schottky.zener.menu.Menu#MAX_ROWS} and may not be
     * smaller than 1
     *
     * @param rows  The amount of rows that should be allocated
     * @param title The title of this menu
     */

    public AbstractSlottedMenu(int rows, @NotNull String title) {
        super(rows, title);
    }

    private final Table<Integer,Integer,MenuSlot> slots = HashBasedTable.create();

    private boolean isSlot(int clickedSlot) {
        int y = clickedSlot / columnCount();
        int x = clickedSlot - y * columnCount();
        return slots.contains(x, y);
    }

    private Optional<MenuSlot> clickedSlot(int clickedSlot) {
        int y = clickedSlot / columnCount();
        int x = clickedSlot - y * columnCount();
        return Optional.ofNullable(slots.get(x, y));
    }

    @Override
    protected void setItem(int slotX, int slotY, MenuItem item) {
        if (item instanceof MenuSlot) {
            slots.put(slotX, slotY, (MenuSlot) item);
        }
        super.setItem(slotX, slotY, item);
    }

    @Override
    public void onTarget(@NotNull MenuTargetEvent event) {
        if (slots.isEmpty()) return;
        switch (event.getAction()) {
            case NOTHING:
            case PICKUP_ALL:
            case PICKUP_SOME:
            case PICKUP_HALF:
            case PICKUP_ONE:
            case DROP_ALL_CURSOR:
            case DROP_ONE_CURSOR:
            case DROP_ALL_SLOT:
            case DROP_ONE_SLOT:
            case CLONE_STACK:
            case COLLECT_TO_CURSOR:
            case UNKNOWN:
                break;
            case PLACE_ALL:
            case PLACE_SOME:
            case PLACE_ONE:
                clickedSlot(event.getSlot()).ifPresent(menuSlot -> {
                    final boolean result = menuSlot.offer(event.getCursor());
                    event.setCancelled(result);
                });
                break;
            case SWAP_WITH_CURSOR:
                if (event.getCurrentItem() == null) break;
                clickedSlot(event.getSlot()).ifPresent(menuSlot -> {
                    final boolean result = menuSlot.take(event.getCurrentItem().getAmount())
                            && menuSlot.offer(event.getCursor());
                    event.setCancelled(result);
                });
                break;
            case MOVE_TO_OTHER_INVENTORY:
                if (event.getClickedInventory() != event.getView().getTopInventory()) {
                    final ItemStack toDistribute = event.getCurrentItem();
                    distributeStack(toDistribute);
                    event.setCurrentItem(toDistribute);
                    event.setCancelled(true);
                }
                break;
            case HOTBAR_MOVE_AND_READD:
            case HOTBAR_SWAP:
                clickedSlot(event.getSlot()).ifPresent(menuSlot -> {
                    final ItemStack hotbar = event.getView().getBottomInventory().getItem(event.getHotbarButton());
                    final int amount = event.getCurrentItem() == null ? 0 : event.getCurrentItem().getAmount();
                    final boolean result = menuSlot.take(amount) &&
                            menuSlot.offer(hotbar);
                    event.setCancelled(result);
                });
                break;
        }
    }

    private void distributeStack(@Nullable ItemStack stack) {
        if (stack == null) return;
        // all applicable slots that the item could be put into, sorted
        // by the amount to add
        final Collection<MenuSlot> applicableSlots = slots.values().stream()
                .filter(cell -> filterCells(cell, stack))
                .sorted(unsafeSortByAmount())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        int amount = stack.getAmount();
        // Step 1: Distribute amount over all applicableSlots that can take more
        Iterator<MenuSlot> itr = applicableSlots.iterator();
        while (itr.hasNext()) {
            final MenuSlot slot = itr.next();
            final ItemStack content = slot.currentStorageContent();
            if (content == null) break;
            else {
                int canBeAdded = content.getMaxStackSize() - content.getAmount();
                canBeAdded = amount - canBeAdded < 0 ? amount : amount - canBeAdded;

                ItemStack offering = stack.clone();
                offering.setAmount(canBeAdded);
                if (slot.offer(offering)) {
                    amount = amount - canBeAdded;
                    content.setAmount(content.getAmount() + canBeAdded);
                    slot.setStorageContent(content);
                    if (amount <= 0) break;
                }
                itr.remove();
            }
        }

        if (applicableSlots.isEmpty()) {
            stack.setAmount(amount);
            return;
        }

        // Step 2: Put rest into slot with highest priority for the item
        // All applicableSlots have no content
        SortedSet<MenuSlot> sortedByPriority = new TreeSet<>(Comparator.comparingInt(s -> s.priorityFor(stack)));
        sortedByPriority.addAll(applicableSlots);
        for (MenuSlot slot: sortedByPriority) {
            if (slot.offer(stack)) {
                slot.setStorageContent(stack);
                stack.setAmount(0);
                return;
            }
        }
    }

    // Filter out all cells where
    // 1) The cell is null or the value of the cell is null
    // 2) the stack is not similar to the one to compare
    // 3) If the stack is similar, the amount is smaller than the maximum stack-size
    private boolean filterCells(MenuSlot slot, ItemStack toCompare) {
        if (slot == null) return false;
        final ItemStack content = slot.currentStorageContent();
        if (content == null) return true;
        if (toCompare.isSimilar(content)) return content.getAmount() < content.getMaxStackSize();
        return false;
    }

    private Comparator<MenuSlot> unsafeSortByAmount() {
        return (s1, s2) -> {
            final ItemStack c1Stack = s1.currentStorageContent();
            final ItemStack c2Stack = s2.currentStorageContent();
            if (c1Stack == null) {
                // if c1Stack == null && c2Stack == null -> No special order,
                // else: c2Stack is similar to desired stack -> prefer c2
                return c2Stack == null ? 0 : 1;
            } else {
                // c1Stack != null, if c2Stack == null prefer c1Stack, else compare amounts
                return c2Stack == null ? 1 : Integer.compare(c1Stack.getAmount(), c2Stack.getAmount());
            }
        };
    }
}
