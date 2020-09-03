package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.ArgumentResolver;
import com.github.schottky.zener.command.resolver.CommandException;
import com.github.schottky.zener.command.util.LanguageInterface;
import com.github.schottky.zener.command.util.Settable;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Stream;

public class ArgumentBuilder<T> {

    private final ArgumentResolver.ArgumentFactory factory;

    private ArgumentBuilder(ArgumentResolver.ArgumentFactory factory) {
        this.factory = factory;
    }

    private ArgumentBuilder(Class<?> forClazz) {
        this.factory = ArgumentResolver.get(forClazz).orElseThrow(RuntimeException::new);
    }

    public static <T> ArgumentBuilder<T> of(Class<?> clazz) {
        return new ArgumentBuilder<>(clazz);
    }

    public static <T> ArgumentBuilder<T> of(ArgumentResolver.ArgumentFactory factory) {
        return new ArgumentBuilder<>(factory);
    }

    public static <T> ArgumentBuilder<T> material() {
        return of(Material.class);
    }

    public static <T> ArgumentBuilder<T> integer() {
        return of(Integer.class);
    }

    public static <T> ArgumentBuilder<T> Double() {
        return of(Double.class);
    }

    public static <T> ArgumentBuilder<T> String() { return of(String.class); }

    @SuppressWarnings("unchecked")
    public Argument<?> create(CommandContext context) {
        Argument<T> arg = (Argument<T>) factory.create(context);
        if (description.isSet()) {
            arg = setDescription(arg, description.value());
        } else if (arg.description().isPresent()) {
            arg = setDescription(arg, arg.description().get());
        }
        if (permission != null) {
            if (arg.isLowLevel()) {
                arg = new LowLevelPermissionArgument<>(arg.asLowLevelArg(), permission);
            } else {
                arg = new AnyPermissionArgument<>(arg, permission);
            }
        }
        if (options != null && arg.isLowLevel()) {
            arg.asLowLevelArg().setOptions(options);
        }
        if (initialValue != null) {
            arg.setValue(initialValue);
        }
        return arg;
    }

    private Argument<T> setDescription(Argument<T> arg, String description) {
        if (arg.isLowLevel()) {
            arg.asLowLevelArg().setDescription(description, isOptional);
            return arg;
        } else {
            return new DescriptiveArgument<>(arg, description, isOptional);
        }
    }

    static abstract class ArgumentWrapper<T> extends AbstractArgument<T> {
        protected final Argument<T> argument;

        public ArgumentWrapper(@NotNull Argument<T> arg) {
            super(arg.context());
            this.argument = arg;
        }

        @Override
        public T value() {
            return argument.value();
        }

        @Override
        public boolean resolve(Queue<String> arguments) {
            return argument.resolve(arguments);
        }

        @Override
        public Stream<String> tabOptions(Queue<String> arguments) {
            return argument.tabOptions(arguments);
        }

        @Override
        public boolean isLowLevel() { return argument.isLowLevel(); }

        @Override
        public boolean isHighLevel() { return argument.isHighLevel(); }

        @Override
        public boolean isVarArgsArgument() { return argument.isVarArgsArgument(); }

        @Override
        public boolean isContextualOnly() { return argument.isContextualOnly(); }

        @Override
        public boolean isUnresolved() { return argument.isUnresolved(); }
    }

    static class DescriptiveArgument<T> extends ArgumentWrapper<T> {

        private final String description;
        private final boolean isOptional;

        DescriptiveArgument(@NotNull Argument<T> arg, @NotNull String description, boolean isOptional) {
            super(arg);
            this.description = description;
            this.isOptional = isOptional;
        }

        @Override
        public Optional<String> description() {
            final String translated = LanguageInterface.translateIdentifier(description);
            return Optional.of(isOptional ? "[" + translated + "]" : "<" + translated + ">");
        }
    }

    static class AnyPermissionArgument<T> extends ArgumentWrapper<T> {

        private final String permission;

        public AnyPermissionArgument(@NotNull Argument<T> arg, @NotNull String permission) {
            super(arg);
            this.permission = permission;
        }

        @Override
        public Optional<String> description() {
            return context.getSender().hasPermission(permission) ?
                    argument.description() :
                    Optional.empty();
        }

        @Override
        public boolean resolve(Queue<String> arguments) {
            if (context.getSender().hasPermission(permission)) {
                return super.resolve(arguments);
            } else {
                throw new CommandException("You do not have permission to execute this command");
            }
        }
    }

    static class LowLevelPermissionArgument<T> extends ArgumentWrapper<T> implements LowLevelArg<T> {

        private final String permission;

        public LowLevelPermissionArgument(@NotNull LowLevelArg<T> arg, @NotNull String permission) {
            super(arg);
            this.permission = permission;
        }

        private boolean isPermitted() {
            return context.getSender().hasPermission(permission);
        }

        @Override
        public void setDescription(String description, boolean isOptional) {
            argument.asLowLevelArg().setDescription(description, isOptional);
        }

        @Override
        public boolean resolve(Queue<String> arguments) {
            if (isPermitted()) {
                return argument.resolve(arguments);
            } else {
                return false;
            }
        }

        @Override
        public Stream<String> optionsAsString() {
            return isPermitted() ? argument.asLowLevelArg().optionsAsString() : Stream.empty();
        }

        @Override
        public void setOptions(Stream<String> options) {
            argument.asLowLevelArg().setOptions(options);
        }

        @Override
        public Optional<String> description() {
            return isPermitted() ? argument.description() : Optional.empty();
        }
    }

    private String permission;

    public ArgumentBuilder<T> requirePermission(String permission) {
        this.permission = permission;
        return this;
    }

    private final Settable<String> description = Settable.notSet();

    public ArgumentBuilder<T> description(String description) {
        this.description.set(description);
        return this;
    }

    public ArgumentBuilder<T> description(String description, boolean optional) {
        this.description.set(description);
        this.isOptional = optional;
        return this;
    }

    private boolean isOptional;

    public ArgumentBuilder<T> setOptional(boolean optional) {
        this.isOptional = optional;
        return this;
    }

    private Stream<String> options;

    public ArgumentBuilder<T> options(Stream<T> options, Function<T,String> mapper) {
        this.options = options.map(mapper);
        return this;
    }

    public ArgumentBuilder<T> options(Stream<T> options) {
        return this.options(options, Object::toString);
    }

    @SafeVarargs
    public final ArgumentBuilder<T> options(T... options) {
        return this.options(Arrays.stream(options));
    }

    public ArgumentBuilder<T> withRawOptions(Stream<String> options) {
        this.options = options;
        return this;
    }

    private T initialValue;

    public ArgumentBuilder<T> initialValue(T initialValue) {
        this.initialValue = initialValue;
        return this;
    }
}
