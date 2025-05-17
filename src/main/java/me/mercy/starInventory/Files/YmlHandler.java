package me.mercy.starInventory.Files;

import me.mercy.starInventory.StarInventory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class YmlHandler {

    private final Plugin plugin = StarInventory.getMain();

    private File file;
    private FileConfiguration fileConfiguration;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public YmlHandler(String filePath) {
        this.file = new File(filePath);

        try {
            // Crea file e cartelle se non esistono
            if (!this.file.exists()) {
                plugin.getLogger().info("Creating file: " + filePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (IOException e) {
            plugin.getLogger().severe("An error occured upon creating: " + filePath);
            plugin.getLogger().severe(e.toString());
        }

        // Carica il file in memoria
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("An error occured upon saving: " + file.getPath());
            plugin.getLogger().severe(e.toString());
        }
    }

    public void reload() {
        String path = file.getPath();
        file = new File(path);
        if (file.exists()) {
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
        } else {
            plugin.getLogger().severe("An error occured upon reloading: " + path);
        }
    }

    public void copyDefaults(String jarpath) {
        // Carica la versione "di default" dal jar (resources)
        plugin.getLogger().info("Copying default config from jar: " + jarpath);
        plugin.saveResource(jarpath, true);
    }

    public ConfigurationSection getSection(String inFilePath) {
        if (fileConfiguration.contains(inFilePath)) {
            return fileConfiguration.getConfigurationSection(inFilePath);
        } else {
            plugin.getLogger().severe("The section " + inFilePath + " does not exist in the file: " + file.getPath());
        }
        return null;
    }

}
