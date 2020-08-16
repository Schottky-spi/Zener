package com.github.schottky.zener.command;

import org.bukkit.permissions.PermissionDefault;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubCmd {

    String value();

    String permission() default "";

    PermissionDefault permDefault() default PermissionDefault.OP;

    String[] aliases() default {};

    String desc() default "";

}
