package com.github.schottky.zener.util.version;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public interface Version {

    int compareToVersion(Version other);

    Release asRelease();

    default boolean isNewerThan(Version other) { return this.compareToVersion(other) > 0; }

    default boolean isOlderThan(Version other) { return this.compareToVersion(other) < 0; }

    static Version fromString(@NotNull String versionId) {
        String simpleVersion = versionId.split("-", 2)[0];
        String[] components = simpleVersion.split("\\.");
        int[] ints = new int[components.length];
        for (int i = 0; i < ints.length; i++) ints[i] = Integer.parseInt(components[i]);
        return Release.fromComponents(ints);
    }

    static Version server() { return fromString(Bukkit.getBukkitVersion()); }
}
