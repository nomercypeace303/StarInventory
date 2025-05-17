package me.mercy.starInventory;

import me.mercy.starInventory.Files.InventoryHandler;
import me.mercy.starInventory.Files.YmlHandler;
import me.mercy.starInventory.Listners.OnPlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class StarInventory extends JavaPlugin {

    private static StarInventory main;
    private static InventoryHandler inventoryHandler;

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

        inventoryHandler = new InventoryHandler();
        inventoryHandler.loadInventory();

        Bukkit.getPluginManager().registerEvents(new OnPlayerJoin(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static StarInventory getMain() {
        return main;
    }

    public static InventoryHandler getInventoryHandler() {
        return inventoryHandler;
    }
}
