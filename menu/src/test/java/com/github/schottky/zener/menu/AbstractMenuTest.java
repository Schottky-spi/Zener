package com.github.schottky.zener.menu;

import com.github.schottky.zener.menu.event.MenuClickEvent;
import com.github.schottky.zener.menu.item.MenuItem;
import com.github.schottky.zener.menu.item.SimpleMenuItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class AbstractMenuTest {

    private static AbstractMenu dut;

    @BeforeEach
    public void reset_dut() {
        dut = new MockMenu();
    }

    @Contract("_ -> new")
    private @NotNull MenuClickEvent mockTrigger(int slot) {
        return new MenuClickEvent(
                null,
                ClickType.LEFT,
                InventoryType.SlotType.OUTSIDE,
                slot,
                InventoryAction.PICKUP_ONE,
                null,
                dut);
    }

    @Nested class Menu_items {

        @ParameterizedTest
        @CsvSource({"-1, 0", "0, -1", "-5, -5", "8, 6", "9, 5", "100, 0", "0, 100"})
        public void cannot_be_added_outside_of_the_menu(int slotX, int slotY) {
            assertThrows(IllegalArgumentException.class, () -> dut.setItem(slotX, slotY, () -> new ItemStack(Material.ANVIL)));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 2, 5})
        public void can_be_added_as_row(int row) {
            final boolean[] triggered = new boolean[dut.columnCount()];
            dut.setRow(row, i -> new SimpleMenuItem(e -> triggered[i] = true, null));
            for (int x = 0; x < dut.columnCount(); x++) {
                dut.clickSlot(x, row, mockTrigger(-1));
            }
            for (int i = 0; i < triggered.length; i++) {
                assertTrue(triggered[i], "Not added at position " + i);
            }
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 4, 8})
        public void can_be_added_as_column(int column) {
            final boolean[] triggered = new boolean[dut.rowCount()];
            dut.setColumn(column, i -> new SimpleMenuItem(e -> triggered[i] = true, null));
            for (int y = 0; y < dut.rowCount(); y++) {
                dut.clickSlot(column, y, mockTrigger(-1));
            }
            for (int i = 0; i < triggered.length; i++) {
                assertTrue(triggered[i], "Not added at position " + i);
            }
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 10, 34, 53})
        public void can_be_clicked(int slot) {
            final boolean[] triggered = {false};
            MenuItem item = new SimpleMenuItem(e -> triggered[0] = true, null);
            dut.setItem(slot, item);
            final MenuClickEvent event = mockTrigger(slot);
            dut.clickSlot(slot, event);
            assertTrue(triggered[0], "Menu-Item at slot " + slot + " not clickable");
        }
    }

    @Nested class A_click_in_a_menu {

        @ParameterizedTest
        @ValueSource(ints = {-10, -20, 90, 54, -1})
        public void has_no_result_when_out_of_bounds(int slot) {
            final MenuClickEvent event = mockTrigger(slot);
            assertDoesNotThrow(() -> dut.clickSlot(slot, event));
        }

        @ParameterizedTest
        @CsvSource({"0, 0, 0", "2, 2, 0", "9, 0, 1", "12, 3, 1", "48, 3, 5"})
        public void converts_from_absolute_to_correct_xy(int absolute, int correctX, int correctY) {
            final boolean[] triggered = {false};
            MenuItem item = new SimpleMenuItem(e -> triggered[0] = true, null);
            dut.setItem(absolute, item);
            final MenuClickEvent event = mockTrigger(absolute);
            dut.clickSlot(correctX, correctY, event);
            assertTrue(triggered[0], "Incorrect field triggered");
        }

        @ParameterizedTest
        @CsvSource({"0, 0, 0", "2, 0, 2", "0, 1, 9", "3, 1, 12", "3, 5, 48"})
        public void converts_to_correct_absolute_from_xy(int slotX, int slotY, int correctAbsolute) {
            final boolean[] triggered = {false};
            MenuItem item = new SimpleMenuItem(e -> triggered[0] = true, null);
            dut.setItem(slotX, slotY, item);
            final MenuClickEvent event = mockTrigger(correctAbsolute);
            dut.clickSlot(correctAbsolute, event);
            assertTrue(triggered[0], "Incorrect field triggered");
        }
    }

}