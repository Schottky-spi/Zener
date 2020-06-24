package de.schottky.zener.localization;

import com.google.common.base.Joiner;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeTest {

    @Test
    void traces_all_nodes() {
        Tree<String,String> tree = new Tree<>();
        tree.addLeafFor(new String[] {"some", "path"}, "value1");
        tree.addLeafFor(new String[] {"some", "other", "path"}, "value2");
        tree.addLeafFor(new String[] {"yet", "another", "path"}, "value3");
        Map<String,String> rawMap = new HashMap<>();
        rawMap.put("some.path", "value1");
        rawMap.put("some.other.path", "value2");
        rawMap.put("yet.another.path", "value3");
        assertEquals(tree.traverseAndFlatten(paths -> Joiner.on('.').join(paths), -1), rawMap);
    }

}