package com.github.happyuky7.separeWorlds.commands;

/*
    * Code by: Happyuky7
    * Github: https://github.com/Happyuky7/SepareWorlds-MC
 */

import com.github.happyuky7.separeWorlds.SepareWorlds;
import com.github.happyuky7.separeWorlds.utils.MessageColors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SepareWorldsCMD implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(MessageColors.getMsgColor("&r "));
            sender.sendMessage(MessageColors.getMsgColor("&a SepareWorlds &7- &fby &aHappyuky7"));
            sender.sendMessage(MessageColors.getMsgColor("&r "));
            sender.sendMessage(MessageColors.getMsgColor("&a Version: &fAlpha-1.0.0"));
            sender.sendMessage(MessageColors.getMsgColor("&c This plugin is in development"));
            sender.sendMessage(MessageColors.getMsgColor("&r "));
            sender.sendMessage(MessageColors.getMsgColor("&a GitHub: &fhhttps://github.com/Happyuky7/SepareWorlds-MC"));
            sender.sendMessage(MessageColors.getMsgColor("&r "));
            sender.sendMessage(MessageColors.getMsgColor("&a /sws help &7- &fShow this message"));
            sender.sendMessage(MessageColors.getMsgColor("&a /sws reload &7- &fReload the plugin"));
            sender.sendMessage(MessageColors.getMsgColor("&r "));
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("separeworlds.reload")) {
                    sender.sendMessage(MessageColors.getMsgColor("&c You do not have permission to use this command"));
                    return true;
                }

                sender.sendMessage(MessageColors.getMsgColor("&a SepareWorlds &7- &fby &aHappyuky7"));
                sender.sendMessage(MessageColors.getMsgColor("&r "));
                sender.sendMessage(MessageColors.getMsgColor("&a Reloading plugin..."));
                sender.sendMessage(MessageColors.getMsgColor("&r "));

                SepareWorlds.getInstance().getConfig().save();
                SepareWorlds.getInstance().getConfig().reload();

                sender.sendMessage(MessageColors.getMsgColor("&r "));
                sender.sendMessage(MessageColors.getMsgColor("&a Plugin reloaded!"));
                sender.sendMessage(MessageColors.getMsgColor("&r "));
            }
        }

        return true;
    }
}
