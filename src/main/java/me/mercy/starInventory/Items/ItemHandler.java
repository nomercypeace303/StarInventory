package me.mercy.starInventory.Items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ItemHandler {

    private ConfigurationSection configuration;
    private ItemStack itemStack;


    public ItemHandler(ConfigurationSection itemConfiguration) {
        this.configuration = itemConfiguration;
        if (configuration.contains("Type")){
            this.itemStack = new ItemStack(Material.valueOf(Objects.requireNonNull(configuration.getString("Type"))));
        }
    }

    //todo: The itembuilder
    public changeName(MiniMessage name){
        Component newname = name.deserialize(configuration.getString("Name"));
        return null;
    }

    public ItemStack BuildItem() {






        return null;
    }
}
