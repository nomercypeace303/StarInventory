package me.mercy.starInventory.Handlers;

import me.mercy.starInventory.Files.ConfigHandler;
import me.mercy.starInventory.Items.ItemHandler;
import me.mercy.starInventory.StarInventory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InventoryHandler {

    private final Plugin plugin = StarInventory.getMain();
    public static List<Integer> blockedSlots = new ArrayList<>();
    public static List<ItemStack> inventoryItems = new ArrayList<>();

    public void setInventory(Player player){
        ConfigurationSection components = StarInventory.getinventoryFile().getSection("Components");
        if (StarInventory.getinventoryFile().isLoaded){
            components.getKeys(false).forEach(key -> {
                if ((Objects.requireNonNull(Objects.requireNonNull(components.getConfigurationSection(key)).getString("Type"))).equalsIgnoreCase("Button")){
                    ConfigurationSection itemConfigurationSection = StarInventory.getinventoryFile().getSection("Components."+key+".Attributes.Item");
                    ItemHandler itemHandler = new ItemHandler(itemConfigurationSection, plugin);
                    int slot = StarInventory.getinventoryFile().getSection("Components."+key+".Attributes").getInt("Slot");
                    player.getInventory().setItem(slot, itemHandler.autoBuildItem());
                    inventoryItems.add(itemHandler.autoBuildItem());
                    if(StarInventory.getinventoryFile().getSection("Components."+key+".Attributes").getBoolean("Locked", true)){
                        blockedSlots.add(slot);
                    };
                }
            });
        }
    }

    public void reloadInventory(){
        blockedSlots.clear();
        inventoryItems.clear();
        StarInventory.inventoryFile.reloadconfig();
    }
}
