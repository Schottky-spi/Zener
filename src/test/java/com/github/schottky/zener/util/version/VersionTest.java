package com.github.schottky.zener.util.version;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VersionTest {


    @Test
    public void a_version_can_be_created_from_string() {
        assertEquals(Release.V_1_8_8, Version.fromString("1.8.8-SNAPSHOT"));
        assertEquals(Release.V_1_8, Version.fromString("1.8"));
        assertEquals(Release.V_1_15, Version.fromString("1.15"));
        assertEquals(Release.V_1_11_1, Version.fromString("1.11.1"));
        assertEquals(Release.UNKNOWN, Version.fromString("0.0.1"));
    }

}