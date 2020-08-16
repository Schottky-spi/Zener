package com.github.schottky.zener.command.resolver;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentResolversTest {

    @ParameterizedTest
    @ValueSource(ints = {-42, -1, 0, 1, 42})
    public void an_integer_can_be_resolved(int actual) throws ArgumentNotResolvable {
        assertEquals(actual, ArgumentResolvers.INT_RESOLVER.resolve(String.valueOf(actual)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1.0", "a", "23.e", "54e"})
    public void an_illegal_integer_cannot_be_resolved(String value) {
        assertThrows(ArgumentNotResolvable.class, () -> ArgumentResolvers.INT_RESOLVER.resolve(value));
    }

    @Nested class The_boolean_resolver {

        @ParameterizedTest
        @ValueSource(strings = {"true", "True", "tRue", "TRUE"})
        public void will_return_true_when_the_name_is_true(String value) throws ArgumentNotResolvable {
            assertTrue(ArgumentResolvers.BOOLEAN_RESOLVER.resolve(value));
        }

        @ParameterizedTest
        @ValueSource(strings = {"false", "False", "FALSE", "fAlse"})
        public void will_return_false_when_the_name_is_false(String value) throws ArgumentNotResolvable {
            assertFalse(ArgumentResolvers.BOOLEAN_RESOLVER.resolve(value));
        }

        @ParameterizedTest
        @ValueSource(strings = {"Not true", "X", "0", "1", "bla", "foo", "bar"})
        public void will_fail_if_the_name_is_neither_true_nor_false(String value) {
            assertThrows(ArgumentNotResolvable.class, () -> ArgumentResolvers.BOOLEAN_RESOLVER.resolve(value));
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = {-42.0, 32e-3, 5.872, -1.0, 0.0, 1.0, 42.0})
    public void a_double_can_be_resolved(double actual) throws ArgumentNotResolvable {
        assertEquals(actual, ArgumentResolvers.DOUBLE_RESOLVER.resolve(String.valueOf(actual)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-e.g", "a", "23.e", "54e"})
    public void an_illegal_double_cannot_be_resolved(String value) {
        assertThrows(ArgumentNotResolvable.class, () -> ArgumentResolvers.INT_RESOLVER.resolve(value));
    }

}