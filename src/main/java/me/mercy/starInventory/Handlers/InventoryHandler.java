package me.mercy.starInventory.Handlers;

import me.mercy.starInventory.StarInventory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class InventoryHandler {

    private final Plugin plugin = StarInventory.getMain();
    public static List<Integer> blockedSlots = new ArrayList<>();
    public static List<ItemStack> inventoryItems = new ArrayList<>();
    // Map<Slot, Right or Left click, Commands>
    public static Map<Integer, Map<Boolean, List<String>>> onInteractionCommand = new HashMap<>();

    public void setInventory(Player player) {
        ConfigurationSection components = StarInventory.getinventoryFile().getSection("Components");
        if (StarInventory.getinventoryFile().isLoaded) {
            components.getKeys(false).forEach(key -> {
                if ((Objects.requireNonNull(Objects.requireNonNull(components.getConfigurationSection(key)).getString("Type"))).equalsIgnoreCase("Button")) {

                    ConfigurationSection itemConfigurationSection = StarInventory.getinventoryFile().getSection("Components." + key + ".Attributes.Item");
                    ItemHandler itemHandler = new ItemHandler(itemConfigurationSection, plugin);
                    int slot = StarInventory.getinventoryFile().getSection("Components." + key + ".Attributes").getInt("Slot");
                    player.getInventory().setItem(slot, itemHandler.autoBuildItem());
                    inventoryItems.add(itemHandler.autoBuildItem());

                    if (StarInventory.getinventoryFile().getSection("Components." + key + ".Attributes").getBoolean("Locked", true)) {
                        blockedSlots.add(slot);
                    }

                    if (components.contains(key + ".Attributes.OnInteraction.Right") || components.contains(key + ".Attributes.OnInteraction.Left")) {
                        ConfigurationSection rightInteraction = components.getConfigurationSection(key + ".Attributes.OnInteraction.Right");
                        ConfigurationSection leftInteraction = components.getConfigurationSection(key + ".Attributes.OnInteraction.Left");

                        // RIGHT click
                        if (rightInteraction != null && rightInteraction.contains("Commands")) {
                            List<String> commands = rightInteraction.getStringList("Commands");
                            if (commands.isEmpty()) {
                                plugin.getLogger().severe("Configuration error in the file: "
                                        + StarInventory.inventoryFile.getFileLocation() + " component number: " + key);
                            } else {
                                plugin.getLogger().info("Loading item n." + key + "'s commands (Right)");
                                onInteractionCommand
                                        .computeIfAbsent(slot, s -> new HashMap<>())
                                        .merge(true, new ArrayList<>(commands), (oldList, newList) -> {
                                            oldList.addAll(newList);
                                            return oldList;
                                        });
                                plugin.getLogger().info("Loaded successfully item n." + key + "'s commands (Right)");
                            }
                        }

                        // LEFT click
                        if (leftInteraction != null && leftInteraction.contains("Commands")) {
                            List<String> commands = leftInteraction.getStringList("Commands");
                            if (commands.isEmpty()) {
                                plugin.getLogger().severe("Configuration error in the file: "
                                        + StarInventory.inventoryFile.getFileLocation() + " component number: " + key);
                            } else {
                                plugin.getLogger().info("Loading item n." + key + "'s commands (Left)");
                                onInteractionCommand
                                        .computeIfAbsent(slot, s -> new HashMap<>())
                                        .merge(false, new ArrayList<>(commands), (oldList, newList) -> {
                                            oldList.addAll(newList);
                                            return oldList;
                                        });
                                plugin.getLogger().info("Loaded successfully item n." + key + "'s commands (Left)");
                            }
                        }
                    }


                }
            });
        }
    }

    public void reloadInventory() {
        blockedSlots.clear();
        inventoryItems.clear();
        onInteractionCommand.clear();
        StarInventory.inventoryFile.reloadconfig();
    }
}
