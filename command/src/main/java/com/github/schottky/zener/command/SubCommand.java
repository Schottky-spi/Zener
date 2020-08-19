package com.github.schottky.zener.command;

import org.apiguardian.api.API;

@API(status = API.Status.EXPERIMENTAL)
public abstract class SubCommand<T extends CommandBase> extends CommandBase {

    protected final T parentCommand;

    public SubCommand(T parentCommand) {
        this.parentCommand = parentCommand;
    }

    /**
     * returns the amount of parent-commands this class has.
     * This is implemented recursively by simply adding one to the
     * parent's depth-method.
     * The Base-class will return 0
     * @return The depth of it's parent plus one
     */

    @Override
    int computeDepth() {
        return 1 + this.parentCommand.computeDepth();
    }

    @Override
    public String toString() {
        return "SubCommand{" +
                "name='" + name + "', " +
                "parentCommand=" + parentCommand +
                '}';
    }
}
