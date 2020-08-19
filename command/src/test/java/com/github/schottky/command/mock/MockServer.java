package com.github.schottky.command.mock;

import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.*;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.loot.LootTable;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class MockServer implements Server {
    @Override
    public @NotNull String getName() {
        return "MockServer";
    }

    @Override
    public @NotNull String getVersion() {
        return "v1.0";
    }

    @Override
    public @NotNull String getBukkitVersion() {
        return "v1.0";
    }

    @Override
    public @NotNull Collection<? extends Player> getOnlinePlayers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaxPlayers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getPort() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getViewDistance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull String getIp() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull String getWorldType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getGenerateStructures() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getAllowEnd() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getAllowNether() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasWhitelist() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWhitelist(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Set<OfflinePlayer> getWhitelistedPlayers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reloadWhitelist() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int broadcastMessage(@NotNull String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull String getUpdateFolder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull File getUpdateFolderFile() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getConnectionThrottle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getTicksPerAnimalSpawns() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getTicksPerMonsterSpawns() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Player getPlayer(@NotNull String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Player getPlayerExact(@NotNull String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull List<Player> matchPlayer(@NotNull String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Player getPlayer(@NotNull UUID id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull PluginManager getPluginManager() {
        return new MockPluginManager();
    }

    @Override
    public @NotNull BukkitScheduler getScheduler() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull ServicesManager getServicesManager() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull List<World> getWorlds() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable World createWorld(@NotNull WorldCreator creator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean unloadWorld(@NotNull String name, boolean save) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean unloadWorld(@NotNull World world, boolean save) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable World getWorld(@NotNull String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable World getWorld(@NotNull UUID uid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable MapView getMap(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull MapView createMap(@NotNull World world) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull ItemStack createExplorerMap(@NotNull World world, @NotNull Location location, @NotNull StructureType structureType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull ItemStack createExplorerMap(@NotNull World world, @NotNull Location location, @NotNull StructureType structureType, int radius, boolean findUnexplored) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reloadData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Logger getLogger() {
        return Logger.getGlobal();
    }

    @Override
    public @Nullable PluginCommand getPluginCommand(@NotNull String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void savePlayers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean dispatchCommand(@NotNull CommandSender sender, @NotNull String commandLine) throws CommandException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addRecipe(@Nullable Recipe recipe) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull List<Recipe> getRecipesFor(@NotNull ItemStack result) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Iterator<Recipe> recipeIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearRecipes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetRecipes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Map<String, String[]> getCommandAliases() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getSpawnRadius() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSpawnRadius(int value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getOnlineMode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getAllowFlight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isHardcore() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shutdown() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int broadcast(@NotNull String message, @NotNull String permission) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull OfflinePlayer getOfflinePlayer(@NotNull String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull OfflinePlayer getOfflinePlayer(@NotNull UUID id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Set<String> getIPBans() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void banIP(@NotNull String address) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unbanIP(@NotNull String address) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Set<OfflinePlayer> getBannedPlayers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull BanList getBanList(BanList.@NotNull Type type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Set<OfflinePlayer> getOperators() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull GameMode getDefaultGameMode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDefaultGameMode(@NotNull GameMode mode) {
        throw new UnsupportedOperationException();
    }

    private final ConsoleCommandSender sender = new MockConsoleCommandSender();

    @Override
    public @NotNull ConsoleCommandSender getConsoleSender() {
        return sender;
    }

    @Override
    public @NotNull File getWorldContainer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull OfflinePlayer[] getOfflinePlayers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Messenger getMessenger() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull HelpMap getHelpMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Inventory createInventory(@Nullable InventoryHolder owner, @NotNull InventoryType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Inventory createInventory(@Nullable InventoryHolder owner, @NotNull InventoryType type, @NotNull String title) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Inventory createInventory(@Nullable InventoryHolder owner, int size) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Inventory createInventory(@Nullable InventoryHolder owner, int size, @NotNull String title) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Merchant createMerchant(@Nullable String title) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMonsterSpawnLimit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAnimalSpawnLimit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAmbientSpawnLimit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPrimaryThread() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull String getMotd() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable String getShutdownMessage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Warning.@NotNull WarningState getWarningState() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull ItemFactory getItemFactory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable ScoreboardManager getScoreboardManager() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable CachedServerIcon getServerIcon() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull CachedServerIcon loadServerIcon(@NotNull File file) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull CachedServerIcon loadServerIcon(@NotNull BufferedImage image) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setIdleTimeout(int threshold) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getIdleTimeout() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ChunkGenerator.@NotNull ChunkData createChunkData(@NotNull World world) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull BossBar createBossBar(@Nullable String title, @NotNull BarColor color, @NotNull BarStyle style, @NotNull BarFlag... flags) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull KeyedBossBar createBossBar(@NotNull NamespacedKey key, @Nullable String title, @NotNull BarColor color, @NotNull BarStyle style, @NotNull BarFlag... flags) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Iterator<KeyedBossBar> getBossBars() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable KeyedBossBar getBossBar(@NotNull NamespacedKey key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeBossBar(@NotNull NamespacedKey key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Entity getEntity(@NotNull UUID uuid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Advancement getAdvancement(@NotNull NamespacedKey key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Iterator<Advancement> advancementIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull BlockData createBlockData(@NotNull Material material) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull BlockData createBlockData(@NotNull Material material, @Nullable Consumer<BlockData> consumer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull BlockData createBlockData(@NotNull String data) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull BlockData createBlockData(@Nullable Material material, @Nullable String data) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable <T extends Keyed> Tag<T> getTag(@NotNull String registry, @NotNull NamespacedKey tag, @NotNull Class<T> clazz) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull <T extends Keyed> Iterable<Tag<T>> getTags(@NotNull String registry, @NotNull Class<T> clazz) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable LootTable getLootTable(@NotNull NamespacedKey key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull List<Entity> selectEntities(@NotNull CommandSender sender, @NotNull String selector) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull UnsafeValues getUnsafe() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Spigot spigot() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, @NotNull byte[] message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Set<String> getListeningPluginChannels() {
        throw new UnsupportedOperationException();
    }
}
