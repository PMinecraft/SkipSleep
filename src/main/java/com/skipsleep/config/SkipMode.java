package com.skipsleep.config;

/**
 * 跳过夜晚的模式枚举
 */
public enum SkipMode {
    /**
     * 固定人数模式 - 需要指定数量的玩家睡觉才能跳过夜晚
     */
    NUMBER("num"),
    
    /**
     * 百分比模式 - 需要指定百分比的玩家睡觉才能跳过夜晚
     */
    PERCENTAGE("pet");

    private final String configValue;

    SkipMode(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigValue() {
        return configValue;
    }

    /**
     * 从配置值解析模式
     */
    public static SkipMode fromConfig(String value) {
        for (SkipMode mode : values()) {
            if (mode.configValue.equalsIgnoreCase(value)) {
                return mode;
            }
        }
        return NUMBER; // 默认值
    }
}
