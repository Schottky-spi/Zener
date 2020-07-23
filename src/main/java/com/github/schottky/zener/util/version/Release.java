package com.github.schottky.zener.util.version;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;

public enum Release implements Version {

    V_1_7(7),
    V_1_7_2(7, 2),
    V_1_7_4(7, 4),
    V_1_7_6(7, 6),
    V_1_7_10(7, 10),

    V_1_8(8),
    V_1_8_1(8, 1),
    V_1_8_2(8, 2),
    V_1_8_8(8, 8),

    V_1_9(9),
    V_1_9_1(9, 1),
    V_1_9_3(9, 3),

    V_1_10(10),

    V_1_11(11),
    V_1_11_1(11, 1),

    V_1_12(12),
    V_1_12_1(12, 1),
    V_1_12_2(12, 2),

    V_1_13(13),
    V_1_13_1(13, 1),
    V_1_13_2(13, 2),

    V_1_14(14),
    V_1_14_1(14, 1),
    V_1_14_2(14, 2),
    V_1_14_3(14, 3),
    V_1_14_4(14, 4),

    V_1_15(15),
    V_1_15_1(15, 1),
    V_1_15_2( 15, 2),

    V_1_16_1(16, 1),

    UNKNOWN(-1, -1, -1);

    Release(int major, int minor, int revision) {
        this.major = major;
        this.minor = minor;
        this.revision = revision;
    }

    Release(int minor, int revision) {
        this(1, minor, revision);
    }

    Release(int minor) {
        this(1, minor, 0);
    }

    private final int major;
    public int major() { return major; }

    private final int minor;
    public int minor() { return minor; }

    private final int revision;
    public int revision() { return revision; }

    @Override
    public int compareToVersion(@NotNull Version version) {
        if (version.getClass() == Release.class) {
            Release release = (Release) version;
            return Comparator.comparingInt(Release::major)
                    .thenComparingInt(Release::minor)
                    .thenComparingInt(Release::revision)
                    .compare(this, release);
        } else {
            return 0;
        }
    }

    @Contract(value = "_, _ -> new", pure = true)
    public @NotNull Snapshot snapshot(int week, String identifier) {
        return new Snapshot(this, week, identifier);
    }

    @Override
    public Release asRelease() {
        return this;
    }


    @Override
    public @NotNull String toString() {
        if (this == UNKNOWN) return "Unknown";
        else return "v" + major + '.' + minor + '.' + revision;
    }

    public static Release fromComponents(int[] components) {
        int[] completeComponents = Arrays.copyOf(components, 3);
        for (Release release: Release.values()) {
            if (
                    release.major == completeComponents[0] &&
                    release.minor == completeComponents[1] &&
                    release.revision == completeComponents[2])
            {
                return release;
            }
        }
        return Release.UNKNOWN;
    }
}
