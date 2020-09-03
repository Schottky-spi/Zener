package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;
import com.github.schottky.zener.command.resolver.CommandException;
import com.github.schottky.zener.command.util.LanguageInterface;

import java.util.Optional;
import java.util.Queue;
import java.util.stream.Stream;

public abstract class AbstractLowLevelArg<T> extends AbstractArgument<T> implements LowLevelArg<T> {

    public AbstractLowLevelArg(CommandContext context) {
        super(context);
    }

    @Override
    public void setOptions(Stream<String> options) {
        this.options = options;
    }

    private Stream<String> options = Stream.empty();

    @Override
    public Stream<String> optionsAsString() {
        return options;
    }

    @Override
    public boolean resolve(Queue<String> arguments) {
        if (arguments.isEmpty()) return false;
        this.value = fromArgument(arguments.poll());
        return true;
    }

    protected abstract T fromArgument(String arg);

    @Override
    public void setDescription(String description, boolean isOptional) {
        this.description = description;
        this.optionalArg = isOptional;
    }

    @Override
    public Stream<String> tabOptions(Queue<String> arguments) {
        if (arguments.isEmpty()) {
            return null;
        } else {
            final String value = arguments.poll();
            if (arguments.isEmpty()) {
                return optionsAsString();
            } else {
                try {
                    fromArgument(value);
                    return optionsAsString();
                } catch (CommandException e) {
                    return null;
                }
            }
        }
    }

    protected String description;

    private boolean optionalArg = false;

    @Override
    public Optional<String> description() {
        if (description == null) return Optional.empty();
        final String translated = LanguageInterface.translateIdentifier(description);
        return Optional.of(optionalArg ? "[" + translated + "]" : "<" + translated + ">");
    }
}
