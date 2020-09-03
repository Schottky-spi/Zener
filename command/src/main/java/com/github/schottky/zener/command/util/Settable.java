package com.github.schottky.zener.command.util;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * represents an object that can have two states: Set and Not Set
 * This is useful to make distinctions between the states 'Not Set',
 * 'Set with null' and 'Set with any non-null value'
 * @param <T> The type of this settable
 */

public class Settable<T> {

    private T value;
    private boolean isSet;

    private Settable() {
        value = null;
        isSet = false;
    }

    private Settable(T value) {
        this.value = value;
        isSet = true;
    }

    /**
     * returns a new settable that is initially not set
     * @param <T> The type of the settable
     * @return The new settable
     */

    public static <T> Settable<T> notSet() {
        return new Settable<>();
    }

    /**
     * returns a new settable that is set to the initial value
     * (which can be null. However, null does not indicate that
     * this settable is not set).
     * @param value The value to set initially
     * @param <T> The type of the new settable
     * @return The new set settable
     */

    public static <T> Settable<T> of(@Nullable T value) {
        return new Settable<>(value);
    }

    /**
     * returns a new settable of a nullable value.
     * Unlike {@link #of(Object)}, a null value indicates that
     * this settable is not set, if the value is null
     * @param value The value to set initially
     * @param <T> The type of the settable
     * @return A new settable that is set, if the value is not null
     * and not set if it is null
     */

    public static <T> Settable<T> ofNullable(@Nullable T value) {
        if (value == null)
            return notSet();
        else
            return Settable.of(value);
    }

    /**
     * Sets this value to a new, potentially null value.
     * After a call to this method, the state of this settable will
     * always be Set, no matter the input
     * @param value The new value to set
     */

    public void set(@Nullable T value) {
        this.value = value;
        this.isSet = true;
    }

    /**
     * sets this value, if it is not null.
     * If it is null, the state will be the same as before.
     * @param value The new value to set
     */

    public void setIfNotNull(T value) {
        if (value != null)
            set(value);
    }

    /**
     * checks if this settable is set, meaning has been modified.
     * This does not guarantee a non-null value
     * @return true, if this has been set, false otherwise
     */

    public boolean isSet() {
        return isSet;
    }

    /**
     * returns the value of this settable. The value is {@code null},
     * if the settable has not been set, or if the settable has been set
     * to {@code null}
     * @return the associated value
     */

    public @Nullable T value() {
        return value;
    }

    /**
     * returns a new optional containing the value, or an empty optional
     * if this value is not set
     * @return The new optional
     */

    public Optional<T> toOptional() {
        return Optional.ofNullable(value);
    }

}
