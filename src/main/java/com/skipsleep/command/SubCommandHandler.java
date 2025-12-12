package com.skipsleep.command;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * 子命令处理器接口
 * 定义统一的执行和 Tab 补全契约
 */
public interface SubCommandHandler {

    /**
     * 获取子命令名称
     *
     * @return 子命令名称（小写）
     */
    String getName();

    /**
     * 获取子命令权限
     *
     * @return 权限节点
     */
    String getPermission();

    /**
     * 执行子命令
     *
     * @param sender 命令发送者
     * @param args   子命令参数（不包含子命令名称本身）
     * @return 是否执行成功
     */
    boolean execute(CommandSender sender, String[] args);

    /**
     * 提供 Tab 补全建议
     *
     * @param sender 命令发送者
     * @param args   当前输入的参数
     * @return 补全建议列表
     */
    default List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
