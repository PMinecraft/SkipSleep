package com.skipsleep.command.handlers;

import com.skipsleep.command.SubCommandHandler;
import com.skipsleep.service.MessageService;
import org.bukkit.command.CommandSender;

/**
 * Help 子命令处理器
 * 处理 /skipsleep help
 */
public class HelpHandler implements SubCommandHandler {

    private final MessageService messageService;

    public HelpHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getPermission() {
        return "skipsleep.command.help";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        messageService.send(sender, "HELP_HEAD");
        messageService.send(sender, "HELP_COMMAND_ENABLE");
        messageService.send(sender, "HELP_COMMAND_NUMBER");
        messageService.send(sender, "HELP_COMMAND_MODEL");
        messageService.send(sender, "HELP_COMMAND_PET");
        messageService.send(sender, "HELP_COMMAND_SOUND");
        messageService.send(sender, "HELP_COMMAND_STATUS");
        messageService.send(sender, "HELP_COMMAND_UPDATE");
        messageService.send(sender, "HELP_COMMAND_RELOAD");
        messageService.send(sender, "HELP_COMMAND_HELP");
        return true;
    }
}
