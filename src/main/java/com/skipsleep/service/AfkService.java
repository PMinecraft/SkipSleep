package com.skipsleep.service;

import com.skipsleep.config.ConfigManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * AFK 服务 - 管理玩家活动状态追踪和 AFK 检测
 */
public class AfkService {

    private final ConfigManager configManager;
    
    // 玩家活动时间追踪（用于 AFK 检测）
    private final Map<UUID, Long> playerLastActivity;

    public AfkService(ConfigManager configManager) {
        this.configManager = configManager;
        this.playerLastActivity = new HashMap<>();
    }

    /**
     * 检查玩家是否处于 AFK 状态
     * 使用内置活动时间追踪进行检测
     *
     * @param player 要检查的玩家
     * @return 如果玩家 AFK 则返回 true
     */
    public boolean isAfk(Player player) {
        if (!configManager.isAfkExclusionEnabled()) {
            return false;
        }

        int threshold = configManager.getAfkInactivityThreshold();
        if (threshold > 0) {
            Long lastActivity = playerLastActivity.get(player.getUniqueId());
            if (lastActivity != null) {
                long inactiveSeconds = (System.currentTimeMillis() - lastActivity) / 1000;
                return inactiveSeconds >= threshold;
            }
            // 如果没有活动记录，认为玩家是活跃的
            return false;
        }

        return false;
    }

    /**
     * 更新玩家活动时间
     * 应在玩家进行任何活动（移动、聊天、交互等）时调用
     *
     * @param player 活动的玩家
     */
    public void updateActivity(Player player) {
        playerLastActivity.put(player.getUniqueId(), System.currentTimeMillis());
    }

    /**
     * 移除玩家活动记录
     * 应在玩家退出服务器时调用
     *
     * @param player 退出的玩家
     */
    public void removePlayer(Player player) {
        playerLastActivity.remove(player.getUniqueId());
    }

    /**
     * 清除所有活动记录
     * 用于插件重载时
     */
    public void reset() {
        playerLastActivity.clear();
    }
}
