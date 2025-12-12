package com.skipsleep.command.handlers;

import com.skipsleep.SkipSleep;
import com.skipsleep.command.SubCommandHandler;
import com.skipsleep.service.MessageService;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Number 子命令处理器
 * 处理 /skipsleep number <数量>
 */
public class NumberHandler implements SubCommandHandler {

    private final SkipSleep plugin;
    private final MessageService messageService;

    public NumberHandler(SkipSleep plugin, MessageService messageService) {
        this.plugin = plugin;
        this.messageService = messageService;
    }

    @Override
    public String getName() {
        return "number";
    }

    @Override
    public String getPermission() {
        return "skipsleep.command.number";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            messageService.send(sender, "COMMAND_ERROR");
            return true;
        }

        try {
            int number = Integer.parseInt(args[0]);
            if (number <= 0) {
                messageService.send(sender, "COMMAND_SET_FAIL");
                return true;
            }
            plugin.getConfigManager().setSkipNumber(number);
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
            return filterCompletions(Arrays.asList("1", "2", "3", "5"), args[0]);
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
