package com.github.schottky.zener.command.util;

import com.github.schottky.zener.command.CommandBase;
import com.github.schottky.zener.localization.Language;
import com.google.common.annotations.VisibleForTesting;

import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;

public class LanguageInterface {

    public static String generateTranslationKey(Iterable<String> content) {
        final StringJoiner joiner = new StringJoiner(".");
        for (String string: content) {
            joiner.add(toSnakeCase(string));
        }
        return joiner.toString();
    }

    @VisibleForTesting
    public static String toSnakeCase(String input) {
        final StringBuilder builder = new StringBuilder();
        final char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            final char c = chars[i];
            if (i == 0 && Character.isUpperCase(c)) {
                builder.append(Character.toLowerCase(c));
            } else if (Character.isUpperCase(c)) {
                builder.append("_").append(Character.toLowerCase(c));
            } else {
                builder.append(Character.toLowerCase(c));
            }
        }
        return builder.toString();
    }

    public static <T> String generateTranslationKey(Iterable<T> content, Function<T,String> toStringFunction) {
        final StringJoiner joiner = new StringJoiner(".");
        for (T t: content) {
            joiner.add(toSnakeCase(toStringFunction.apply(t)));
        }
        return joiner.toString();
    }

    public static String translate(List<CommandBase> path, String useCase) {
        return Language.current().translate(generateTranslationKeyFor(useCase, path));
    }

    public static String generateTranslationKey(List<CommandBase> path) {
        return generateTranslationKey(path, CommandBase::name);
    }

    public static String generateTranslationKeyFor(String useCase, List<CommandBase> path) {
        return "command." + generateTranslationKey(path) + "." + useCase;
    }
    
    public static String translateIdentifier(String identifier) {
        if (!Language.isValidIdentifier(identifier))
            return identifier;
        else
            return Language.current().translate("command.ident." + identifier);
    }
}
