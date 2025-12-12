package com.skipsleep.command.handlers;

import com.skipsleep.SkipSleep;
import com.skipsleep.command.SubCommandHandler;
import com.skipsleep.config.ConfigManager;
import com.skipsleep.config.SkipMode;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Status 子命令处理器
 * 处理 /skipsleep status - 显示当前状态
 */
public class StatusHandler implements SubCommandHandler {

    private final SkipSleep plugin;

    public StatusHandler(SkipSleep plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "status";
    }

    @Override
    public String getPermission() {
        return "skipsleep.command.status";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ConfigManager config = plugin.getConfigManager();

        sender.sendMessage(ChatColor.GREEN + "========== " + ChatColor.YELLOW + "SkipSleep Status" + ChatColor.GREEN + " ==========");
        sender.sendMessage(ChatColor.AQUA + "Version: " + ChatColor.WHITE + plugin.getDescription().getVersion());
        sender.sendMessage(ChatColor.AQUA + "Enabled: " + formatBoolean(config.isSkipEnabled()));
        sender.sendMessage(ChatColor.AQUA + "Mode: " + ChatColor.WHITE + config.getSkipMode().name());

        if (config.getSkipMode() == SkipMode.NUMBER) {
            sender.sendMessage(ChatColor.AQUA + "Required Players: " + ChatColor.WHITE + config.getSkipNumber());
        } else {
            sender.sendMessage(ChatColor.AQUA + "Required Percentage: " + ChatColor.WHITE + config.getSkipPercentage() + "%");
        }

        sender.sendMessage(ChatColor.AQUA + "Sound: " + formatBoolean(config.isSoundEnabled()));
        sender.sendMessage(ChatColor.AQUA + "AFK Exclusion: " + formatBoolean(config.isAfkExclusionEnabled()));
        sender.sendMessage(ChatColor.AQUA + "Update Check: " + formatBoolean(config.isUpdateCheckEnabled()));

        // 显示当前睡眠状态（如果是玩家）
        if (sender instanceof Player) {
            Player player = (Player) sender;
            World world = player.getWorld();
            int sleeping = plugin.getSleepManager().getSleepingPlayerCount(world);
            int eligible = plugin.getSleepManager().getEligiblePlayerCount(world);
            sender.sendMessage(ChatColor.AQUA + "Sleeping in " + world.getName() + ": " +
                    ChatColor.WHITE + sleeping + "/" + eligible);
        }

        sender.sendMessage(ChatColor.GREEN + "==========================================");
        return true;
    }

    private String formatBoolean(boolean value) {
        return value ? ChatColor.GREEN + "ON" : ChatColor.RED + "OFF";
    }
}
