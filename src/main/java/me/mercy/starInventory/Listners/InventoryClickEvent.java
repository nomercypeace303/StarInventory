package me.mercy.starInventory.Listners;

import me.mercy.starInventory.Handlers.InventoryHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class InventoryClickEvent implements Listener {

    private final Map<UUID, Map<Integer, Long>> lastClick = new HashMap<>();

    @EventHandler
    public void event(org.bukkit.event.inventory.InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getType() != InventoryType.PLAYER) return;

        int slot = event.getSlot();

        if (InventoryHandler.blockedSlots.contains(slot)) {
            event.setCancelled(true);
        }

        if (!InventoryHandler.onInteractionCommand.containsKey(slot)) return;

        long now = System.currentTimeMillis();
        Map<Integer, Long> playerClicks = lastClick.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
        long last = playerClicks.getOrDefault(slot, 0L);

        if (now - last < 120) return; // evita spam multiplo
        playerClicks.put(slot, now);

        Map<Boolean, List<String>> commands = InventoryHandler.onInteractionCommand.get(slot);
        List<String> commandsList = null;

        if (event.getClick().isLeftClick()) {
            commandsList = commands.getOrDefault(false, Collections.emptyList());
        } else if (event.getClick().isRightClick()) {
            commandsList = commands.getOrDefault(true, Collections.emptyList());
        }

        if (commandsList != null && !commandsList.isEmpty()) {
            for (String cmd : commandsList) {
                player.performCommand(cmd.replace("%player%", player.getName()));
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        lastClick.remove(event.getPlayer().getUniqueId()); // cleanup
    }
}
