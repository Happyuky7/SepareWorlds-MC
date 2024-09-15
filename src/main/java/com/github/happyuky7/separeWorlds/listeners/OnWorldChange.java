package com.github.happyuky7.separeWorlds.listeners;

/*
 * Code by: Happyuky7
 * Github: https://github.com/Happyuky7/SepareWorlds-MC
 */

import com.github.happyuky7.separeWorlds.SepareWorlds;
import com.github.happyuky7.separeWorlds.files.DataYaml;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;

public class OnWorldChange implements Listener {


    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String fromWorld = event.getFrom().getName();
        String toWorld = player.getWorld().getName();

        if (SepareWorlds.getInstance().getConfig().contains("worlds.config.groups")) {
            String fromGroup = getWorldGroup(fromWorld);
            String toGroup = getWorldGroup(toWorld);

            if (fromGroup != null && toGroup != null && !fromGroup.equals(toGroup)) {
                savePlayerData(player, fromWorld, fromGroup);
                loadPlayerData(player, toWorld, toGroup);
            }
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

    private void savePlayerData(Player player, String worldName, String group) {
        File file = new File(SepareWorlds.getInstance().getDataFolder() + File.separator + "data" + File.separator + group + File.separator + player.getUniqueId() + ".yml");
        FileConfiguration data = DataYaml.getYamlConfiguration(file);

        int pos = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            data.set("inventory." + pos, item);
            pos++;
        }
        if (SepareWorlds.getInstance().getConfig().getBoolean("worlds.config.groups." + group + ".options.ender-chest", true)) {
            pos = 0;
            for (ItemStack item : player.getEnderChest().getContents()) {
                data.set("ender_chest." + pos, item);
                pos++;
            }
        }

        data.set("armor_contents.helmet", player.getInventory().getHelmet());
        data.set("armor_contents.chestplate", player.getInventory().getChestplate());
        data.set("armor_contents.leggings", player.getInventory().getLeggings());
        data.set("armor_contents.boots", player.getInventory().getBoots());

        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));
        player.getInventory().clear();
        player.getEnderChest().clear();

        if (SepareWorlds.getInstance().getConfig().getBoolean("worlds.config.groups." + group + ".options.gamemode", true)) {
            data.set("gamemode", player.getGameMode().toString());
            player.setGameMode(GameMode.SURVIVAL);
        }
        if (SepareWorlds.getInstance().getConfig().getBoolean("worlds.config.groups." + group + ".options.fly", true)) {
            data.set("flying", player.isFlying());
            player.setFlying(false);
        }

        pos = 0;
        data.set("potion_effect", null);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            data.set("potion_effect." + pos + ".type", effect.getType().getName());
            data.set("potion_effect." + pos + ".level", effect.getAmplifier());
            data.set("potion_effect." + pos + ".duration", effect.getDuration());
            player.removePotionEffect(effect.getType());
            pos++;
        }

        if (SepareWorlds.getInstance().getConfig().getBoolean("worlds.config.groups." + group + ".options.health", true)) {
            data.set("health", player.getHealth());
        }
        data.set("hunger", player.getFoodLevel());
        data.set("exp", player.getExp());
        data.set("exp-level", player.getLevel());

        DataYaml.saveYamlConfiguration(file, data);
    }

    private void loadPlayerData(Player player, String worldName, String group) {
        File file = new File(SepareWorlds.getInstance().getDataFolder() + File.separator + "data" + File.separator + group + File.separator + player.getUniqueId() + ".yml");
        FileConfiguration data = DataYaml.getYamlConfiguration(file);

        if (data.contains("inventory")) {
            for (String key : data.getConfigurationSection("inventory").getKeys(false)) {
                player.getInventory().setItem(Integer.parseInt(key), data.getItemStack("inventory." + key));
            }
        }
        if (SepareWorlds.getInstance().getConfig().getBoolean("worlds.config.groups." + group + ".options.ender-chest", true)) {
            if (data.contains("ender_chest")) {
                for (String key : data.getConfigurationSection("ender_chest").getKeys(false)) {
                    player.getEnderChest().setItem(Integer.parseInt(key), data.getItemStack("ender_chest." + key));
                }
            }
        }
        if (data.contains("potion_effect")) {
            for (String key : data.getConfigurationSection("potion_effect").getKeys(false)) {
                PotionEffectType type = PotionEffectType.getByName(data.getString("potion_effect." + key + ".type"));
                int duration = data.getInt("potion_effect." + key + ".duration");
                int level = data.getInt("potion_effect." + key + ".level");
                PotionEffect effect = new PotionEffect(type, duration, level);
                player.addPotionEffect(effect);
            }
        }
        if (data.contains("armor_contents")) {
            player.getInventory().setHelmet(data.getItemStack("armor_contents.helmet"));
            player.getInventory().setChestplate(data.getItemStack("armor_contents.chestplate"));
            player.getInventory().setLeggings(data.getItemStack("armor_contents.leggings"));
            player.getInventory().setBoots(data.getItemStack("armor_contents.boots"));
        }
        if (SepareWorlds.getInstance().getConfig().getBoolean("worlds.config.groups." + group + ".options.gamemode", true)) {
            if (data.contains("gamemode")) {
                player.setGameMode(GameMode.valueOf(data.getString("gamemode")));
            }
        }
        if (SepareWorlds.getInstance().getConfig().getBoolean("worlds.config.groups." + group + ".options.fly", true)) {
            if (data.contains("flying")) {
                player.setFlying(data.getBoolean("flying"));
            }
        }
        if (SepareWorlds.getInstance().getConfig().getBoolean("worlds.config.groups." + group + ".options.health", true)) {
            if (data.contains("health")) {
                player.setHealth(data.getDouble("health"));
            }
        }
        if (data.contains("hunger")) {
            player.setFoodLevel(data.getInt("hunger"));
        }
        if (data.contains("exp") && !data.contains("exp-level")) {
            int totalExp = data.getInt("exp");
            player.setTotalExperience(totalExp);
            player.setLevel(0);
            player.setExp(0.0F);
            while (totalExp > player.getExpToLevel()) {
                totalExp -= player.getExpToLevel();
                player.setLevel(player.getLevel() + 1);
            }
            float xp = (totalExp / player.getExpToLevel());
            player.setExp(xp);
        } else if (data.contains("exp-level") && data.contains("exp")) {
            player.setExp((float) data.getDouble("exp"));
            player.setLevel(data.getInt("exp-level"));
        } else {
            player.setExp(0.0F);
            player.setLevel(0);
        }
    }
}
