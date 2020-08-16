package com.github.schottky.zener.menu;

import com.github.schottky.zener.menu.item.SimpleMenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractModifiableMenuTest {

    ModifiableMenu dut;

    @BeforeEach
    public void setup() {
        this.dut = new MockModifiableMenu();
    }

    static class MockModifiableMenu extends AbstractModifiableMenu {

        public MockModifiableMenu() {
            super(Menu.MAX_ROWS, "Modifiable menu");
        }
    }

    @Test
    public void an_item_can_be_added_after_creation() {
        boolean[] clicked = new boolean[1];
        this.dut.put(0, 0, new SimpleMenuItem(() -> clicked[0] = true, null));
        assertFalse(clicked[0]);
        dut.clickSlot(0, MockTrigger.mockEvent(0, dut));
        assertTrue(clicked[0]);
    }

    @Test
    public void an_item_can_be_removed_after_creation() {
        boolean[] clicked = new boolean[1];
        this.dut.put(0, 0, new SimpleMenuItem(() -> clicked[0] = true, null));
        dut.clickSlot(0, MockTrigger.mockEvent(0, dut));
        assertTrue(clicked[0]);
        dut.remove(0, 0);
        dut.clickSlot(0, MockTrigger.mockEvent(0, dut));
        assertTrue(clicked[0]);
    }

}