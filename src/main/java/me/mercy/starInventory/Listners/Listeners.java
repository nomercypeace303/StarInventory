package me.mercy.starInventory.Listners;

import me.mercy.starInventory.Files.YmlHandler;
import me.mercy.starInventory.Handlers.InventoryHandler;
import me.mercy.starInventory.StarInventory;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;


public class Listeners implements org.bukkit.event.Listener {
    private final Plugin plugin = StarInventory.getMain();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String inventoryFilePath = plugin.getDataFolder().getPath() + "/Inventories/" + new YmlHandler(plugin.getDataFolder().getPath() + "/config.yml").getSection("PluginInfo").getString("Inventory");
        plugin.getLogger().info("Loading inventory from: " + inventoryFilePath);

        Player player = event.getPlayer();
        StarInventory.getInventoryHandler().setInventory(player);
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        StarInventory.getInventoryHandler().setInventory(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(item ->
                InventoryHandler.inventoryItems.stream().anyMatch(i -> i.isSimilar(item))
        );
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {
        if (event.getSlot() < 0) return;

        Player player = (Player)event.getWhoClicked();
        ItemStack item = player.getOpenInventory().getBottomInventory().getItem(event.getSlot());


        if (item == null) return;
        if (InventoryHandler.blockedSlots.isEmpty()) return;
        for (Integer slot : InventoryHandler.blockedSlots){
            if (slot == event.getRawSlot()){
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode().toString().equalsIgnoreCase("CREATIVE") || player.getGameMode().toString().equalsIgnoreCase("SPECTATOR")){
            StarInventory.getInventoryHandler().setInventory(player);
        }
        if (player.getGameMode().toString().equalsIgnoreCase("SURVIVAL")){
            for (ItemStack item : InventoryHandler.inventoryItems) {
                player.getInventory().remove(item);
            }
            InventoryHandler.blockedSlots.clear();
        }

    }


}
