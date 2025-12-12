package com.skipsleep;

import com.skipsleep.bstats.Metrics;
import com.skipsleep.command.CommandRouter;
import com.skipsleep.config.ConfigManager;
import com.skipsleep.language.LanguageManager;
import com.skipsleep.listener.PlayerListener;
import com.skipsleep.listener.SleepListener;
import com.skipsleep.manager.SleepManager;
import com.skipsleep.service.AfkService;
import com.skipsleep.service.MessageService;
import com.skipsleep.update.UpdateChecker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

/**
 * SkipSleep - 只需部分玩家睡觉即可跳过夜晚
 *
 * @author PlaidMrdeer
 */
public final class SkipSleep extends JavaPlugin {

    private static final int BSTATS_PLUGIN_ID = 16211;

    private static SkipSleep instance;

    // 管理层
    private ConfigManager configManager;
    private LanguageManager languageManager;
    private SleepManager sleepManager;
    
    // 服务层
    private MessageService messageService;
    private AfkService afkService;
    
    // 工具层
    private UpdateChecker updateChecker;

    /**
     * 获取插件实例
     */
    public static SkipSleep getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        // 保存默认配置
        saveDefaultConfig();

        // 初始化管理器
        this.configManager = new ConfigManager(this);
        this.languageManager = new LanguageManager(this);
        
        // 初始化服务层
        this.afkService = new AfkService(configManager);
        this.messageService = new MessageService(languageManager);
        
        // 初始化睡眠管理器（依赖 AfkService）
        this.sleepManager = new SleepManager(this, afkService);
        
        // 初始化更新检查器
        this.updateChecker = new UpdateChecker(this);

        // 加载语言文件
        if (!languageManager.load()) {
            getLogger().severe("Failed to load language file! Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // 显示启动 Logo
        printLogo();

        // 异步检查更新
        if (configManager.isUpdateCheckEnabled()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    checkForUpdates();
                }
            }.runTaskAsynchronously(this);
        }

        // 注册命令
        registerCommands();

        // 注册事件监听器
        registerListeners();

        // 注册 bStats
        new Metrics(this, BSTATS_PLUGIN_ID);

        getLogger().info("SkipSleep has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SkipSleep has been disabled!");
    }

    /**
     * 打印启动 Logo
     */
    private void printLogo() {
        String[] logo = {
            "  ____  _    _      ____  _                 ",
            " / ___|| | _(_)_ __/ ___|| | ___  ___ _ __  ",
            " \\___ \\| |/ / | '_ \\___ \\| |/ _ \\/ _ \\ '_ \\ ",
            "  ___) |   <| | |_) |__) | |  __/  __/ |_) |",
            " |____/|_|\\_\\_| .__/____/|_|\\___|\\___| .__/ ",
            "              |_|                    |_|    "
        };

        for (String line : logo) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + line);
        }
    }

    /**
     * 检查更新
     */
    private void checkForUpdates() {
        messageService.send(Bukkit.getConsoleSender(), "CURRENT_VERSION");
        messageService.send(Bukkit.getConsoleSender(), "INSPECT_NEW_VERSION");

        if (updateChecker.isLatestVersion()) {
            messageService.send(Bukkit.getConsoleSender(), "NO_NEW_VERSION");
        } else {
            messageService.send(Bukkit.getConsoleSender(), "DETECTED_NEW_VERSION");
            messageService.send(Bukkit.getConsoleSender(), "NEW_VERSION_WEBSITE");
        }
    }

    /**
     * 注册命令
     */
    private void registerCommands() {
        CommandRouter commandRouter = new CommandRouter(this, messageService);
        
        Objects.requireNonNull(getCommand("skipsleep"))
                .setExecutor(commandRouter);
        Objects.requireNonNull(getCommand("skipsleep"))
                .setTabCompleter(commandRouter);
    }

    /**
     * 注册事件监听器
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(
                new SleepListener(this, messageService), this);
        getServer().getPluginManager().registerEvents(
                new PlayerListener(this, messageService, afkService, updateChecker), this);
    }

    /**
     * 重新加载插件
     */
    public void reloadPlugin() {
        configManager.reload();
        if (!languageManager.load()) {
            getLogger().warning("Failed to reload language file!");
        }
        sleepManager.reset();
        afkService.reset();
        updateChecker.refresh();
    }

    // ============== Getters ==============

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public SleepManager getSleepManager() {
        return sleepManager;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public AfkService getAfkService() {
        return afkService;
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
}
