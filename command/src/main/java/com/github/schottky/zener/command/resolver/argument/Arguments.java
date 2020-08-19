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

        public IntArgument(int initialValue) { super(initialValue); }

        public IntArgument() { super(); }

        @Override
        protected Integer fromArgument(String arg, CommandContext context) throws ArgumentNotResolvable {
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

        public DoubleArgument(double initialValue) { super(initialValue); }

        public DoubleArgument() { super(); }

        @Override
        protected Double fromArgument(String arg, CommandContext context) throws ArgumentNotResolvable {
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

        public StringArgument(String initialValue) { super(initialValue); }

        public StringArgument() { super(); }

        @Override
        protected String fromArgument(String arg, CommandContext context) {
            return arg;
        }
    }

    /**
     * An argument that contains a Boolean value
     */

    public static class BooleanArgument extends AbstractLowLevelArg<Boolean> {

        public BooleanArgument(boolean initialValue) { super(initialValue); }

        public BooleanArgument() { super(); }

        @Override
        protected Boolean fromArgument(String arg, CommandContext context) throws ArgumentNotResolvable {
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

        public MaterialArgument(Material initialValue) { super(initialValue); }

        public MaterialArgument() { super(); }

        @Override
        protected Material fromArgument(String arg, CommandContext context) throws ArgumentNotResolvable {
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
        public Stream<Material> options(CommandContext context) {
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

        public OfflinePlayerArg(OfflinePlayer initialValue) { super(initialValue); }

        public OfflinePlayerArg() { super(); }

        @Override
        protected OfflinePlayer fromArgument(String arg, CommandContext context) throws ArgumentNotResolvable {
            OfflinePlayer player = Bukkit.getOfflinePlayer(arg);
            if (!player.hasPlayedBefore())
                throw ArgumentNotResolvable.withMessage("Offline-player not found");
            else
                return player;
        }

        @Override
        public Stream<OfflinePlayer> options(CommandContext context) {
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

        public PlayerArg(Player initialValue) { super(initialValue); }

        public PlayerArg() { super(); }

        @Override
        protected Player fromArgument(String arg, CommandContext context) throws ArgumentNotResolvable {
            Player player = Bukkit.getPlayer(arg);
            if (player == null)
                throw ArgumentNotResolvable.withMessage("Player " + arg + " not found");
            else
                return player;
        }

        @Override
        public Stream<Player> options(CommandContext context) {
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

        public ItemStackArgument() {
            super(new MaterialArgument(),
                    new IntArgument(1).withOptions(Stream.of(1, 32, 64)));
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
