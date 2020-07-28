package com.github.schottky.zener.util;

import com.github.schottky.zener.util.test.CSVToArray;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class PersistentDataTypesTest {

    static PersistentDataAdapterContext mockContext = new MockPersistentDataAdapterContext();

    @Nested
    class The_UUID_Data_Type {

        @RepeatedTest(1000)
        void stores_and_restores_a_random_uuid() {
            UUID uuid = UUID.randomUUID();
            byte[] primitiveValue = PersistentDataTypes.UUID.toPrimitive(uuid, mockContext);
            assertEquals(16, primitiveValue.length, "UUID not stored in 16 bytes");
            UUID restored = PersistentDataTypes.UUID.fromPrimitive(primitiveValue, mockContext);
            assertEquals(uuid, restored);
        }
    }

    @Nested
    class The_Boolean_Data_Type {

        @Test
        void has_0_as_primitive_when_false() {
            assertEquals((byte) 0, PersistentDataTypes.BOOLEAN.toPrimitive(false, mockContext));
        }

        @Test
        void has_1_as_primitive_when_true() {
            assertEquals((byte) 1, PersistentDataTypes.BOOLEAN.toPrimitive(true, mockContext));
        }

        @Test
        void is_false_when_primitive_is_0() {
            assertEquals(false, PersistentDataTypes.BOOLEAN.fromPrimitive((byte) 0, mockContext));
        }

        @ParameterizedTest
        @ValueSource(bytes = {1, 2, -1, -5, (byte) 128, Byte.MIN_VALUE, Byte.MAX_VALUE})
        void is_true_when_primitive_is_everything_but_0(byte testData) {
            assertEquals(true, PersistentDataTypes.BOOLEAN.fromPrimitive(testData, mockContext));
        }
    }

    @Nested class The_String_list_data_type {

        @ParameterizedTest
        @CsvSource({"Hello, World, foo, bar", "Hello"})
        void stores_and_restores_a_string_list(@ConvertWith(CSVToArray.class)String... input) {
            List<String> strings = Arrays.asList(input);
            byte[] primitive = PersistentDataTypes.STRING_LIST.toPrimitive(new PersistentDataTypes.StringList(strings), mockContext);
            PersistentDataTypes.StringList restored = PersistentDataTypes.STRING_LIST.fromPrimitive(primitive, mockContext);
            assertIterableEquals(strings, restored);
        }

        @Test
        void stores_an_empty_string() {
            List<String> strings = Collections.emptyList();
            byte[] primitive = PersistentDataTypes.STRING_LIST.toPrimitive(new PersistentDataTypes.StringList(strings), mockContext);
            PersistentDataTypes.StringList restored = PersistentDataTypes.STRING_LIST.fromPrimitive(primitive, mockContext);
            assertIterableEquals(strings, restored);
        }
    }

    static class MockPersistentDataAdapterContext implements PersistentDataAdapterContext {

        @Override
        public @NotNull PersistentDataContainer newPersistentDataContainer() {
            return new MockPersistentDataContainer();
        }
    }

    static class MockPersistentDataContainer implements PersistentDataContainer {

        public <T, Z> void set(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType, @NotNull Z z) { }
        public <T, Z> boolean has(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType) { return false; }
        public <T, Z> @Nullable Z get(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType) { return null; }
        public <T, Z> @NotNull Z getOrDefault(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType, @NotNull Z z) { return z; }
        public void remove(@NotNull NamespacedKey namespacedKey) { }
        public boolean isEmpty() { return true; }
        public @NotNull PersistentDataAdapterContext getAdapterContext() { return new MockPersistentDataAdapterContext(); }
    }

}