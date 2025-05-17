package me.mercy.starInventory;

import me.mercy.starInventory.Commands.Commands;
import me.mercy.starInventory.Files.ConfigHandler;
import me.mercy.starInventory.Files.YmlHandler;
import me.mercy.starInventory.Listners.OnPlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class StarInventory extends JavaPlugin {

    private static StarInventory main;
    public static ConfigHandler inventoryFile;
    public static ConfigHandler languageFile;

    private void firstLoad() {
        /*
         *   firstLoad logic, loads all the default files and directories
         * */

        YmlHandler defaultInventory = new YmlHandler(main.getDataFolder().getPath() + "/Inventories/inventory.yml");
        defaultInventory.copyDefaults("Inventories/inventory.yml");
        YmlHandler defaultLanguage = new YmlHandler(main.getDataFolder().getPath() + "/Languages/EN_en.yml");
        defaultInventory.copyDefaults("Languages/EN_en.yml");

    }


    @Override
    public void onEnable() {
        // Plugin startup logic
        main = this;

        /*
         *
         * Configuration logic, when the configuration doesn't exist, or it's blank,
         * it creates the default configuration file
         *
         * */

        if (getConfig().getKeys(true).isEmpty()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }

        if (getConfig().getBoolean("PluginInfo.FirstLoad")) {
            getLogger().info("First load of the plugin, loading default files...");
            firstLoad();
            getConfig().set("PluginInfo.FirstLoad", false);
            saveConfig();
            reloadConfig();
        }

        inventoryFile = new ConfigHandler("/Inventories/" + getConfig().getString("PluginInfo.Inventory"));
        inventoryFile.loadconfig();
        languageFile = new ConfigHandler("/Languages/" + getConfig().getString("PluginInfo.Language"));
        languageFile.loadconfig();

        Bukkit.getPluginManager().registerEvents(new OnPlayerJoin(), this);
        Objects.requireNonNull(getCommand("starinventory")).setExecutor(new Commands());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static StarInventory getMain() {
        return main;
    }

    public static ConfigHandler getinventoryFile() {
        return inventoryFile;
    }

    public static ConfigHandler getLanguageFile() {
        return languageFile;
    }
}
