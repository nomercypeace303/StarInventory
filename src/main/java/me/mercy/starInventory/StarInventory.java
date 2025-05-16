package me.mercy.starInventory;

import me.mercy.starInventory.Files.YmlHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class StarInventory extends JavaPlugin {

    private static StarInventory main;

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

        if (getConfig().getKeys(true).isEmpty()){
            getConfig().options().copyDefaults(true);
            saveConfig();
        }

        if (getConfig().getBoolean("PluginInfo.FirstLoad")) {
            firstLoad();
            getConfig().set("PluginInfo.FirstLoad", false);
            saveConfig();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static StarInventory getMain() {return main;}
}
