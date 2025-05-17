package me.mercy.starInventory.Listners;

import me.mercy.starInventory.Files.ConfigHandler;
import me.mercy.starInventory.Files.YmlHandler;
import me.mercy.starInventory.Items.ItemHandler;
import me.mercy.starInventory.StarInventory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.Objects;


public class OnPlayerJoin implements org.bukkit.event.Listener {
    private final Plugin plugin = StarInventory.getMain();
    public static ConfigHandler inventory = StarInventory.getinventoryFile();

    public void setter(Player player){
        ConfigurationSection components = inventory.getSection("Components");
        if (inventory.isLoaded){
            components.getKeys(false).forEach(key -> {
                if ((Objects.requireNonNull(Objects.requireNonNull(components.getConfigurationSection(key)).getString("Type"))).equalsIgnoreCase("Button")){
                    ConfigurationSection itemConfigurationSection = inventory.getSection("Components."+key+".Attributes.Item");
                    ItemHandler itemHandler = new ItemHandler(itemConfigurationSection, plugin);
                    player.getInventory().setItem(inventory.getSection("Components."+key+".Attributes").getInt("Slot"), itemHandler.autoBuildItem());
                }

            });
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String inventoryFilePath = plugin.getDataFolder().getPath() + "/Inventories/" + new YmlHandler(plugin.getDataFolder().getPath() + "/config.yml").getSection("PluginInfo").getString("Inventory");
        plugin.getLogger().info("Loading inventory from: " + inventoryFilePath);

        Player player = event.getPlayer();

        setter(player);
    }


}
