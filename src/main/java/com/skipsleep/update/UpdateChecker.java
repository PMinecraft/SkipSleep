package com.skipsleep.update;

import com.skipsleep.SkipSleep;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

/**
 * 更新检查器
 */
public class UpdateChecker {

    private static final String UPDATE_URL = "https://api.spigotmc.org/legacy/update.php?resource=106146";

    private final SkipSleep plugin;
    private String latestVersion;

    public UpdateChecker(SkipSleep plugin) {
        this.plugin = plugin;
        this.latestVersion = null;
    }

    /**
     * 获取最新版本号
     */
    public String getLatestVersion() {
        if (latestVersion == null) {
            fetchLatestVersion();
        }
        return latestVersion;
    }

    /**
     * 从 SpigotMC 获取最新版本
     */
    private void fetchLatestVersion() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new URL(UPDATE_URL).openStream(),
                        StandardCharsets.UTF_8))) {
            latestVersion = reader.readLine();
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to check for updates", e);
            latestVersion = plugin.getDescription().getVersion();
        }
    }

    /**
     * 检查是否是最新版本
     */
    public boolean isLatestVersion() {
        String latest = getLatestVersion();
        String current = plugin.getDescription().getVersion();
        return latest != null && latest.equalsIgnoreCase(current);
    }

    /**
     * 刷新版本缓存
     */
    public void refresh() {
        latestVersion = null;
    }
}
