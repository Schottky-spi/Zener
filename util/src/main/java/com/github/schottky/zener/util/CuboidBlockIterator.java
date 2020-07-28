package com.github.schottky.zener.util;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Utility-class to iterate over a certain cuboid area
 */

public class CuboidBlockIterator implements Iterator<Block> {

    private final Location start;
    private final Location end;
    private int currentX;
    private int currentY;
    private int currentZ;
    private final World world;

    /**
     * returns a new block-iterator that iterates over all blocks in a certain region
     * @param start The start of the region
     * @param end The end of the region
     * @return The iterator
     */
    @Contract("_, _ -> new")
    public static @NotNull CuboidBlockIterator of(Location start, @NotNull Location end) {
        return new CuboidBlockIterator(start, end.add(1, 1, 1));
    }

    /**
     * returns a new block-iterator that iterates over all blocks in a certain region
     * @param start The start of the region
     * @param end The end of the region
     * @param world the world that these vectors are valid in
     * @return The iterator
     */
    @Contract("_, _, _ -> new")
    public static @NotNull CuboidBlockIterator of(@NotNull Vector start, @NotNull Vector end, @NotNull World world) {
        return new CuboidBlockIterator(start.toLocation(world), end.toLocation(world));
    }

    /**
     * returns a new block-iterator that iterates over all blocks in a certain region
     * @param bb The bounding-box of the region
     * @param world the world that these vectors are valid in
     * @return The iterator
     */
    @Contract("_, _ -> new")
    public static @NotNull CuboidBlockIterator of(@NotNull BoundingBox bb, @NotNull World world) {
        return new CuboidBlockIterator(bb.getMin().toLocation(world), bb.getMax().toLocation(world));
    }

    private CuboidBlockIterator(@NotNull Location start, @NotNull Location end) {
        Preconditions.checkArgument(start.getWorld() != null, "World cannot be null");
        Preconditions.checkArgument(start.getWorld().equals(end.getWorld()), "Different worlds not possible");
        this.world = start.getWorld();
        this.start = start;
        this.end = end;
        this.currentX = start.getBlockX();
        this.currentY = start.getBlockY();
        this.currentZ = start.getBlockZ();
    }

    @Override
    public boolean hasNext() {
        return end.getBlockX() == currentX &&
                end.getBlockY() == currentY &&
                end.getBlockZ() == currentZ;
    }

    @Override
    public Block next() {
        final Block block = world.getBlockAt(currentX, currentY, currentZ);
        if (currentX == end.getBlockX()) {
            if (currentY == end.getBlockY()) {
                if (currentZ > end.getBlockZ()) {
                    throw new NoSuchElementException();
                } else {
                    this.currentX = start.getBlockX();
                    this.currentY = start.getBlockY();
                    currentZ++;
                }
            } else {
                this.currentX = start.getBlockX();
                currentY++;
            }
        } else {
            currentX++;
        }
        return block;
    }

    /**
     * removes this block; setting it's type to air
     */

    @Override
    public void remove() {
        world.getBlockAt(currentX, currentY, currentZ).setType(Material.AIR);
    }
}
