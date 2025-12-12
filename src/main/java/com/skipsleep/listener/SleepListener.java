package com.skipsleep.listener;

import com.skipsleep.SkipSleep;
import com.skipsleep.config.SkipMode;
import com.skipsleep.manager.SleepManager;
import com.skipsleep.service.MessageService;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * 睡眠事件监听器
 */
public class SleepListener implements Listener {

    private static final long SKIP_DELAY_TICKS = 100L; // 5秒延迟

    private final SkipSleep plugin;
    private final MessageService messageService;

    public SleepListener(SkipSleep plugin, MessageService messageService) {
        this.plugin = plugin;
        this.messageService = messageService;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        // 检查事件结果是否允许睡觉
        if (event.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK) {
            return;
        }

        // 检查插件是否启用
        if (!plugin.getConfigManager().isSkipEnabled()) {
            return;
        }

        Player player = event.getPlayer();
        World world = player.getWorld();

        // 检查世界是否被禁用
        if (plugin.getConfigManager().isWorldDisabled(world.getName())) {
            return;
        }

        SleepManager sleepManager = plugin.getSleepManager();

        // 检查是否已达到睡眠上限
        if (sleepManager.isAtCapacity(world)) {
            event.setCancelled(true);
            messageService.send(player, "SKIP_NIGHT_NUMBER_FULL");
            return;
        }

        // 检查是否是夜晚或暴风雨
        if (!sleepManager.isNightOrStorm(world)) {
            return;
        }

        // 延迟一tick执行，确保事件完成
        new BukkitRunnable() {
            @Override
            public void run() {
                if (event.isCancelled()) {
                    return;
                }

                handleBedEnter(player, world);
            }
        }.runTaskLater(plugin, 1L);
    }

    /**
     * 处理玩家进入床
     */
    private void handleBedEnter(Player player, World world) {
        SleepManager sleepManager = plugin.getSleepManager();
        sleepManager.incrementSleepCount(world);

        // 广播进度消息
        if (plugin.getConfigManager().isProgressMessagesEnabled()) {
            broadcastSleepProgress(world);
        }

        // 播放通知音效
        playNotificationSound(world);

        // 检查是否满足跳过条件
        if (sleepManager.shouldSkipNight(world)) {
            sleepManager.setSkipInProgress(world, true);
            
            if (plugin.getConfigManager().isSkipMessageEnabled()) {
                messageService.broadcastToWorld(world, "SKIP_NIGHT");
            }

            // 延迟执行跳过夜晚
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (sleepManager.shouldSkipNight(world)) {
                        sleepManager.executeSkipNight(world);
                    } else {
                        messageService.broadcastToWorld(world, "SKIP_FAILED");
                        sleepManager.setSkipInProgress(world, false);
                    }
                }
            }.runTaskLater(plugin, SKIP_DELAY_TICKS);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        // 检查插件是否启用
        if (!plugin.getConfigManager().isSkipEnabled()) {
            return;
        }

        Player player = event.getPlayer();
        World world = player.getWorld();

        // 检查世界是否被禁用
        if (plugin.getConfigManager().isWorldDisabled(world.getName())) {
            return;
        }

        SleepManager sleepManager = plugin.getSleepManager();

        // 如果跳过正在进行中，不减少计数
        if (sleepManager.isSkipInProgress(world)) {
            return;
        }

        // 检查是否是夜晚或暴风雨
        if (!sleepManager.isNightOrStorm(world)) {
            return;
        }

        sleepManager.decrementSleepCount(world);

        // 广播更新的进度
        if (sleepManager.getSleepingPlayerCount(world) > 0) {
            if (plugin.getConfigManager().isProgressMessagesEnabled()) {
                broadcastSleepProgress(world);
            }
            playNotificationSound(world);
        }
    }

    /**
     * 广播睡眠进度消息
     */
    private void broadcastSleepProgress(World world) {
        SkipMode mode = plugin.getConfigManager().getSkipMode();

        // 使用世界特定的消息（通过占位符）
        if (mode == SkipMode.NUMBER) {
            messageService.broadcastToWorld(world, "SKIP_NUMBER_TOTAL_NUMBER");
        } else {
            messageService.broadcastToWorld(world, "SKIP_PET_TOTAL_PET");
        }
    }

    /**
     * 播放通知音效
     */
    private void playNotificationSound(World world) {
        if (!plugin.getConfigManager().isSoundEnabled()) {
            return;
        }

        Sound sound = plugin.getConfigManager().getNotificationSound();
        float volume = plugin.getConfigManager().getSoundVolume();
        float pitch = plugin.getConfigManager().getSoundPitch();

        for (Player player : world.getPlayers()) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }
}
