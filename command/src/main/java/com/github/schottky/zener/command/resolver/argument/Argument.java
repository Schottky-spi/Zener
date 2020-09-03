package com.github.schottky.zener.command.resolver.argument;

import com.github.schottky.zener.command.CommandContext;

import java.util.Optional;
import java.util.Queue;
import java.util.stream.Stream;

/**
 * represents an Argument which itself represents a type
 * @param <T> The type that this argument represents
 */
public interface Argument<T> {

    /**
     * the description for this argument.
     * If this is a low-level argument, this will me something like
     * "&lt;argName&gt;"; if it is a high-level arg, this will be
     * a composition of the underlying low-level arguments.
     * This will be an empty optional, if no description has been set,
     * or the argument does not have a description (for example because
     * it is a contextual argument)
     * @return The description of this argument
     */

    Optional<String> description();

    /**
     * forcefully sets the value that this argument contains.
     * This should be used for an optional argument that is initially set,
     * for example using a variable-args argument
     * @param initial The initial argument to set
     */

    void setValue(T initial);

    /**
     * returns the value associated to this argument
     * @return The value
     */

    T value();

    /**
     * resolves the given string-arguments. This has different meanings for
     * different implementations of this interface. For example, a {@link LowLevelArg}
     * will poll the last element and try to resolve that. A {@link HighLevelArg} will
     * call the resolve-method of all sub-arguments and check if the queue is empty.
     * Contextual or unresolved arguments won't change the queue at all.
     * @param arguments The arguments to resolve
     * @return True, if the argument could be resolved, false otherwise
     */

    boolean resolve(Queue<String> arguments);

    boolean isLowLevel();

    default LowLevelArg<T> asLowLevelArg() {
        throw new ClassCastException(this.getClass() + " is not a low-level argument");
    }

    boolean isHighLevel();

    default HighLevelArg<T> asHighLevelArg() {
        throw new ClassCastException(this.getClass() + " is not a high-level argument");
    }

    boolean isContextualOnly();

    default ContextualArgument<T> asContextualArgument() {
        throw new ClassCastException(this.getClass() + " is not a contextual argument");
    }

    boolean isUnresolved();

    default UnresolvedArgument<T> asUnresolvedArgument() {
        throw new ClassCastException(this.getClass() + " is not an unresolved argument");
    }

    boolean isVarArgsArgument();

    default VarArgsArgument<T> asVarArgsArgument() {
        throw new ClassCastException(this.getClass() + " is not a variable-args argument");
    }

    Stream<String> tabOptions(Queue<String> arguments);

    CommandContext context();

    default int asInt() {
        return as(Integer.class);
    }

    default double asDouble() {
        return as(Double.class);
    }

    default boolean asBoolean() {
        return as(Boolean.class);
    }

    default <V> V as(Class<V> vClass) {
        return vClass.cast(value());
    }
}
