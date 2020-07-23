package com.github.schottky.zener.util.version;

import org.jetbrains.annotations.NotNull;

public class Snapshot implements Version {

    private final Release forRelease;
    private final int week;
    private final String identifier;

    public Snapshot(Release forRelease, int week, String identifier) {
        this.forRelease = forRelease;
        this.week = week;
        this.identifier = identifier;
    }

    @Override
    public int compareToVersion(@NotNull Version o) {
        return 0;
    }

    @Override
    public Release asRelease() {
        return forRelease;
    }
}
