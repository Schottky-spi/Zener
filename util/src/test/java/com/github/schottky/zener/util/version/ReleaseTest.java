package com.github.schottky.zener.util.version;

import com.github.schottky.zener.util.test.StringToRelease;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReleaseTest {

    @Test
    public void v_1_7_is_older_than_all_versions() {
        for (Release version : Release.values()) {
            if (version == Release.V_1_7 || version == Release.UNKNOWN) continue;
            assertTrue(version.isNewerThan(Release.V_1_7), "Version " + version + " is older than " + Release.V_1_7);
            assertTrue(Release.V_1_7.isOlderThan(version), "Version " + version + " is not newer than " + Release.V_1_7);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"V_1_14_2", "V_1_15", "V_1_11", "V_1_16_1"})
    public void a_release_is_neither_older_nor_newer_than_itself(@ConvertWith(StringToRelease.class) @NotNull Release release) {
        assertFalse(release.isNewerThan(release));
        assertFalse(release.isOlderThan(release));
    }

    @ParameterizedTest
    @ValueSource(strings = {"V_1_7_2", "V_1_15", "V_1_11", "V_1_16_1"})
    public void a_release_is_always_older_than_its_snapshot(@ConvertWith(StringToRelease.class) @NotNull Release release) {
        assertTrue(release.isOlderThan(release.snapshot(0, 0, "a")));
    }

    @Test
    public void a_release_is_newer_than_an_older_snapshot() {
        assertTrue(Release.V_1_8_8.isNewerThan(Release.V_1_8_2.snapshot(100, 10, "a")));
    }

    @Test
    public void v_1_16_is_newer_than_all_versions() {
        for (Release version: Release.values()) {
            if (version == Release.V_1_16_1 || version == Release.UNKNOWN) continue;
            assertTrue(Release.V_1_16_1.isNewerThan(version), "Version " + version + " is newer than " + Release.V_1_16_1);
            assertTrue(version.isOlderThan(Release.V_1_16_1), "Version " + version + " is not newer than " + Release.V_1_16_1);
        }
    }

}