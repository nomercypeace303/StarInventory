package me.mercy.starInventory.Files;

import me.mercy.starInventory.StarInventory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class ConfigHandler {

    private final Plugin plugin = StarInventory.getMain();
    private final String configPath ;
    private YmlHandler config;
    public boolean isLoaded = false;

    public ConfigHandler(String configPath) {
        this.configPath = configPath;
    }


    public void loadconfig() {
        config = new YmlHandler(plugin.getDataFolder().getPath() + configPath);
        isLoaded = true;
    }

    //public void saveconfig() {
    //    config.save();
    //    plugin.getLogger().info("The config " + configPath + " has been saved!");
    //}

    public void reloadconfig() {
        config.reload();
        plugin.getLogger().info("The config " + configPath + " has been reloaded!");
    }

    public ConfigurationSection getSection(String inFilePath) {
        ConfigurationSection section = config.getSection(inFilePath);
        if (section == null) {
            plugin.getLogger().severe("The section " + inFilePath + " does not exist in the file: " + configPath);
        }
        return section;
    }


}
