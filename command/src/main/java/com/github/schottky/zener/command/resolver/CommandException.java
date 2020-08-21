package com.github.schottky.zener.command.resolver;

public class CommandException extends RuntimeException {

    public CommandException(String message) {
        super(message);
    }

    public CommandException() {
        super();
    }
}
