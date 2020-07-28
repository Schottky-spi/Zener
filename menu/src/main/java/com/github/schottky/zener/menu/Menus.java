package com.github.schottky.zener.menu;

import com.github.schottky.zener.menu.item.MenuItem;
import com.github.schottky.zener.menu.item.Spacer;
import com.github.schottky.zener.util.item.Items;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Menus {

    private Menus() {}

    @Contract("_, _, _ -> new")
    public static @NotNull Menu newConfirmationDialog(
            @NotNull String title,
            @NotNull MenuItem onAccept,
            @NotNull MenuItem onDeny) {
        return new ConfirmationDialog(title, onAccept, onDeny);
    }

    private static class ConfirmationDialog extends AbstractMenu {
        public ConfirmationDialog(
                @NotNull String title,
                @NotNull MenuItem onAccept,
                @NotNull MenuItem onDeny)
        {
            super(1, title);
            this.setRow(0, i -> new Spacer(DyeColor.LIGHT_GRAY));
            this.setItem(3, 0, onAccept);
            this.setItem(5, 0, onDeny);
            this.setItem(4, 0, () -> Items.withoutTitle(Material.NETHER_STAR));
        }
    }
}
