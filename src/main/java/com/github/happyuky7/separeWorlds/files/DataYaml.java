package com.github.happyuky7.separeWorlds.files;

/*
 * Code by: Happyuky7
 * Github: https://github.com/Happyuky7/SepareWorlds-MC
 */

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class DataYaml {

    public static FileConfiguration getYamlConfiguration(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void saveYamlConfiguration(File file, FileConfiguration config) {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
