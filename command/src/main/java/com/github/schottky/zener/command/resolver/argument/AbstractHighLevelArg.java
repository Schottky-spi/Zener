package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Queue;
import java.util.StringJoiner;
import java.util.stream.Stream;

/**
 * Abstract superclass for common high-level arguments
 * @param <T> The type of the high-level argument
 */
public abstract class AbstractHighLevelArg<T> extends AbstractArgument<T> implements HighLevelArg<T> {

    /**
     * The arguments that this high-level arg contains
     */
    protected final Argument<?>[] contents;

    /**
     * Constructs a new high-level argument with the given Arguments
     * @param context the context that this is being used in
     * @param contents The contents that this high-level argument contains
     */
    public AbstractHighLevelArg(CommandContext context, Argument<?>... contents) {
        super(context);
        this.contents = contents;
    }

    public AbstractHighLevelArg(CommandContext context, ArgumentBuilder<?>... contents) {
        super(context);
        this.contents = new Argument<?>[contents.length];
        for (int i = 0; i < contents.length; i++) {
            this.contents[i] = contents[i].create(context);
        }
    }

    @Override
    public boolean resolve(Queue<String> arguments) {
        for (Argument<?> arg: contents) {
            if (arguments.isEmpty() && !arg.isUnresolved() && !arg.isContextualOnly())
                return false;
            else
                arg.resolve(arguments);
        }
        return true;
    }

    @Override
    public Argument<?>[] contents() {
        return contents;
    }

    @Override
    public Stream<String> tabOptions(Queue<String> arguments) {
        if (arguments.isEmpty())
            return Stream.empty();
        for (Argument<?> arg: contents) {
            if (arg.isContextualOnly() || arg.isUnresolved())
                continue;

            Stream<String> stream = arg.tabOptions(arguments);
            if (arguments.isEmpty())
                return stream;
            if (stream == null)
                return Stream.empty();
        }
        return Stream.empty();
    }

    @Override
    public @Nullable Optional<String> description() {
        StringJoiner joiner = new StringJoiner(" ");
        for (Argument<?> argument: contents) {
            argument.description().ifPresent(joiner::add);
        }
        return joiner.length() > 0 ? Optional.of(joiner.toString()) : Optional.empty();
    }

    public abstract T value();
}
