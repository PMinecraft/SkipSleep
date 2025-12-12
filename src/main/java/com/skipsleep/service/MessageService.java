package com.skipsleep.service;

import com.skipsleep.language.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * 消息服务 - 封装所有消息发送逻辑
 * 负责向玩家、世界或全服广播消息
 */
public class MessageService {

    private final LanguageManager languageManager;

    public MessageService(LanguageManager languageManager) {
        this.languageManager = languageManager;
    }

    /**
     * 发送消息给指定接收者
     *
     * @param sender  消息接收者
     * @param langKey 语言文件中的消息键
     */
    public void send(@NotNull CommandSender sender, String langKey) {
        String message = languageManager.getMessage(langKey);
        sender.sendMessage(message);
    }

    /**
     * 发送带世界上下文的消息给指定接收者
     *
     * @param sender  消息接收者
     * @param langKey 语言文件中的消息键
     * @param world   世界上下文（用于占位符替换）
     */
    public void send(@NotNull CommandSender sender, String langKey, World world) {
        String message = languageManager.getMessage(langKey, world);
        sender.sendMessage(message);
    }

    /**
     * 向全服广播消息
     *
     * @param langKey 语言文件中的消息键
     */
    public void broadcast(String langKey) {
        String message = languageManager.getMessage(langKey);
        Bukkit.broadcastMessage(message);
    }

    /**
     * 向指定世界的所有玩家广播消息
     *
     * @param world   目标世界
     * @param langKey 语言文件中的消息键
     */
    public void broadcastToWorld(World world, String langKey) {
        String message = languageManager.getMessage(langKey, world);
        for (Player player : world.getPlayers()) {
            player.sendMessage(message);
        }
    }

    /**
     * 获取语言管理器（用于需要直接访问消息内容的场景）
     */
    public LanguageManager getLanguageManager() {
        return languageManager;
    }
}
