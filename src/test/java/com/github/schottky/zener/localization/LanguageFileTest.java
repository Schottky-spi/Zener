package com.github.schottky.zener.localization;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LanguageFileTest {

    @Test
    void generates_correct_from_io_file() throws Exception {
        assertEquals(new LanguageFile(new File("/"), Locale.US, LanguageFile.StorageProvider.JSON),
                LanguageFile.fromIOFile(new File("/en-us.lang")),
                "valid file");
        assertEquals(new LanguageFile(new File("/"), Locale.US, LanguageFile.StorageProvider.JSON),
                LanguageFile.fromIOFile(new File("/en-US.lang")),
                "valid file");
        assertEquals(new LanguageFile(new File("/"), Locale.US, LanguageFile.StorageProvider.JSON),
                LanguageFile.fromIOFile(new File("/en-us")),
                "valid file");
    }

    @Test
    void throws_if_incorrect_io_file() {
        assertThrows(InvalidLanguageDescription.class, () -> LanguageFile.fromIOFile(new File("en.us.lang")));
        assertThrows(InvalidLanguageDescription.class, () -> LanguageFile.fromIOFile(new File("en-us.json")));
    }

    @Test
    void generates_correct_io_file() {
        LanguageFile en_json = new LanguageFile(new File("/"), Locale.US, LanguageFile.StorageProvider.JSON);
        LanguageFile ger_yaml = new LanguageFile(new File("/"), Locale.GERMAN, LanguageFile.StorageProvider.YAML);

        assertEquals(new File("/en-US.lang"), en_json.toIOFile());
        assertEquals(new File("/de.yaml"), ger_yaml.toIOFile());
    }
}