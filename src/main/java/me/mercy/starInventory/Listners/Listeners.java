package me.mercy.starInventory.Listners;

import me.mercy.starInventory.Files.YmlHandler;
import me.mercy.starInventory.Handlers.InventoryHandler;
import me.mercy.starInventory.StarInventory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class Listeners implements Listener {
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

/*
    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {
        ClickType clickType = event.getClick();
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getType() != InventoryType.PLAYER) return;

        int slot = event.getSlot();

        // blocco slot
        if (InventoryHandler.blockedSlots.contains(slot)) {
            event.setCancelled(true);
        }

        // comandi sugli slot
        if (InventoryHandler.onInteractionCommand.containsKey(slot)) {
            Map<Boolean, List<String>> commands = InventoryHandler.onInteractionCommand.get(slot);

            if (!commands.isEmpty() && event.getWhoClicked() instanceof Player player) {
                List<String> commandsList = null;

                if (clickType.isLeftClick()) {
                    commandsList = commands.getOrDefault(false, Collections.emptyList());
                } else if (clickType.isRightClick()) {
                    commandsList = commands.getOrDefault(true, Collections.emptyList());
                }

                if (commandsList != null && !commandsList.isEmpty()) {
                    for (String cmd : commandsList) {
                        player.performCommand(cmd.replace("%player%", player.getName()));
                    }
                }
            }
        }
    }
*/

    @EventHandler
    public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode().toString().equalsIgnoreCase("CREATIVE") || player.getGameMode().toString().equalsIgnoreCase("SPECTATOR")) {
            StarInventory.getInventoryHandler().setInventory(player);
        }
        if (player.getGameMode().toString().equalsIgnoreCase("SURVIVAL") || player.getGameMode().toString().equalsIgnoreCase("ADVENTURE")) {
            for (ItemStack item : InventoryHandler.inventoryItems) {
                player.getInventory().remove(item);
            }
            InventoryHandler.blockedSlots.clear();
        }

    }


}
