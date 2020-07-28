package com.github.schottky.zener.util.version;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class Snapshot implements Version {

    private final Release forRelease;

    private final int week;
    public int week() { return week; }
    private final int year;
    public int year() { return year; }

    private final String identifier;
    public String identifier() { return identifier; }

    public Snapshot(Release forRelease, int year, int week, String identifier) {
        this.forRelease = forRelease;
        this.year = year;
        this.week = week;
        this.identifier = identifier;
    }

    @Override
    public int compareToVersion(@NotNull Version o) {
        if (o.getClass() == Release.class) {
            return asRelease().compareToVersion(o);
        } else if (o.getClass() == Snapshot.class) {
            Snapshot snapshot = (Snapshot) o;
            return Comparator.comparing(Snapshot::asRelease)
                    .thenComparing(Snapshot::week)
                    .thenComparing(Snapshot::identifier)
                    .compare(this, snapshot);
        }
        return -1;
    }

    @Override
    public String toString() {
        return year + 'w' + week + identifier;
    }

    @Override
    public Release asRelease() {
        return forRelease;
    }
}
