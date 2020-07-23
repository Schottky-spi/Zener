package com.github.schottky.zener.util.version;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReleaseTest {

    @Test
    public void v_1_7_is_older_than_any() {
        for (Release version : Release.values()) {
            if (version == Release.V_1_7 || version == Release.UNKNOWN) continue;
            assertTrue(version.isNewerThan(Release.V_1_7), "Version " + version + " is older than " + Release.V_1_7);
            assertTrue(Release.V_1_7.isOlderThan(version), "Version " + version + " is not newer than " + Release.V_1_7);
        }
    }

    @Test
    public void a_release_is_newer() {
        assertTrue(Release.V_1_13.isNewerThan(Release.V_1_8_8));
    }

}