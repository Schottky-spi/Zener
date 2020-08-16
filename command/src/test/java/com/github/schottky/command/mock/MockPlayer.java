package com.github.schottky.command.mock;

import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.*;

public class MockPlayer extends MockCommandSender implements Player {
    @Override
    public @NotNull String getDisplayName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDisplayName(@Nullable String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull String getPlayerListName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPlayerListName(@Nullable String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable String getPlayerListHeader() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable String getPlayerListFooter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPlayerListHeader(@Nullable String header) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPlayerListFooter(@Nullable String footer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPlayerListHeaderFooter(@Nullable String header, @Nullable String footer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCompassTarget(@NotNull Location loc) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Location getCompassTarget() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable InetSocketAddress getAddress() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isConversing() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void acceptConversationInput(@NotNull String input) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean beginConversation(@NotNull Conversation conversation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation, @NotNull ConversationAbandonedEvent details) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendRawMessage(@NotNull String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void kickPlayer(@Nullable String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void chat(@NotNull String msg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean performCommand(@NotNull String command) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSneaking() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSneaking(boolean sneak) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSprinting() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void loadData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSleepingIgnored(boolean isSleeping) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSleepingIgnored() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void playNote(@NotNull Location loc, byte instrument, byte note) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void playNote(@NotNull Location loc, @NotNull Instrument instrument, @NotNull Note note) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull Sound sound, float volume, float pitch) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull String sound, float volume, float pitch) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void stopSound(@NotNull Sound sound) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void stopSound(@NotNull String sound) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void stopSound(@NotNull Sound sound, @Nullable SoundCategory category) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void stopSound(@NotNull String sound, @Nullable SoundCategory category) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void playEffect(@NotNull Location loc, @NotNull Effect effect, int data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void playEffect(@NotNull Location loc, @NotNull Effect effect, @Nullable T data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendBlockChange(@NotNull Location loc, @NotNull Material material, byte data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendBlockChange(@NotNull Location loc, @NotNull BlockData block) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean sendChunkChange(@NotNull Location loc, int sx, int sy, int sz, @NotNull byte[] data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendSignChange(@NotNull Location loc, @Nullable String[] lines) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendSignChange(@NotNull Location loc, @Nullable String[] lines, @NotNull DyeColor dyeColor) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendMap(@NotNull MapView map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateInventory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, int newValue) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, @NotNull Material material, int newValue) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int amount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getPlayerTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getPlayerTimeOffset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPlayerTimeRelative() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetPlayerTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPlayerWeather(@NotNull WeatherType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable WeatherType getPlayerWeather() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetPlayerWeather() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void giveExp(int amount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void giveExpLevels(int amount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getExp() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setExp(float exp) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLevel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLevel(int level) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getTotalExperience() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTotalExperience(int exp) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getExhaustion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setExhaustion(float value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getSaturation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSaturation(float value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getFoodLevel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFoodLevel(int value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getAllowFlight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAllowFlight(boolean flight) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void hidePlayer(@NotNull Player player) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void hidePlayer(@NotNull Plugin plugin, @NotNull Player player) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showPlayer(@NotNull Player player) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showPlayer(@NotNull Plugin plugin, @NotNull Player player) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canSee(@NotNull Player player) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isFlying() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFlying(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFlySpeed(float value) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWalkSpeed(float value) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getFlySpeed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getWalkSpeed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTexturePack(@NotNull String url) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setResourcePack(@NotNull String url) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setResourcePack(@NotNull String url, @NotNull byte[] hash) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Scoreboard getScoreboard() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setScoreboard(@NotNull Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isHealthScaled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHealthScaled(boolean scale) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHealthScale(double scale) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getHealthScale() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Entity getSpectatorTarget() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSpectatorTarget(@Nullable Entity entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendTitle(@Nullable String title, @Nullable String subtitle) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendTitle(@Nullable String title, @Nullable String subtitle, int fadeIn, int stay, int fadeOut) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetTitle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, @Nullable T data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, @Nullable T data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull AdvancementProgress getAdvancementProgress(@NotNull Advancement advancement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getClientViewDistance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull String getLocale() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateCommands() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void openBook(@NotNull ItemStack book) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Player.@NotNull Spigot spigot() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOnline() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isBanned() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWhitelisted() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWhitelisted(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Player getPlayer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getFirstPlayed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getLastPlayed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPlayedBefore() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull PlayerInventory getInventory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Inventory getEnderChest() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull MainHand getMainHand() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setWindowProperty(InventoryView.@NotNull Property prop, int value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull InventoryView getOpenInventory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable InventoryView openInventory(@NotNull Inventory inventory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable InventoryView openWorkbench(@Nullable Location location, boolean force) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable InventoryView openEnchanting(@Nullable Location location, boolean force) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void openInventory(@NotNull InventoryView inventory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable InventoryView openMerchant(@NotNull Villager trader, boolean force) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable InventoryView openMerchant(@NotNull Merchant merchant, boolean force) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void closeInventory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull ItemStack getItemInHand() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setItemInHand(@Nullable ItemStack item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull ItemStack getItemOnCursor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setItemOnCursor(@Nullable ItemStack item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasCooldown(@NotNull Material material) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getCooldown(@NotNull Material material) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCooldown(@NotNull Material material, int ticks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getSleepTicks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Location getBedSpawnLocation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBedSpawnLocation(@Nullable Location location) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBedSpawnLocation(@Nullable Location location, boolean force) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean sleep(@NotNull Location location, boolean force) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void wakeup(boolean setSpawnLocation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Location getBedLocation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull GameMode getGameMode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setGameMode(@NotNull GameMode mode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isBlocking() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isHandRaised() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getExpToLevel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean discoverRecipe(@NotNull NamespacedKey recipe) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int discoverRecipes(@NotNull Collection<NamespacedKey> recipes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean undiscoverRecipe(@NotNull NamespacedKey recipe) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int undiscoverRecipes(@NotNull Collection<NamespacedKey> recipes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Entity getShoulderEntityLeft() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setShoulderEntityLeft(@Nullable Entity entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Entity getShoulderEntityRight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setShoulderEntityRight(@Nullable Entity entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getEyeHeight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Location getEyeLocation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull List<Block> getLineOfSight(@Nullable Set<Material> transparent, int maxDistance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Block getTargetBlock(@Nullable Set<Material> transparent, int maxDistance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull List<Block> getLastTwoTargetBlocks(@Nullable Set<Material> transparent, int maxDistance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Block getTargetBlockExact(int maxDistance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Block getTargetBlockExact(int maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable RayTraceResult rayTraceBlocks(double maxDistance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable RayTraceResult rayTraceBlocks(double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getRemainingAir() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRemainingAir(int ticks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaximumAir() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaximumAir(int ticks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaximumNoDamageTicks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getLastDamage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLastDamage(double damage) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNoDamageTicks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Player getKiller() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addPotionEffect(@NotNull PotionEffect effect) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addPotionEffect(@NotNull PotionEffect effect, boolean force) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addPotionEffects(@NotNull Collection<PotionEffect> effects) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPotionEffect(@NotNull PotionEffectType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable PotionEffect getPotionEffect(@NotNull PotionEffectType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removePotionEffect(@NotNull PotionEffectType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Collection<PotionEffect> getActivePotionEffects() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasLineOfSight(@NotNull Entity other) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable EntityEquipment getEquipment() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getCanPickupItems() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isLeashed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Entity getLeashHolder() throws IllegalStateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setLeashHolder(@Nullable Entity holder) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isGliding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setGliding(boolean gliding) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSwimming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSwimming(boolean swimming) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRiptiding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSleeping() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAI(boolean ai) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasAI() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCollidable(boolean collidable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCollidable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> @Nullable T getMemory(@NotNull MemoryKey<T> memoryKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void setMemory(@NotNull MemoryKey<T> memoryKey, @Nullable T memoryValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable AttributeInstance getAttribute(@NotNull Attribute attribute) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void damage(double amount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void damage(double amount, @Nullable Entity source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getHealth() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHealth(double health) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getAbsorptionAmount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAbsorptionAmount(double amount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getMaxHealth() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxHealth(double health) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetMaxHealth() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Location getLocation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Location getLocation(@Nullable Location loc) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setVelocity(@NotNull Vector velocity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Vector getVelocity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getHeight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getWidth() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull BoundingBox getBoundingBox() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOnGround() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull World getWorld() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean teleport(@NotNull Location location) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean teleport(@NotNull Location location, PlayerTeleportEvent.@NotNull TeleportCause cause) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean teleport(@NotNull Entity destination) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean teleport(@NotNull Entity destination, PlayerTeleportEvent.@NotNull TeleportCause cause) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull List<Entity> getNearbyEntities(double x, double y, double z) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getEntityId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getFireTicks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaxFireTicks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFireTicks(int ticks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDead() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isValid() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPersistent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPersistent(boolean persistent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Entity getPassenger() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setPassenger(@NotNull Entity passenger) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull List<Entity> getPassengers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addPassenger(@NotNull Entity passenger) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removePassenger(@NotNull Entity passenger) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean eject() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getFallDistance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFallDistance(float distance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLastDamageCause(@Nullable EntityDamageEvent event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable EntityDamageEvent getLastDamageCause() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull UUID getUniqueId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getTicksLived() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTicksLived(int value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void playEffect(@NotNull EntityEffect type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull EntityType getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isInsideVehicle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean leaveVehicle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Entity getVehicle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCustomNameVisible() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setGlowing(boolean flag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isGlowing() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setInvulnerable(boolean flag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isInvulnerable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSilent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSilent(boolean flag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasGravity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setGravity(boolean gravity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getPortalCooldown() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Set<String> getScoreboardTags() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addScoreboardTag(@NotNull String tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeScoreboardTag(@NotNull String tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull PistonMoveReaction getPistonMoveReaction() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull BlockFace getFacing() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Pose getPose() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable String getCustomName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCustomName(@Nullable String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull List<MetadataValue> getMetadata(@NotNull String metadataKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasMetadata(@NotNull String metadataKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull PersistentDataContainer getPersistentDataContainer() {
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

    @Override
    public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> projectile) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> projectile, @Nullable Vector velocity) {
        throw new UnsupportedOperationException();
    }
}
