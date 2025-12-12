package com.skipsleep.command.handlers;

import com.skipsleep.SkipSleep;
import com.skipsleep.command.SubCommandHandler;
import com.skipsleep.config.SkipMode;
import com.skipsleep.service.MessageService;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mode 子命令处理器
 * 处理 /skipsleep mode <num|pet>
 */
public class ModeHandler implements SubCommandHandler {

    private final SkipSleep plugin;
    private final MessageService messageService;

    public ModeHandler(SkipSleep plugin, MessageService messageService) {
        this.plugin = plugin;
        this.messageService = messageService;
    }

    @Override
    public String getName() {
        return "mode";
    }

    @Override
    public String getPermission() {
        return "skipsleep.command.mode";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            messageService.send(sender, "COMMAND_ERROR");
            return true;
        }

        String modeArg = args[0].toLowerCase();
        if (!modeArg.equals("num") && !modeArg.equals("pet")) {
            messageService.send(sender, "COMMAND_SET_FAIL");
            return true;
        }

        SkipMode mode = SkipMode.fromConfig(modeArg);
        plugin.getConfigManager().setSkipMode(mode);
        messageService.send(sender, "COMMAND_SET_SUCCESSFULLY");
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return filterCompletions(Arrays.asList("num", "pet"), args[0]);
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
