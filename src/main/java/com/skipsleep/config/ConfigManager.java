package com.skipsleep.config;

import com.skipsleep.SkipSleep;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.List;

/**
 * 配置管理器 - 封装所有配置访问
 */
public class ConfigManager {
    private final SkipSleep plugin;
    private FileConfiguration config;

    public ConfigManager(SkipSleep plugin) {
        this.plugin = plugin;
        reload();
    }

    /**
     * 重新加载配置
     */
    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    /**
     * 保存配置到文件
     */
    public void save() {
        plugin.saveConfig();
    }

    // ============== 基础设置 ==============

    /**
     * 是否启用更新检查
     */
    public boolean isUpdateCheckEnabled() {
        return config.getBoolean("update", true);
    }

    /**
     * 是否启用跳过夜晚功能
     */
    public boolean isSkipEnabled() {
        return config.getBoolean("skip", true);
    }

    /**
     * 获取跳过模式
     */
    public SkipMode getSkipMode() {
        String mode = config.getString("model", "num");
        return SkipMode.fromConfig(mode);
    }

    /**
     * 获取跳过夜晚需要的人数（固定人数模式）
     */
    public int getSkipNumber() {
        return config.getInt("skipNum", 1);
    }

    /**
     * 获取跳过夜晚需要的百分比（百分比模式）
     */
    public int getSkipPercentage() {
        return config.getInt("skipPet", 50);
    }

    /**
     * 获取语言设置
     */
    public String getLanguage() {
        return config.getString("Language.lang", "chinese");
    }

    // ============== 音效设置 ==============

    /**
     * 是否启用音效
     */
    public boolean isSoundEnabled() {
        return config.getBoolean("sound.enabled", true);
    }

    /**
     * 获取通知音效
     */
    public Sound getNotificationSound() {
        String soundName = config.getString("sound.name", "BLOCK_NOTE_BLOCK_BASS");
        try {
            return Sound.valueOf(soundName.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid sound name: " + soundName + ", using default");
            return Sound.BLOCK_NOTE_BLOCK_BASS;
        }
    }

    /**
     * 获取音效音量
     */
    public float getSoundVolume() {
        return (float) config.getDouble("sound.volume", 1.0);
    }

    /**
     * 获取音效音调
     */
    public float getSoundPitch() {
        return (float) config.getDouble("sound.pitch", 1.0);
    }

    // ============== AFK 设置 ==============

    /**
     * 是否排除 AFK 玩家
     */
    public boolean isAfkExclusionEnabled() {
        return config.getBoolean("afk.excludeAfk", false);
    }

    /**
     * 获取 AFK 不活动阈值（秒）
     */
    public int getAfkInactivityThreshold() {
        return config.getInt("afk.inactivityThreshold", 300);
    }

    // ============== 世界设置 ==============

    /**
     * 获取禁用的世界列表
     */
    public List<String> getDisabledWorlds() {
        return config.getStringList("disabledWorlds");
    }

    /**
     * 检查世界是否被禁用
     */
    public boolean isWorldDisabled(String worldName) {
        List<String> disabledWorlds = getDisabledWorlds();
        if (disabledWorlds == null || disabledWorlds.isEmpty()) {
            return false;
        }
        return disabledWorlds.stream()
                .anyMatch(w -> w.equalsIgnoreCase(worldName));
    }

    // ============== 消息设置 ==============

    /**
     * 是否显示进度消息
     */
    public boolean isProgressMessagesEnabled() {
        return config.getBoolean("showProgressMessages", true);
    }

    /**
     * 是否显示跳过消息
     */
    public boolean isSkipMessageEnabled() {
        return config.getBoolean("showSkipMessage", true);
    }

    // ============== 修改配置 ==============

    /**
     * 设置是否启用更新检查
     */
    public void setUpdateCheckEnabled(boolean enabled) {
        config.set("update", enabled);
        save();
    }

    /**
     * 设置是否启用跳过夜晚功能
     */
    public void setSkipEnabled(boolean enabled) {
        config.set("skip", enabled);
        save();
    }

    /**
     * 设置跳过模式
     */
    public void setSkipMode(SkipMode mode) {
        config.set("model", mode.getConfigValue());
        save();
    }

    /**
     * 设置跳过夜晚需要的人数
     */
    public void setSkipNumber(int number) {
        config.set("skipNum", number);
        save();
    }

    /**
     * 设置跳过夜晚需要的百分比
     */
    public void setSkipPercentage(int percentage) {
        config.set("skipPet", percentage);
        save();
    }

    /**
     * 设置是否启用音效
     */
    public void setSoundEnabled(boolean enabled) {
        config.set("sound.enabled", enabled);
        save();
    }
}
