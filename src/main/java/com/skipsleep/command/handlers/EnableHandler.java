package com.skipsleep.command.handlers;

import com.skipsleep.SkipSleep;
import com.skipsleep.command.SubCommandHandler;
import com.skipsleep.service.MessageService;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Enable 子命令处理器
 * 处理 /skipsleep enable <on|off>
 */
public class EnableHandler implements SubCommandHandler {

    private final SkipSleep plugin;
    private final MessageService messageService;

    public EnableHandler(SkipSleep plugin, MessageService messageService) {
        this.plugin = plugin;
        this.messageService = messageService;
    }

    @Override
    public String getName() {
        return "enable";
    }

    @Override
    public String getPermission() {
        return "skipsleep.command.enable";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            messageService.send(sender, "COMMAND_ERROR");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "on":
            case "true":
                plugin.getConfigManager().setSkipEnabled(true);
                break;
            case "off":
            case "false":
                plugin.getConfigManager().setSkipEnabled(false);
                break;
            default:
                messageService.send(sender, "COMMAND_SET_FAIL");
                return true;
        }

        messageService.send(sender, "COMMAND_SET_SUCCESSFULLY");
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return filterCompletions(Arrays.asList("on", "off"), args[0]);
        }
        return SubCommandHandler.super.tabComplete(sender, args);
    }

    private List<String> filterCompletions(List<String> options, String input) {
        String lowerInput = input.toLowerCase();
        return options.stream()
                .filter(s -> s.toLowerCase().startsWith(lowerInput))
                .collect(Collectors.toList());
    }
}
