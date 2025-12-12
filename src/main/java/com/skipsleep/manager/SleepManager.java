package com.skipsleep.manager;

import com.skipsleep.SkipSleep;
import com.skipsleep.config.SkipMode;
import com.skipsleep.model.WorldSleepState;
import com.skipsleep.service.AfkService;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 睡眠管理器 - 管理睡眠计数和状态
 * 支持多世界隔离的睡眠状态
 */
public class SleepManager {

    private final SkipSleep plugin;
    private final AfkService afkService;
    private final DecimalFormat percentFormat;
    
    // 每个世界独立的睡眠状态
    private final Map<UUID, WorldSleepState> worldStates;

    public SleepManager(SkipSleep plugin, AfkService afkService) {
        this.plugin = plugin;
        this.afkService = afkService;
        this.percentFormat = new DecimalFormat("0.00");
        this.worldStates = new HashMap<>();
    }

    /**
     * 获取或创建世界睡眠状态
     */
    private WorldSleepState getWorldState(World world) {
        return worldStates.computeIfAbsent(world.getUID(), k -> new WorldSleepState());
    }

    /**
     * 重置所有世界的睡眠状态
     */
    public void reset() {
        worldStates.clear();
    }

    /**
     * 重置指定世界的睡眠状态
     */
    public void resetWorld(World world) {
        worldStates.remove(world.getUID());
    }

    /**
     * 增加睡眠玩家计数
     */
    public void incrementSleepCount(World world) {
        WorldSleepState state = getWorldState(world);
        state.incrementSleepingCount();
        updatePercentage(world, state);
    }

    /**
     * 减少睡眠玩家计数
     */
    public void decrementSleepCount(World world) {
        WorldSleepState state = getWorldState(world);
        state.decrementSleepingCount();
        updatePercentage(world, state);
    }

    /**
     * 更新百分比
     */
    private void updatePercentage(World world, WorldSleepState state) {
        int eligiblePlayers = getEligiblePlayerCount(world);
        if (eligiblePlayers > 0) {
            double rawPercent = (double) state.getSleepingPlayerCount() / eligiblePlayers * 100;
            state.setCurrentPercentage(Double.parseDouble(percentFormat.format(rawPercent)));
        } else {
            state.setCurrentPercentage(0.0);
        }
    }

    /**
     * 获取有资格参与睡眠计数的玩家数量
     * 排除 AFK 玩家（如果配置启用）
     */
    public int getEligiblePlayerCount(World world) {
        int count = 0;
        for (Player player : world.getPlayers()) {
            if (!afkService.isAfk(player)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 检查是否满足跳过夜晚条件
     */
    public boolean shouldSkipNight(World world) {
        WorldSleepState state = getWorldState(world);
        SkipMode mode = plugin.getConfigManager().getSkipMode();
        
        if (mode == SkipMode.NUMBER) {
            return state.getSleepingPlayerCount() >= plugin.getConfigManager().getSkipNumber();
        } else {
            return state.getCurrentPercentage() >= plugin.getConfigManager().getSkipPercentage();
        }
    }

    /**
     * 检查是否已达到睡眠上限
     */
    public boolean isAtCapacity(World world) {
        return shouldSkipNight(world);
    }

    /**
     * 执行跳过夜晚
     */
    public void executeSkipNight(World world) {
        world.setTime(0);
        world.setStorm(false);
        world.setClearWeatherDuration(0);
        resetWorld(world);
    }

    /**
     * 检查世界是否是夜晚或暴风雨
     */
    public boolean isNightOrStorm(World world) {
        long time = world.getTime();
        // 12541 是 Minecraft 中可以睡觉的最早时间
        return time >= 12541 || world.hasStorm();
    }

    // ============== Getters ==============

    public int getSleepingPlayerCount(World world) {
        return getWorldState(world).getSleepingPlayerCount();
    }

    public double getCurrentPercentage(World world) {
        return getWorldState(world).getCurrentPercentage();
    }

    public boolean isSkipInProgress(World world) {
        return getWorldState(world).isSkipInProgress();
    }

    public void setSkipInProgress(World world, boolean skipInProgress) {
        getWorldState(world).setSkipInProgress(skipInProgress);
    }

    // 用于向后兼容的无参方法（使用主世界）
    public int getSleepingPlayerCount() {
        World mainWorld = Bukkit.getWorlds().get(0);
        return getSleepingPlayerCount(mainWorld);
    }

    public double getCurrentPercentage() {
        World mainWorld = Bukkit.getWorlds().get(0);
        return getCurrentPercentage(mainWorld);
    }
}
