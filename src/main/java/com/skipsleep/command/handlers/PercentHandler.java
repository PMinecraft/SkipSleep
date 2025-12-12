package com.skipsleep.command.handlers;

import com.skipsleep.SkipSleep;
import com.skipsleep.command.SubCommandHandler;
import com.skipsleep.service.MessageService;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Percent 子命令处理器
 * 处理 /skipsleep percent <百分比>
 */
public class PercentHandler implements SubCommandHandler {

    private final SkipSleep plugin;
    private final MessageService messageService;

    public PercentHandler(SkipSleep plugin, MessageService messageService) {
        this.plugin = plugin;
        this.messageService = messageService;
    }

    @Override
    public String getName() {
        return "percent";
    }

    @Override
    public String getPermission() {
        return "skipsleep.command.percent";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            messageService.send(sender, "COMMAND_ERROR");
            return true;
        }

        try {
            int percent = Integer.parseInt(args[0]);
            if (percent <= 0 || percent > 100) {
                messageService.send(sender, "COMMAND_SET_FAIL");
                return true;
            }
            plugin.getConfigManager().setSkipPercentage(percent);
        } catch (NumberFormatException e) {
            messageService.send(sender, "COMMAND_SET_FAIL");
            return true;
        }

        messageService.send(sender, "COMMAND_SET_SUCCESSFULLY");
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return filterCompletions(Arrays.asList("25", "50", "75"), args[0]);
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
