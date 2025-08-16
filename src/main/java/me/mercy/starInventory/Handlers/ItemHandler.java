package me.mercy.starInventory.Handlers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ItemHandler {

    private final ConfigurationSection configuration;
    private final MiniMessage miniMessage;
    private ItemStack itemStack;
    private final Plugin plugin; // serve per NBT keys

    public ItemHandler(ConfigurationSection itemConfiguration, Plugin plugin) {
        this.configuration = itemConfiguration;
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();

        if (configuration.contains("Type")) {
            String typeString = configuration.getString("Type");
            if (typeString != null) {
                Material material = Material.getMaterial(typeString.toUpperCase());
                if (material != null) {
                    this.itemStack = new ItemStack(material);
                }
            }
        }
    }

    public ItemHandler setName() {
        if (itemStack == null || !configuration.contains("Name")) return this;

        String nameString = configuration.getString("Name");
        if (nameString == null) return this;

        Component name = miniMessage.deserialize(nameString);

        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.displayName(name);
            itemStack.setItemMeta(meta);
        }

        return this;
    }

    public ItemHandler setLore() {
        if (itemStack == null || !configuration.contains("Lore")) return this;

        List<String> loreStrings = configuration.getStringList("Lore");
        if (loreStrings.isEmpty()) return this;

        List<Component> lore = new ArrayList<>();
        for (String line : loreStrings) {
            lore.add(miniMessage.deserialize(line));
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.lore(lore);
            itemStack.setItemMeta(meta);
        }

        return this;
    }

    public ItemHandler setCustomModelData() {
        if (itemStack == null || !configuration.contains("CustomModelData")) return this;

        int modelData = configuration.getInt("CustomModelData");

        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setCustomModelData(modelData);
            itemStack.setItemMeta(meta);
        }

        return this;
    }

    public ItemHandler addFlags() {
        if (itemStack == null || !configuration.contains("Flags")) return this;

        List<String> flags = configuration.getStringList("Flags");

        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            for (String flag : flags) {
                try {
                    meta.addItemFlags(ItemFlag.valueOf(flag.toUpperCase()));
                } catch (IllegalArgumentException ignored) {
                    // ignoriamo flag non valida
                }
            }
            itemStack.setItemMeta(meta);
        }

        return this;
    }

    // Glow finto
    public ItemHandler setGlow() {
        if (itemStack == null) return this;

        boolean glow = configuration.getBoolean("Glow", false);
        if (!glow) return this;

        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.addEnchant(Enchantment.AQUA_AFFINITY, 1, true); // incanto fake
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);       // nasconde l'enchant
            itemStack.setItemMeta(meta);
        }

        return this;
    }

    // NBT custom (stringa opzionale "CustomNBTId" nella config)
    public ItemHandler setNBT() {
        if (itemStack == null) return this;
        if (!configuration.contains("CustomNBTId")) return this;

        String nbtId = configuration.getString("CustomNBTId");
        if (nbtId == null || nbtId.isEmpty()) return this;

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return this;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "custom_id");
        container.set(key, PersistentDataType.STRING, nbtId);

        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemStack buildItem() {
        return itemStack;
    }

    public ItemStack autoBuildItem() {
        return this.setName()
                .setLore()
                .setCustomModelData()
                .addFlags()
                .setGlow()
                .setNBT()
                .buildItem();
    }
}
