package com.skipsleep.language;

import com.skipsleep.SkipSleep;
import com.skipsleep.manager.SleepManager;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * 语言管理器
 */
public class LanguageManager {

    private static final List<String> DEFAULT_LANGUAGES = Arrays.asList(
            "english.yml", "chinese.yml", "german.yml"
    );

    private final SkipSleep plugin;
    private FileConfiguration langConfig;

    public LanguageManager(SkipSleep plugin) {
        this.plugin = plugin;
    }

    /**
     * 加载语言文件
     * 
     * @return 是否加载成功
     */
    public boolean load() {
        // 确保默认语言文件存在
        saveDefaultLanguages();

        // 获取配置的语言
        String languageName = plugin.getConfigManager().getLanguage();
        File langFile = new File(plugin.getDataFolder(), "lang/" + languageName + ".yml");

        if (!langFile.exists()) {
            plugin.getLogger().warning("Unknown language file: " + languageName + ".yml");
            return false;
        }

        langConfig = YamlConfiguration.loadConfiguration(langFile);
        return true;
    }

    /**
     * 保存默认语言文件
     */
    private void saveDefaultLanguages() {
        for (String lang : DEFAULT_LANGUAGES) {
            File file = new File(plugin.getDataFolder(), "lang/" + lang);
            if (!file.exists()) {
                plugin.saveResource("lang/" + lang, false);
            }
        }
    }

    /**
     * 获取消息并应用占位符（使用默认世界）
     */
    public String getMessage(String key) {
        return getMessage(key, null);
    }

    /**
     * 获取消息并应用占位符
     */
    public String getMessage(String key, World world) {
        String message = langConfig.getString(key);
        if (message == null) {
            return ChatColor.RED + "Missing language key: " + key;
        }
        return applyPlaceholders(message, world);
    }

    /**
     * 应用占位符替换
     */
    private String applyPlaceholders(String message, World world) {
        String result = message;

        // 前缀
        String prefix = langConfig.getString("prefix", "&b[&aSkipSleep&b]");
        result = result.replace("{prefix}", prefix);

        // 颜色代码
        result = ChatColor.translateAlternateColorCodes('&', result);

        // 版本信息
        result = result.replace("{version}", plugin.getDescription().getVersion());
        
        String latestVersion = plugin.getUpdateChecker() != null 
                ? plugin.getUpdateChecker().getLatestVersion() : null;
        result = result.replace("{new_version}", latestVersion != null ? latestVersion : "unknown");

        // 配置值
        result = result.replace("{totalNum}", 
                String.valueOf(plugin.getConfigManager().getSkipNumber()));
        result = result.replace("{totalPet}", 
                String.valueOf(plugin.getConfigManager().getSkipPercentage()));

        // 睡眠状态（世界特定或全局）
        SleepManager sleepManager = plugin.getSleepManager();
        if (sleepManager != null) {
            if (world != null) {
                result = result.replace("{num}", String.valueOf(sleepManager.getSleepingPlayerCount(world)));
                result = result.replace("{pet}", String.valueOf(sleepManager.getCurrentPercentage(world)));
                result = result.replace("{eligible}", String.valueOf(sleepManager.getEligiblePlayerCount(world)));
            } else {
                result = result.replace("{num}", String.valueOf(sleepManager.getSleepingPlayerCount()));
                result = result.replace("{pet}", String.valueOf(sleepManager.getCurrentPercentage()));
            }
        }

        return result;
    }
}
