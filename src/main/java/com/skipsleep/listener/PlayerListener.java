package com.skipsleep.listener;

import com.skipsleep.SkipSleep;
import com.skipsleep.service.AfkService;
import com.skipsleep.service.MessageService;
import com.skipsleep.update.UpdateChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * 玩家事件监听器
 */
public class PlayerListener implements Listener {

    private final SkipSleep plugin;
    private final MessageService messageService;
    private final AfkService afkService;
    private final UpdateChecker updateChecker;

    public PlayerListener(SkipSleep plugin, MessageService messageService, 
                          AfkService afkService, UpdateChecker updateChecker) {
        this.plugin = plugin;
        this.messageService = messageService;
        this.afkService = afkService;
        this.updateChecker = updateChecker;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // 初始化玩家活动时间
        afkService.updateActivity(player);

        // 通知管理员有新版本
        if (player.isOp() && plugin.getConfigManager().isUpdateCheckEnabled()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    checkAndNotifyUpdate(player);
                }
            }.runTaskAsynchronously(plugin);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        // 清理玩家活动记录
        afkService.removePlayer(player);

        // 延迟检查，可能需要重置世界状态
        new BukkitRunnable() {
            @Override
            public void run() {
                // 如果世界没有玩家了，重置该世界的睡眠状态
                if (player.getWorld().getPlayers().isEmpty()) {
                    plugin.getSleepManager().resetWorld(player.getWorld());
                }
            }
        }.runTaskLater(plugin, 1L);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        // 只有在启用 AFK 排除时才追踪活动
        if (!plugin.getConfigManager().isAfkExclusionEnabled()) {
            return;
        }

        // 只检测位置变化（不是头部转动）
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
            event.getFrom().getBlockY() == event.getTo().getBlockY() &&
            event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        // 更新玩家活动时间
        afkService.updateActivity(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (plugin.getConfigManager().isAfkExclusionEnabled()) {
            afkService.updateActivity(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (plugin.getConfigManager().isAfkExclusionEnabled()) {
            afkService.updateActivity(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (plugin.getConfigManager().isAfkExclusionEnabled()) {
            afkService.updateActivity(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && plugin.getConfigManager().isAfkExclusionEnabled()) {
            afkService.updateActivity((Player) event.getDamager());
        }
    }

    /**
     * 检查更新并通知玩家
     */
    private void checkAndNotifyUpdate(Player player) {
        if (!updateChecker.isLatestVersion()) {
            messageService.send(player, "DETECTED_NEW_VERSION");
            messageService.send(player, "NEW_VERSION_WEBSITE");
        }
    }
}
