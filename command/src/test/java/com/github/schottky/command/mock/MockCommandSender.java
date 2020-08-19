package com.github.schottky.command.mock;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Consumer;

public abstract class MockCommandSender implements CommandSender {

    public static Consumer<String> callback;

    @Override
    public void sendMessage(@NotNull String message) {
        System.out.println("[to " + this.getClass().getSimpleName() + "] " + message);
        if (callback != null) callback.accept(message);
    }

    @Override
    public void sendMessage(@NotNull String[] messages) {
        for (String message: messages)
            sendMessage(message);
    }

    @Override
    public @NotNull Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    public @NotNull String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPermissionSet(@NotNull String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission perm) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPermission(@NotNull String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPermission(@NotNull Permission perm) {
        return true;
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value, int ticks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, int ticks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAttachment(@NotNull PermissionAttachment attachment) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void recalculatePermissions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOp() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException();
    }
}
