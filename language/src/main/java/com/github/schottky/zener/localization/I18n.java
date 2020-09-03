package com.github.schottky.zener.localization;

import java.util.Map;

public abstract class I18n {

    public static String of(String identifier) {
        return Language.current().translate(identifier);
    }

    public static String of(String identifier, String replace, Object with) {
        return Language.current().translateWithExtra(identifier, replace, with);
    }

    public static String of(String identifier, String replace1, Object with1, String replace2, Object with2) {
        return Language.current().translateWithExtra(identifier, replace1, with1, replace2, with2);
    }

    public static String of(String identifier, Map<String,Object> map) {
        return Language.current().translateWithExtra(identifier, map);
    }
}
