package com.github.schottky.zener.command.resolver;

import com.github.schottky.command.mock.MockPlayer;
import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.argument.AbstractLowLevelArg;
import com.github.schottky.zener.command.resolver.argument.Arguments;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentResolverTest {

    static class CustomArgsResolver extends AbstractLowLevelArg<Byte> {

        public CustomArgsResolver(CommandContext context) {
            super(context);
        }

        @Override
        protected Byte fromArgument(String arg) {
            try {
                return Byte.valueOf(arg);
            } catch (NumberFormatException e) {
                throw ArgumentNotResolvable.withMessage("Byte not assignable to " + arg);
            }
        }
    }

    private final CommandContext mockContext =
            new CommandContext(new MockPlayer(), null, "label", new String[0]);

    @Test
    public void an_empty_parameter_array_can_be_resolved() {
        final Object[] resolved = new ArgumentResolver(new String[0], mockContext).resolve(new Parameter[0]);
        assertEquals(0, resolved.length);
    }

    @Test
    public void an_existing_argument_can_be_resolved() {
        final Parameter[] types = new Parameter[] { new Parameter(String.class) };
        final Object[] resolved = new ArgumentResolver(new String[] {"foo"}, mockContext).resolve(types);
        assertEquals(1, resolved.length);
        assertEquals("foo", resolved[0]);
    }

    @Test
    public void when_an_argument_gets_registered_it_can_be_resolved() {
        ArgumentResolver.registerArgument(Byte.class, CustomArgsResolver::new);
        final Parameter[] types = new Parameter[] { new Parameter(Byte.class) };
        final Object[] objects = new ArgumentResolver(new String[] {"1"}, mockContext).resolve(types);
        assertEquals(1, objects.length);
        assertEquals((byte) 1, objects[0]);
    }

    static class NewIntResolver extends AbstractLowLevelArg<Integer> {

        public NewIntResolver(CommandContext context) {
            super(context);
        }

        @Override
        protected Integer fromArgument(String arg) {
            try {
                return Integer.parseInt(arg) + 42;
            } catch (NumberFormatException e) {
                throw ArgumentNotResolvable.withMessage("Int not assignable to " + arg);
            }
        }
    }

    @Test
    public void when_an_existing_argument_gets_registered_it_overrides_the_old_one() {
        ArgumentResolver.registerArgument(Integer.class, NewIntResolver::new);
        final Parameter[] types = new Parameter[] { new Parameter(Integer.class) };
        final Object[] objects = new ArgumentResolver(new String[] {"1"}, mockContext).resolve(types);
        assertEquals(1, objects.length);
        assertEquals(1 + 42, objects[0]);
    }

    @Nested class The_argument_resolver_resolves_varargs_arguments {

        final Parameter[] parameters = Parameter.of(ItemStack.class);

        @Test
        public void with_min_args() {
            final Object[] objects =
                    new ArgumentResolver(new String[] {"ROTTEN_FLESH"}, mockContext).resolve(parameters);
            final ItemStack stack = (ItemStack) objects[0];
            assertSame(Material.ROTTEN_FLESH, stack.getType());
            assertEquals(1, stack.getAmount());
        }

        @Test
        public void with_max_args() {
            final Object[] objects =
                    new ArgumentResolver(new String[] {"ROTTEN_FLESH", "2"}, mockContext).resolve(parameters);
            ArgumentResolver.registerArgument(Integer.TYPE, Arguments.IntArgument::new);
            final ItemStack stack = (ItemStack) objects[0];
            assertSame(Material.ROTTEN_FLESH, stack.getType());
            assertEquals(2, stack.getAmount());
        }

        @Test
        public void throws_when_too_few_args_are_given() {
            assertThrows(ArgumentNotResolvable.class, () ->
                    new ArgumentResolver(new String[0], mockContext).resolve(parameters));
        }

        @Test
        public void throws_when_too_many_args_are_given() {
            assertThrows(ArgumentNotResolvable.class, () ->
                    new ArgumentResolver(new String[] {"ROTTEN_FLESH", "2", "foo"}, mockContext).resolve(parameters) );
        }

    }

    @Test
    public void an_argument_can_resolve_only_unresolved_args() throws NoSuchMethodException {
        final Parameter[] parameters = Parameter.fromMethod(
                getClass().getDeclaredMethod("resolveOnly", Player.class));
        final Object[] objects = new ArgumentResolver(new String[0], mockContext).resolve(parameters);
        assertEquals(1, objects.length);
        assertEquals(mockContext.getPlayer(), objects[0]);
    }

    public void resolveOnly(@Unresolved Player player) { }

}