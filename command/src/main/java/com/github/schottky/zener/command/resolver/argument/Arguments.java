package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.ArgumentNotResolvable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * A variety of possible pre-built arguments
 */
public class Arguments {

    /**
     * An argument that contains an integer-value
     */

    public static class IntArgument extends AbstractLowLevelArg<Integer> {

        public IntArgument(CommandContext context, int initialValue) {
            super(context, initialValue);
        }

        public IntArgument(CommandContext context) {
            super(context);
        }

        @Override
        protected Integer fromArgument(String arg) throws ArgumentNotResolvable {
            try {
                return Integer.parseInt(arg);
            } catch (NumberFormatException e) {
                throw ArgumentNotResolvable.withMessage(arg + " is not a number!");
            }
        }
    }

    /**
     * An argument that contains a double-value
     */

    public static class DoubleArgument extends AbstractLowLevelArg<Double> {

        public DoubleArgument(CommandContext context, double initialValue) {
            super(context, initialValue);
        }

        public DoubleArgument(CommandContext context) {
            super(context);
        }

        @Override
        protected Double fromArgument(String arg) throws ArgumentNotResolvable {
            try {
                return Double.parseDouble(arg);
            } catch (NumberFormatException e) {
                throw ArgumentNotResolvable.fromException(e);
            }
        }

    }

    /**
     * An argument that contains a string-value
     */

    public static class StringArgument extends AbstractLowLevelArg<String> {

        public StringArgument(CommandContext context, String initialValue) {
            super(context, initialValue);
        }

        public StringArgument(CommandContext context) {
            super(context);
        }

        @Override
        protected String fromArgument(String arg) {
            return arg;
        }
    }

    /**
     * An argument that contains a Boolean value
     */

    public static class BooleanArgument extends AbstractLowLevelArg<Boolean> {

        public BooleanArgument(CommandContext context, boolean initialValue) {
            super(context, initialValue);
        }

        public BooleanArgument(CommandContext context) {
            super(context);
        }

        @Override
        public Boolean fromArgument(String arg) throws ArgumentNotResolvable {
            if (arg.equalsIgnoreCase("true"))
                return true;
            else if (arg.equalsIgnoreCase("false"))
                return false;
            else throw ArgumentNotResolvable.withMessage("argument is not a boolean value");
        }
    }

    /**
     * An Argument that contains a Material-value.
     * Can be resolved by the name (ROTTEN_FLESH)
     * or the namespaced-key (minecraft:rotten_flesh)
     */

    public static class MaterialArgument extends AbstractLowLevelArg<Material> {

        public MaterialArgument(CommandContext context, Material initialValue) {
            super(context, initialValue);
            this.description = "type";
        }

        public MaterialArgument(CommandContext context) {
            super(context);
            this.description = "type";
        }

        @Override
        protected Material fromArgument(String arg) throws ArgumentNotResolvable {
            Material material = Material.matchMaterial(arg);
            if (material == null) {
                throw ArgumentNotResolvable.withMessage("No material with name " + arg + " found");
            } else {
                return material;
            }
        }

        // cache for efficiency
        private static final Material[] materials = Material.values();

        @Override
        public Stream<Material> options() {
            return Arrays.stream(materials);
        }

        @Override
        public String toString(Material value) {
            return value.name().toLowerCase(Locale.US);
        }
    }

    /**
     * An argument that contains an offline player.
     * May include a blocking request to the Mojang-API
     */

    public static class OfflinePlayerArg extends AbstractLowLevelArg<OfflinePlayer> {

        public OfflinePlayerArg(CommandContext context, OfflinePlayer initialValue) {
            super(context, initialValue);
            this.description = "player";
        }

        public OfflinePlayerArg(CommandContext context) {
            super(context);
            this.description = "player";
        }

        @Override
        protected OfflinePlayer fromArgument(String arg) throws ArgumentNotResolvable {
            OfflinePlayer player = Bukkit.getOfflinePlayer(arg);
            if (!player.hasPlayedBefore())
                throw ArgumentNotResolvable.withMessage("Offline-player not found");
            else
                return player;
        }

        @Override
        public Stream<OfflinePlayer> options() {
            return Bukkit.getOnlinePlayers().stream().map(p -> (OfflinePlayer) p);
        }

        @Override
        public String toString(OfflinePlayer player) {
            return player.getName();
        }
    }

    /**
     * AN argument that contains an online player
     */

    public static class PlayerArg extends AbstractLowLevelArg<Player> {

        public PlayerArg(CommandContext context, Player initialValue) {
            super(context, initialValue);
            this.description = "player";
        }

        public PlayerArg(CommandContext context) {
            super(context);
            this.description = "player";
        }

        @Override
        protected Player fromArgument(String arg) throws ArgumentNotResolvable {
            Player player = Bukkit.getPlayer(arg);
            if (player == null)
                throw ArgumentNotResolvable.withMessage("Player " + arg + " not found");
            else
                return player;
        }

        @Override
        public Stream<Player> options() {
            return Bukkit.getOnlinePlayers().stream().map(p -> (Player) p);
        }

        @Override
        public String toString(Player player) {
            return player.getName();
        }
    }

    /**
     * An argument that contains an item-stack.
     * This is a variable-args argument with the amount being
     * the variable part. If this behavior is not desired,
     * use {@link MaterialArgument} to build the stack
     */

    public static class ItemStackArgument extends AbstractHighLevelVarArg<ItemStack> {

        public ItemStackArgument(CommandContext context) {
            super(context,
                    new MaterialArgument(context)
                            .withDescription("type"),
                    new IntArgument(context, 1)
                            .withOptions(Stream.of(1, 32, 64))
                            .withDescription("amount", true));
        }

        @Override
        public int minArgs() {
            return 1;
        }

        @Override
        public int maxArgs() {
            return 2;
        }

        @Override
        public ItemStack value() {
            return new ItemStack(contents[0].as(Material.class), contents[1].asInt());
        }
    }
}
