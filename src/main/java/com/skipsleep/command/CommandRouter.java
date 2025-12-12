package com.skipsleep.command;

import com.skipsleep.SkipSleep;
import com.skipsleep.command.handlers.*;
import com.skipsleep.service.MessageService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 命令路由器
 * 负责路由和权限检查，委托给具体的子命令处理器
 */
public class CommandRouter implements CommandExecutor, TabCompleter {

    private final MessageService messageService;
    private final Map<String, SubCommandHandler> handlers;
    private final SubCommandHandler helpHandler;

    public CommandRouter(SkipSleep plugin, MessageService messageService) {
        this.messageService = messageService;
        this.handlers = new LinkedHashMap<>();
        
        // 注册所有子命令处理器
        registerHandler(new EnableHandler(plugin, messageService));
        registerHandler(new NumberHandler(plugin, messageService));
        registerHandler(new ModeHandler(plugin, messageService));
        registerHandler(new PercentHandler(plugin, messageService));
        registerHandler(new SoundHandler(plugin, messageService));
        registerHandler(new StatusHandler(plugin));
        registerHandler(new ReloadHandler(plugin, messageService));
        registerHandler(new UpdateHandler(plugin, messageService));
        
        // Help 处理器作为默认处理器
        this.helpHandler = new HelpHandler(messageService);
        registerHandler(helpHandler);
    }

    /**
     * 注册子命令处理器
     */
    private void registerHandler(SubCommandHandler handler) {
        handlers.put(handler.getName().toLowerCase(), handler);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        // 无参数时显示帮助
        if (args.length == 0) {
            return helpHandler.execute(sender, new String[0]);
        }

        // 查找子命令处理器
        String subCommandName = args[0].toLowerCase();
        SubCommandHandler handler = handlers.get(subCommandName);

        if (handler == null) {
            messageService.send(sender, "COMMAND_ERROR");
            return true;
        }

        // 权限检查
        if (!sender.hasPermission(handler.getPermission())) {
            messageService.send(sender, "COMMAND_NO_PERMISSION");
            return true;
        }

        // 提取子命令参数并执行
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        return handler.execute(sender, subArgs);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                 @NotNull Command command,
                                                 @NotNull String alias,
                                                 @NotNull String[] args) {

        if (args.length == 1) {
            // 第一个参数：子命令名称
            return handlers.values().stream()
                    .filter(h -> sender.hasPermission(h.getPermission()))
                    .map(SubCommandHandler::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length >= 2) {
            // 后续参数：委托给具体的处理器
            String subCommandName = args[0].toLowerCase();
            SubCommandHandler handler = handlers.get(subCommandName);

            if (handler != null && sender.hasPermission(handler.getPermission())) {
                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                return handler.tabComplete(sender, subArgs);
            }
        }

        return Collections.emptyList();
    }
}
