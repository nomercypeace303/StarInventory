package me.mercy.starInventory.Listners;

import me.mercy.starInventory.Files.ConfigHandler;
import me.mercy.starInventory.Files.YmlHandler;
import me.mercy.starInventory.Items.ItemHandler;
import me.mercy.starInventory.StarInventory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class OnPlayerJoin implements org.bukkit.event.Listener {
    private final Plugin plugin = StarInventory.getMain();
    public static ConfigHandler inventory = StarInventory.getinventoryFile();
    public static List<Integer> blockedSlots = new ArrayList<>();
    public static List<ItemStack> inventoryItems = new ArrayList<>();

    public void setter(Player player){
        ConfigurationSection components = inventory.getSection("Components");
        if (inventory.isLoaded){
            components.getKeys(false).forEach(key -> {
                if ((Objects.requireNonNull(Objects.requireNonNull(components.getConfigurationSection(key)).getString("Type"))).equalsIgnoreCase("Button")){
                    ConfigurationSection itemConfigurationSection = inventory.getSection("Components."+key+".Attributes.Item");
                    ItemHandler itemHandler = new ItemHandler(itemConfigurationSection, plugin);
                    int slot = inventory.getSection("Components."+key+".Attributes").getInt("Slot");
                    player.getInventory().setItem(slot, itemHandler.autoBuildItem());
                    inventoryItems.add(itemHandler.autoBuildItem());
                    if(inventory.getSection("Components."+key+".Attributes").getBoolean("Locked", true)){
                        blockedSlots.add(slot);
                    };
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
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        setter(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(item ->
                inventoryItems.stream().anyMatch(i -> i.isSimilar(item))
        );
    }



    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        ItemStack item = player.getInventory().getItem(event.getSlot());

        if (item == null) return;
        if (blockedSlots.isEmpty()) return;
        for (Integer slot : blockedSlots){
            if (slot == event.getSlot()){
                event.setCancelled(true);
                return;
            }
        }
    }


}
