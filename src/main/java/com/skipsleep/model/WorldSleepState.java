package com.skipsleep.model;

/**
 * 世界睡眠状态模型
 * 封装单个世界的睡眠相关状态
 */
public class WorldSleepState {

    private int sleepingPlayerCount;
    private double currentPercentage;
    private boolean skipInProgress;

    public WorldSleepState() {
        this.sleepingPlayerCount = 0;
        this.currentPercentage = 0.0;
        this.skipInProgress = false;
    }

    // ============== Getters ==============

    /**
     * 获取正在睡觉的玩家数量
     */
    public int getSleepingPlayerCount() {
        return sleepingPlayerCount;
    }

    /**
     * 获取当前睡眠百分比
     */
    public double getCurrentPercentage() {
        return currentPercentage;
    }

    /**
     * 检查跳过夜晚是否正在进行中
     */
    public boolean isSkipInProgress() {
        return skipInProgress;
    }

    // ============== Setters ==============

    /**
     * 设置正在睡觉的玩家数量
     */
    public void setSleepingPlayerCount(int sleepingPlayerCount) {
        this.sleepingPlayerCount = Math.max(0, sleepingPlayerCount);
    }

    /**
     * 设置当前睡眠百分比
     */
    public void setCurrentPercentage(double currentPercentage) {
        this.currentPercentage = Math.max(0.0, currentPercentage);
    }

    /**
     * 设置跳过夜晚进行状态
     */
    public void setSkipInProgress(boolean skipInProgress) {
        this.skipInProgress = skipInProgress;
    }

    // ============== 便捷方法 ==============

    /**
     * 增加睡眠玩家计数
     */
    public void incrementSleepingCount() {
        this.sleepingPlayerCount++;
    }

    /**
     * 减少睡眠玩家计数
     */
    public void decrementSleepingCount() {
        if (this.sleepingPlayerCount > 0) {
            this.sleepingPlayerCount--;
        }
    }

    /**
     * 重置状态
     */
    public void reset() {
        this.sleepingPlayerCount = 0;
        this.currentPercentage = 0.0;
        this.skipInProgress = false;
    }
}
