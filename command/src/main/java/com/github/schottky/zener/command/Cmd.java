package com.github.schottky.zener.command;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.permissions.PermissionDefault;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a valid command.
 * Every command needs to have a name that uniquely identifies this command.
 * This name must be equal to the name given in the config.yml
 */

@API(status = Status.EXPERIMENTAL)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cmd {

    /**
     * The unique name of this command. Must be the same as the name in the config.yml
     * @return The name
     */

    String name();

    /**
     * the permission. If empty, it is assumed to be the same as what was defined in the config.yml
     * If not, a new permission will be registered using this name and this {@link #permDefault()}
     * @return The permission needed to execute this command. Can be empty (as defined in {@link String#isEmpty()})
     */

    String permission() default "";

    /**
     * The permission-default. Defaults to OP
     * @return The permission-default
     */

    PermissionDefault permDefault() default PermissionDefault.OP;

    /**
     * the minimum amount of arguments a player has to specify so that this command can work
     * <br>defaults to 0
     * @return the minimum amount of arguments
     */

    int minArgs() default 0;

    /**
     * the maximum amount of arguments a player can specify.
     * -1 indicates an unlimited amount of arguments
     * <br> defaults to -1
     * @return the maximum amount of arguments
     */

    int maxArgs() default -1;

    /**
     * aliases that may be used to alternatively access this command.
     * This should especially be used in sub-commands.
     * @return The aliases that can be used to trigger this command
     */

    String[] aliases() default {};
}
