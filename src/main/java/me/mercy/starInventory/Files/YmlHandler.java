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
        String path = plugin.getDataFolder().getPath() + "/" + filePath;
        this.file = new File(path);

        try {
            // Crea file e cartelle se non esistono
            if (!this.file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (IOException e) {
            plugin.getLogger().severe("An error occured upon creating: " + path);
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
        InputStream defaultConfigStream = plugin.getResource(jarpath);
        if (defaultConfigStream == null) {
            plugin.getLogger().warning("Default resource not found: " + file.getName() + ", path: " + jarpath);
            return;
        }

        // Carica quel file come YamlConfiguration
        FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfigStream));

        // Imposta i default nel file effettivo
        fileConfiguration.setDefaults(defaultConfig);
        fileConfiguration.options().copyDefaults(true);

        // Salva i cambiamenti nel file fisico
        save();
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
