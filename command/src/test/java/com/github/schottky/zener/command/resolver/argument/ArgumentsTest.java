package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.resolver.ArgumentNotResolvable;
import com.github.schottky.zener.command.resolver.CommandException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArgumentsTest {

    @Nested class Int_Arguments {

        private final Arguments.IntArgument intArgument = new Arguments.IntArgument();

        @ParameterizedTest
        @ValueSource(ints = {-100, -50, 0, 49, 100})
        public void will_be_converted_to_its_value(int actual) throws CommandException {
            intArgument.resolve(String.valueOf(actual), null);
            assertEquals(actual, intArgument.value());
        }

        @ParameterizedTest
        @ValueSource(strings = {"1.0", "ab", "2e3"})
        public void throws_when_given_an_incorrect_input(String input) {
            assertThrows(ArgumentNotResolvable.class, () -> intArgument.resolve(input, null));
        }
    }

}