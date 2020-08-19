package com.github.schottky.command;

import com.github.schottky.zener.command.Cmd;
import com.github.schottky.zener.command.CommandBase;
import com.github.schottky.zener.command.SubCmd;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@Cmd(value = "test", permission = "perm")
public class TestCommand extends CommandBase {

    public Consumer<Object[]> callback;

    @SubCmd("subCmd")
    public void subCmd(int arg1, boolean arg2) {
        if (callback != null) callback.accept(new Object[] {arg1, arg2});
    }

    @SubCmd("varArgsCmd")
    public void varArgsCmd(ItemStack arg1) {
        if (callback != null) callback.accept(new Object[] {arg1});
    }


}
