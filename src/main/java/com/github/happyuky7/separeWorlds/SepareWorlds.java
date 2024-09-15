package com.github.happyuky7.separeWorlds;

/*
 * Code by: Happyuky7
 * Github: https://github.com/Happyuky7/SepareWorlds-MC
 */

import com.github.happyuky7.separeWorlds.commands.SepareWorldsCMD;
import com.github.happyuky7.separeWorlds.files.FileManager;
import com.github.happyuky7.separeWorlds.listeners.OnChat;
import com.github.happyuky7.separeWorlds.listeners.OnWorldChange;
import com.github.happyuky7.separeWorlds.listeners.OnWorldChangeInvGamemode;
import com.github.happyuky7.separeWorlds.utils.MessageColors;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SepareWorlds extends JavaPlugin {

    public static SepareWorlds instance;

    public static SepareWorlds getInstance() {
        return instance;
    }

    private FileManager config;

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;

        config = new FileManager(this, "config");

        if (!getConfig().getString("config-version").equals("Alpha-1.0.0")) {
            getLogger().warning("The configuration file is outdated, please delete it and restart the server to generate a new one.");
            getServer().getPluginManager().disablePlugin(this);
        }

        if (getConfig().getBoolean("worlds.config.enabled")) {
            getServer().getPluginManager().registerEvents(new OnWorldChange(), this);
        } else if (getConfig().getBoolean("worlds-inv-gamemode.config.enabled")) {
            getServer().getPluginManager().registerEvents(new OnWorldChangeInvGamemode(), this);
        } else {
            getLogger().warning("No listener world inventory enabled");
            getLogger().warning("No listener world gamemode enabled");
            getLogger().warning("Message: ");
            getLogger().warning(
                    "This message is not an error as such only indicates that they are not, " +
                            "active if it is the first time you use the plugin all options are disabled by default to avoid problems, " +
                            "if you still do not perform a backup of the server do it before making any changes. Only for security."
            );
        }

        if (getConfig().getBoolean("worlds-chat.enabled")) {
            getServer().getPluginManager().registerEvents(new OnChat(), this);
        } else {
            getLogger().warning("No listener world chat enabled");
            getLogger().warning("Message: ");
            getLogger().warning(
                    "This message is not an error as such only indicates that they are not, " +
                            "active if it is the first time you use the plugin all options are disabled by default to avoid problems, " +
                            "if you still do not perform a backup of the server do it before making any changes. Only for security."
            );
        }

        getCommand("sws").setExecutor(new SepareWorldsCMD());

        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&r "));
        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&a Enabled SepareWorlds"));
        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&r "));
        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&a SepareWorlds &7- &fby &aHappyuky7"));
        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&r "));
        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&a Version: &fAlpha-1.0.0"));
        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&c This plugin is in development"));
        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&r "));
        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&a GitHub: &fhttps://github.com/Happyuky7/SepareWorlds-MC"));
        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&r "));



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&r "));
        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&c Disabled SepareWorlds"));
        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&r "));
        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&a SepareWorlds &7- &fby &aHappyuky7"));
        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&r "));
        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&a Version: &fAlpha-1.0.0"));
        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&c This plugin is in development"));
        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&r "));
        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&a GitHub: &fhttps://github.com/Happyuky7/SepareWorlds-MC"));
        Bukkit.getConsoleSender().sendMessage(MessageColors.getMsgColor("&r "));

    }

    public FileManager getConfig() {
        return config;
    }
}
