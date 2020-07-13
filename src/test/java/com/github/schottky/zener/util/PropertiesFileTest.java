package com.github.schottky.zener.util;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PropertiesFileTest {

    @Nested class Properties_can_be_loaded {

        @Test
        public void from_a_reader() {
            Reader reader = new StringReader("property1:My awesome property\nproperty2:My second property");
            PropertiesFile properties = PropertiesFile.loadProperties(reader);
            Map<String,String> expected = new LinkedHashMap<>();
            expected.put("property1", "My awesome property");
            expected.put("property2", "My second property");
            assertEquals(expected, properties.rawMap());
        }
    }
}