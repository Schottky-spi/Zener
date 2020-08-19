package com.github.schottky.zener.command;

import com.github.schottky.command.TestCommand;
import com.github.schottky.command.mock.MockCommand;
import com.github.schottky.command.mock.MockPlayer;
import com.github.schottky.command.mock.MockServer;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MethodBasedSubCommandTest {

    private TestCommand base;

    @BeforeAll
    public static void setupAll() {
        Bukkit.setServer(new MockServer());
    }

    @BeforeEach
    public void setup() {
        base = new TestCommand();
    }

    @Nested class When_the_test_command_gets_created {

        @Test
        public void it_registers_all_sub_commands() {
            assertEquals(1, base.subCommands.size());
        }

        @Test
        public void the_sub_commands_are_method_based_sub_commands() {
            for (SubCommand<?> subCommand: base.subCommands) {
                assertEquals(MethodBasedSubCommand.class, subCommand.getClass());
            }
        }
    }

    private void dispatchCommand(String... args) {
        base.onCommand(new MockPlayer(), new MockCommand("test"), "test", args);
    }

    @Test
    public void a_sub_command_can_be_called_with_appropriate_arguments() {
        base.callback = objects -> {
            assertEquals(1, objects[0]);
            assertEquals(true, objects[1]);
        };
        dispatchCommand("subCmd", "1", "true");
    }

    @Test
    public void a_sub_command_fails_when_there_are_arguments_missing() {
        base.callback = args -> fail("Successfully executed command, but too few arguments were provided");
        dispatchCommand("subCmd", "1");
    }

    @Test
    public void a_sub_command_fails_when_there_are_too_many_arguments() {
        base.callback = args -> fail("Successfully executed command, but too many arguments were provided");
        dispatchCommand("subCmd", "1", "true", "salt");
    }

    @Test
    public void a_subcommand_does_not_accept_incorrect_arguments() {
        base.callback = args -> fail("Successfully executed command, but wrong arguments provided");
        assertDoesNotThrow(() -> dispatchCommand("subCmd", "1.0", "true"));
    }

}