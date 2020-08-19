package com.github.schottky.zener.command.resolver;

public class ArgumentNotResolvable extends CommandException {

    public static ArgumentNotResolvable withMessage(String message) {
        return new ArgumentNotResolvable(message);
    }

    public static ArgumentNotResolvable fromException(Throwable th) {
        return new ArgumentNotResolvable(th.getMessage());
    }

    public ArgumentNotResolvable(String message) {
        super(message);
    }
}
