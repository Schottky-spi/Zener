package com.github.schottky.zener.command;

import com.github.schottky.command.TestCommand;
import com.github.schottky.command.mock.MockServer;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class SubCommandTest {

    @Cmd(value = "subCommand1", permission = "x")
    public static class SubCommand1 extends SubCommand<TestCommand> {

        public SubCommand1(TestCommand parentCommand) {
            super(parentCommand);
        }
    }

    @Cmd(value = "subCommand2", permission = "x")
    static class SubCommand2 extends SubCommand<SubCommand1> {

        public SubCommand2(SubCommand1 parentCommand) {
            super(parentCommand);
        }
    }

    private TestCommand rootCommand = new TestCommand();
    public SubCommand1 subCommand1 = new SubCommand1(rootCommand);
    public SubCommand2 subCommand2 = new SubCommand2(subCommand1);

    @BeforeEach
    public void setup() {
        rootCommand = new TestCommand();
        subCommand1 = new SubCommand1(rootCommand);
        subCommand2 = new SubCommand2(subCommand1);
        rootCommand.registerSubCommands(subCommand1);
        subCommand1.registerSubCommands(subCommand2);
    }

    @BeforeAll
    public static void setupAll() {
        Bukkit.setServer(new MockServer());
    }

    @Test
    public void a_subcommand_returns_its_root() {
        assertEquals(rootCommand, rootCommand.root());
        assertEquals(rootCommand, subCommand1.root());
        assertEquals(rootCommand, subCommand2.root());
    }

    @Test
    public void a_subcommand_returns_its_depth() {
        assertEquals(0, rootCommand.computeDepth());
        assertEquals(1, subCommand1.computeDepth());
        assertEquals(2, subCommand2.computeDepth());
    }

    @Test
    public void a_subcommand_returns_its_path() {
        assertIterableEquals(Collections.singletonList(rootCommand), rootCommand.path());
        assertIterableEquals(Arrays.asList(rootCommand, subCommand1), subCommand1.path());
        assertIterableEquals(Arrays.asList(rootCommand, subCommand1, subCommand2), subCommand2.path());
    }

}