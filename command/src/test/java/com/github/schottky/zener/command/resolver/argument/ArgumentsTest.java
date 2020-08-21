package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.command.mock.MockPlayer;
import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.ArgumentNotResolvable;
import com.github.schottky.zener.command.resolver.CommandException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArgumentsTest {

    final CommandContext context = new CommandContext(new MockPlayer(), null, "label", new String[]{});

    @Nested class Int_Arguments {

        private final Arguments.IntArgument intArgument = new Arguments.IntArgument(context);

        @ParameterizedTest
        @ValueSource(ints = {-100, -50, 0, 49, 100})
        public void will_be_converted_to_its_value(int actual) throws CommandException {
            intArgument.resolve(String.valueOf(actual));
            assertEquals(actual, intArgument.value());
        }

        @ParameterizedTest
        @ValueSource(strings = {"1.0", "ab", "2e3"})
        public void throws_when_given_an_incorrect_input(String input) {
            assertThrows(ArgumentNotResolvable.class, () -> intArgument.resolve(input));
        }
    }

    @Nested class Double_Arguments {

        private final Arguments.DoubleArgument doubleArgument = new Arguments.DoubleArgument(context);

        @ParameterizedTest
        @ValueSource(doubles = {-100, -10.0, -23.456, 0.0, 3.14, 100})
        public void will_be_converted_to_its_value(double actual) {
            doubleArgument.resolve(String.valueOf(actual));
            assertEquals(actual, doubleArgument.value());
        }

        @ParameterizedTest
        @ValueSource(strings = {"seven.0", "a", "3.1a", "b"})
        public void throws_when_given_an_incorrect_input(String input) {
            assertThrows(ArgumentNotResolvable.class, () -> doubleArgument.resolve(input));
        }
    }

    @Nested class String_Arguments {

        private final Arguments.StringArgument stringArgument = new Arguments.StringArgument(context);

        @ParameterizedTest
        @ValueSource(strings = {"Hello", "World", "42", ""})
        public void will_be_converted_to_its_value(String actual) {
            stringArgument.resolve(actual);
            assertEquals(actual, stringArgument.value());
        }
    }

    @Nested class Boolean_Arguments {

        private final Arguments.BooleanArgument booleanArgument = new Arguments.BooleanArgument(context);

        @ParameterizedTest
        @CsvSource(value = {"true, true", "True, true", "TRUE, true", "false, false", "False, false", "FALSE, false"})
        public void will_be_converted_to_its_value(String value, boolean actual) {
            booleanArgument.resolve(value);
            assertEquals(actual, booleanArgument.value());
        }

        @ParameterizedTest
        @ValueSource(strings = {"flase", "rtue", "0", "1", "xyz"})
        public void throws_when_given_an_incorrect_input(String input) {
            assertThrows(ArgumentNotResolvable.class, () -> booleanArgument.resolve(input));
        }
    }

}