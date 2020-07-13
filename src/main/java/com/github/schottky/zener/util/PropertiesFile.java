package com.github.schottky.zener.util;

import com.github.schottky.zener.util.messaging.Console;
import com.google.common.base.Joiner;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class PropertiesFile {

    private final Map<String,String> properties = new LinkedHashMap<>();
    public static final char SEPARATOR_CHAR = ':';
    public static final char COMMENT_CHAR = '#';

    public String getProperty(String property) {
        return properties.get(property);
    }

    public void addProperty(String property, String value) {
        this.properties.put(property, value);
    }

    public void load(@NotNull Reader reader) {
        BufferedReader bufferedReader = new BufferedReader(reader);
        String currentLine;
        int currentLineInd = 1;
        try {
            while ((currentLine = bufferedReader.readLine()) != null) {
                // ignore comments
                if (currentLine.startsWith(String.valueOf(COMMENT_CHAR))) continue;
                int colonInd = currentLine.indexOf(SEPARATOR_CHAR);
                if (colonInd < 0) {
                    Console.warning(String.format("Invalid properties-file (at line %d), " +
                            "does not contain a separator ('%s')", currentLineInd, SEPARATOR_CHAR));
                } else {
                    addProperty(currentLine.substring(0, colonInd), currentLine.substring(colonInd + 1));
                }
                currentLineInd++;
            }
        } catch (IOException e) {
            Console.error(e);
        }
    }

    public void save(@NotNull Writer writer) {
        try {
            writer.write(toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String,String> rawMap() {
        return Collections.unmodifiableMap(properties);
    }

    @Override
    public String toString() {
        return Joiner.on('\n').withKeyValueSeparator(':').join(properties);
    }

    public static @NotNull PropertiesFile loadProperties(@NotNull Reader reader) {
        PropertiesFile propertiesFile = new PropertiesFile();
        propertiesFile.load(reader);
        return propertiesFile;
    }

    public static @NotNull PropertiesFile loadProperties(final File file) {
        final PropertiesFile properties = new PropertiesFile();
        try {
            final FileReader reader = new FileReader(file);
            properties.load(reader);
            reader.close();
        } catch (IOException e) {
            Console.error(e);
        }
        return properties;
    }
}
