package com.github.schottky.zener.command.resolver;

import com.github.schottky.zener.command.resolver.argument.Argument;
import com.github.schottky.zener.command.resolver.argument.Arguments;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ArgumentResolverTest {

    @Test
    public void the_argument_resolver_resolves_varargs_arguments() throws CommandException {
        final Argument<?> argument = new Arguments.ItemStackArgument();
        ArgumentResolver resolver = new ArgumentResolver(new String[]{"ROTTEN_FLESH", "2"});
        resolver.resolve(argument, null);
        final ItemStack stack = argument.as(ItemStack.class);
        assertSame(stack.getType(), Material.ROTTEN_FLESH);
        assertEquals(stack.getAmount(), 2);
    }

}