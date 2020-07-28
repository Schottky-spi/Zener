package com.github.schottky.zener.util.version;

import com.github.schottky.zener.util.test.StringToRelease;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SnapshotTest {

    @ParameterizedTest
    @ValueSource(strings = {"V_1_7", "V_1_14_4", "V_1_15"})
    public void a_snapshot_is_always_newer_than_its_release(@ConvertWith(StringToRelease.class) Release release) {
        Snapshot snapshot = new Snapshot(release, 0, 0,"a");
        assertTrue(snapshot.isNewerThan(release), "snapshot " + snapshot + "");
    }

}