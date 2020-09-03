package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.command.mock.MockPlayer;
import com.github.schottky.zener.command.CommandContext;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class HighLevelArgTest {

    final CommandContext context = new CommandContext(new MockPlayer(), null, "label", new String[]{});

    @Nested class High_level_args_provide_the_correct_option {

        @Test
        public void test() {
            Queue<String> args = new LinkedList<>(Collections.singletonList("BAMBOO"));
            final Argument<?> arg = new Arguments.ItemStackArgument(context);
            final Stream<String> options = arg.tabOptions(args);
            System.out.println(options.limit(10).collect(Collectors.toList()));
        }
    }

}