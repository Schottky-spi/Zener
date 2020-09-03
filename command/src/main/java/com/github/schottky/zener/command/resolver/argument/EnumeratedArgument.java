package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.ArgumentNotResolvable;

import java.util.*;
import java.util.stream.Stream;

/**
 * A low-level arg that is meant to encompass a selected option of enum-constants.
 * This is not meant to support many options, usually no more than 4-6.
 * This is, for example, suitable for the 6 {@link org.bukkit.block.BlockFace}s UP, DOWN, NORTH, EAST,
 * SOUTH and WEST, but not for the {@link org.bukkit.Material}-enum
 * @param <T> The type of this argument
 */

public class EnumeratedArgument<T extends Enum<T>> extends AbstractLowLevelArg<T> {

    private final List<T> enumConstants;

    /**
     * creates a new Enumerated argument that contains all elements
     * of the specific class
     * @param context The context
     * @param enumClass The enum-class
     */

    public EnumeratedArgument(CommandContext context, Class<T> enumClass) {
        super(context);
        this.enumConstants = new ArrayList<>(EnumSet.allOf(enumClass));
    }

    /**
     * creates a new Enumerated argument that contains a subset of all elements
     * of a certain enum
     * @param context The context
     * @param enumConstants All cnostants to include
     */

    public EnumeratedArgument(CommandContext context, Set<T> enumConstants) {
        super(context);
        this.enumConstants = new ArrayList<>(enumConstants);
    }

    @Override
    public Stream<String> optionsAsString() {
        return enumConstants.stream().map(this::map);
    }

    protected String map(T t) {
        return t.name();
    }

    @Override
    protected T fromArgument(String arg) {
        for (T t : enumConstants)
            if (t.name().equalsIgnoreCase(arg))
                return t;

        throw ArgumentNotResolvable.withMessage("");
    }

    @Override
    public Optional<String> description() {
        if (enumConstants.size() == 0)
            return Optional.empty();
        final StringJoiner joiner = new StringJoiner("|", "<", ">");
        if (enumConstants.size() > 3) {
            joiner
                    .add(enumConstants.get(0).name())
                    .add(enumConstants.get(1).name())
                    .add("...")
                    .add(enumConstants.get(enumConstants.size() - 1).name());
        } else {
            for (T option: enumConstants)
                joiner.add(option.name());
        }
        return Optional.of(joiner.toString());
    }
}
