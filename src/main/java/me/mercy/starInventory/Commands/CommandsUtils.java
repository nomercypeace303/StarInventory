package me.mercy.starInventory.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class CommandsUtils {

    public void sendMessage(CommandSender sender, String message){
        Component componentmessage = MiniMessage.miniMessage().deserialize(message);
        sender.sendMessage(componentmessage);
    }

    public String replacePlaceholders(String message, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }
        return message;
    }

}
