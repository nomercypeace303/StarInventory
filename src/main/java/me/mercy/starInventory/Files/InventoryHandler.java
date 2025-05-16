package me.mercy.starInventory.Files;

import me.mercy.starInventory.StarInventory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class InventoryHandler {

    private final Plugin plugin = StarInventory.getMain();
    private String inventoryPath= "";
    private YmlHandler inventory;


    public void loadInventory() {
        if (!(plugin.getConfig().getString("PluginInfo.Inventory") == null)) {
            inventoryPath = plugin.getConfig().getString("PluginInfo.Inventory");
            inventory = new YmlHandler(plugin.getDataFolder().getPath() + "/Inventories/" + inventoryPath);
        } else {
            plugin.getLogger().severe("The inventory path is not set in the config file!");
        }
    }

    public void saveInventory() {
        inventory.save();
        plugin.getLogger().info("The inventory " + inventoryPath + " has been saved!");
    }

    public void reloadInventory() {
        inventory.reload();
        plugin.getLogger().info("The inventory " + inventoryPath + " has been reloaded!");
    }

    public ConfigurationSection getSection(String inFilePath) {
        inventory.getSection(inFilePath);
        return null;
    }

}
