package com.github.schottky.zener.command;

import com.github.schottky.command.TestCommand;
import com.github.schottky.command.mock.MockServer;
import com.github.schottky.zener.command.util.LanguageInterface;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LanguageInterfaceTest {

    @BeforeAll
    public static void setup() {
        Bukkit.setServer(new MockServer());
    }

    @ParameterizedTest
    @CsvSource({"hello, hello", "helloWorld, hello_world", "HelloWorld, hello_world"})
    public void an_input_will_be_converted_to_lower_snake_case(String actual, String expected) {
        assertEquals(expected, LanguageInterface.toSnakeCase(actual));
    }

    @Cmd(value = "subCmd", permission = "x")
    static class TestSubCommand extends SubCommand<TestCommand> {

        public TestSubCommand(TestCommand parentCommand) {
            super(parentCommand);
        }

        @SubCmd(value = "subSubCmd", permission = "x")
        public void subCmd() {}
    }

    @Test
    public void the_generation_key_will_be_generated_according_to_the_command() {
        final TestCommand parent = new TestCommand();
        final TestSubCommand subCommand = new TestSubCommand(parent);
        parent.registerSubCommands(subCommand);
        SubCommand<?> subCommand1 = null;
        for (SubCommand<?> command: subCommand.subCommands) {
            if (command.name().equals("subSubCmd")) subCommand1 = command;
        }

        assertEquals("test", LanguageInterface.generateTranslationKey(parent.path()));
        assertEquals("test.sub_cmd", LanguageInterface.generateTranslationKey(subCommand.path()));
        assertEquals("test.sub_cmd.sub_sub_cmd", LanguageInterface.generateTranslationKey(subCommand1.path()));
    }

}