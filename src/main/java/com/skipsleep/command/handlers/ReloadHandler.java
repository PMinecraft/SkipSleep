package com.skipsleep.command.handlers;

import com.skipsleep.SkipSleep;
import com.skipsleep.command.SubCommandHandler;
import com.skipsleep.service.MessageService;
import org.bukkit.command.CommandSender;

/**
 * Reload 子命令处理器
 * 处理 /skipsleep reload
 */
public class ReloadHandler implements SubCommandHandler {

    private final SkipSleep plugin;
    private final MessageService messageService;

    public ReloadHandler(SkipSleep plugin, MessageService messageService) {
        this.plugin = plugin;
        this.messageService = messageService;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return "skipsleep.command.reload";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        plugin.reloadPlugin();
        messageService.send(sender, "COMMAND_RELOAD");
        return true;
    }
}
