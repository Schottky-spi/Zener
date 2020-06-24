package de.schottky.zener.command;

import org.bukkit.permissions.PermissionDefault;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cmd {
    String name();
    String permission();
    int minArgs() default 0;
    int maxArgs() default -1;
    PermissionDefault permDefault() default PermissionDefault.OP;
}
