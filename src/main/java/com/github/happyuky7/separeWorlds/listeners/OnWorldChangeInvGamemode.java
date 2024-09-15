package com.github.happyuky7.separeWorlds.listeners;

/*
 * Code by: Happyuky7
 * Github: https://github.com/Happyuky7/SepareWorlds-MC
 */

import com.github.happyuky7.separeWorlds.SepareWorlds;
import com.github.happyuky7.separeWorlds.files.DataYaml;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;

public class OnWorldChangeInvGamemode implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String fromWorld = event.getFrom().getName();
        String toWorld = player.getWorld().getName();
        String fromGamemode = event.getPlayer().getGameMode().toString();
        String toGamemode = player.getGameMode().toString();

        if (SepareWorlds.getInstance().getConfig().contains("worlds-inv-gamemode.config.groups")) {
            String fromGroup = getWorldGroup(fromWorld);
            String toGroup = getWorldGroup(toWorld);

            if (fromGroup != null && toGroup != null && !fromGroup.equals(toGroup)) {
                savePlayerData(player, fromWorld, fromGroup, fromGamemode);
                loadPlayerData(player, toWorld, toGroup, toGamemode);
            }
        }
    }

    private String getWorldGroup(String worldName) {
        FileConfiguration config = SepareWorlds.getInstance().getConfig();
        for (String group : config.getConfigurationSection("worlds-inv-gamemode.config.groups").getKeys(false)) {
            if (config.getStringList("worlds-inv-gamemode.config.groups." + group + ".worlds").contains(worldName)) {
                return group;
            }
        }
        return null;
    }

    private void savePlayerData(Player player, String worldName, String group, String playerGamemode) {
        File file = new File(SepareWorlds.getInstance().getDataFolder() + File.separator + "data" + File.separator + group + File.separator + player.getUniqueId() + ".yml");
        FileConfiguration data = DataYaml.getYamlConfiguration(file);

        savePDataGamemode(player, data, playerGamemode, group);

        saveAdditionalData(player, data, playerGamemode);

        DataYaml.saveYamlConfiguration(file, data);
    }

    private void savePDataGamemode(Player player, FileConfiguration data, String playerGamemode, String group) {
        int pos = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            data.set(playerGamemode + ".inventory." + pos, item);
            pos++;
        }

        if (SepareWorlds.getInstance().getConfig().getBoolean("worlds-inv-gamemode.config.groups." + group + ".options.ender-chest", true)) {
            pos = 0;
            for (ItemStack item : player.getEnderChest().getContents()) {
                data.set(playerGamemode + ".ender_chest." + pos, item);
                pos++;
            }
        }

        data.set(playerGamemode + ".armor_contents.helmet", player.getInventory().getHelmet());
        data.set(playerGamemode + ".armor_contents.chestplate", player.getInventory().getChestplate());
        data.set(playerGamemode + ".armor_contents.leggings", player.getInventory().getLeggings());
        data.set(playerGamemode + ".armor_contents.boots", player.getInventory().getBoots());
    }

    private void saveAdditionalData(Player player, FileConfiguration data, String playerGamemode) {
        data.set(playerGamemode + ".health", player.getHealth());
        data.set(playerGamemode + ".hunger", player.getFoodLevel());
        data.set(playerGamemode + ".exp", player.getExp());
        data.set(playerGamemode + ".exp-level", player.getLevel());

        int pos = 0;
        data.set(playerGamemode + ".potion_effect", null);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            data.set(playerGamemode + ".potion_effect." + pos + ".type", effect.getType().getName());
            data.set(playerGamemode + ".potion_effect." + pos + ".level", effect.getAmplifier());
            data.set(playerGamemode + ".potion_effect." + pos + ".duration", effect.getDuration());
            pos++;
        }
    }

    private void loadPlayerData(Player player, String worldName, String group, String playerGamemode) {
        File file = new File(SepareWorlds.getInstance().getDataFolder() + File.separator + "data" + File.separator + group + File.separator + player.getUniqueId() + ".yml");
        FileConfiguration data = DataYaml.getYamlConfiguration(file);

        loadPDataGamemode(player, data, playerGamemode);
        loadAdditionalData(player, data, playerGamemode);
    }

    private void loadPDataGamemode(Player player, FileConfiguration data, String playerGamemode) {
        // Clear the player's current inventory and ender chest
        player.getInventory().clear();
        player.getEnderChest().clear();

        // Load the saved inventory based on the gamemode
        if (data.contains(playerGamemode + ".inventory")) {
            for (String key : data.getConfigurationSection(playerGamemode + ".inventory").getKeys(false)) {
                player.getInventory().setItem(Integer.parseInt(key), data.getItemStack(playerGamemode + ".inventory." + key));
            }
        }

        if (data.contains(playerGamemode + ".ender_chest")) {
            for (String key : data.getConfigurationSection(playerGamemode + ".ender_chest").getKeys(false)) {
                player.getEnderChest().setItem(Integer.parseInt(key), data.getItemStack(playerGamemode + ".ender_chest." + key));
            }
        }

        player.getInventory().setHelmet(data.getItemStack(playerGamemode + ".armor_contents.helmet"));
        player.getInventory().setChestplate(data.getItemStack(playerGamemode + ".armor_contents.chestplate"));
        player.getInventory().setLeggings(data.getItemStack(playerGamemode + ".armor_contents.leggings"));
        player.getInventory().setBoots(data.getItemStack(playerGamemode + ".armor_contents.boots"));
    }

    private void loadAdditionalData(Player player, FileConfiguration data, String playerGamemode) {
        if (data.contains(playerGamemode + ".health")) {
            player.setHealth(data.getDouble(playerGamemode + ".health"));
        }
        if (data.contains(playerGamemode + ".hunger")) {
            player.setFoodLevel(data.getInt(playerGamemode + ".hunger"));
        }
        if (data.contains(playerGamemode + ".exp") && data.contains(playerGamemode + ".exp-level")) {
            int totalExp = data.getInt(playerGamemode + ".exp");
            player.setLevel(data.getInt(playerGamemode + ".exp-level"));
            player.setExp((float) data.getDouble(playerGamemode + ".exp"));
        }

        if (data.contains(playerGamemode + ".potion_effect")) {
            for (String key : data.getConfigurationSection(playerGamemode + ".potion_effect").getKeys(false)) {
                PotionEffectType type = PotionEffectType.getByName(data.getString(playerGamemode + ".potion_effect." + key + ".type"));
                int duration = data.getInt(playerGamemode + ".potion_effect." + key + ".duration");
                int level = data.getInt(playerGamemode + ".potion_effect." + key + ".level");
                PotionEffect effect = new PotionEffect(type, duration, level);
                player.addPotionEffect(effect);
            }
        }
    }
}
