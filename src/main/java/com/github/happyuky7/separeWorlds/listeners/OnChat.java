package com.github.happyuky7.separeWorlds.listeners;

/*
 * Code by: Happyuky7
 * Github: https://github.com/Happyuky7/SepareWorlds-MC
 */

import com.github.happyuky7.separeWorlds.SepareWorlds;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

public class OnChat implements Listener {

    private final Map<String, String> worldChatFormats = new HashMap<>();
    private final Map<String, Boolean> worldChatRangeEnabled = new HashMap<>();
    private final Map<String, Integer> worldChatRanges = new HashMap<>();

    public OnChat() {
        loadChatConfig();
    }

    private void loadChatConfig() {
        FileConfiguration config = SepareWorlds.getInstance().getConfig();

        if (config.getBoolean("worlds-chat.enabled")) {
            for (String group : config.getConfigurationSection("worlds-chat.config.groups").getKeys(false)) {
                String format = config.getString("worlds-chat.config.groups." + group + ".options.format");
                boolean rangeEnabled = config.getBoolean("worlds-chat.config.groups." + group + ".options.range-enabled");
                int range = config.getInt("worlds-chat.config.groups." + group + ".options.range");

                worldChatFormats.put(group, format);
                worldChatRangeEnabled.put(group, rangeEnabled);
                worldChatRanges.put(group, range);
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();
        String group = getWorldGroup(worldName);

        if (group != null && worldChatFormats.containsKey(group)) {
            String format = worldChatFormats.get(group);
            boolean rangeEnabled = worldChatRangeEnabled.get(group);
            int range = worldChatRanges.get(group);

            if (rangeEnabled) {

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.getWorld().equals(player.getWorld()) && onlinePlayer.getLocation().distance(player.getLocation()) <= range) {
                        onlinePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', format
                                .replace("{player}", player.getName())
                                .replace("{message}", event.getMessage())));
                    }
                }
            } else {

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.getWorld().equals(player.getWorld())) {
                        onlinePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', format
                                .replace("{player}", player.getName())
                                .replace("{message}", event.getMessage())));
                    }
                }
            }

            event.setCancelled(true);
        }
    }

    private String getWorldGroup(String worldName) {
        FileConfiguration config = SepareWorlds.getInstance().getConfig();
        for (String group : config.getConfigurationSection("worlds.config.groups").getKeys(false)) {
            if (config.getStringList("worlds.config.groups." + group + ".worlds").contains(worldName)) {
                return group;
            }
        }
        return null;
    }
}
