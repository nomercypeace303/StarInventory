
package me.mercy.starInventory.Commands;

import me.mercy.starInventory.Files.ConfigHandler;
import me.mercy.starInventory.StarInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class Commands implements CommandExecutor, TabCompleter {

    private final Plugin plugin = StarInventory.getMain();
    private final CommandsUtils cmdUtils = new CommandsUtils();
    private final ConfigHandler languageFile = StarInventory.getLanguageFile();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (args.length == 0) {
            for (String line : languageFile.getSection("Commands").getStringList("Help")) {
                cmdUtils.sendMessage(commandSender, line);
            }
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            StarInventory.getInventoryHandler().reloadInventory();
            StarInventory.languageFile.reloadconfig();
            languageFile.reloadconfig();
            cmdUtils.sendMessage(commandSender, languageFile.getSection("Commands").getString("Reload"));

        }

        if (args.length == 1 && args[0].equalsIgnoreCase("applychanges")) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                StarInventory.getInventoryHandler().setInventory(player);
            }
            cmdUtils.sendMessage(commandSender, languageFile.getSection("Commands").getString("ApplyChanges"));
        }

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("inventory") && args[0].equalsIgnoreCase("reload")) {
                StarInventory.getInventoryHandler().reloadInventory();
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("%file%", "inventory");
                String message = cmdUtils.replacePlaceholders(Objects.requireNonNull(languageFile.getSection("Commands").getString("ReloadSpecific")), placeholders);
                cmdUtils.sendMessage(commandSender, message);
            }
            if (args[1].equalsIgnoreCase("language") && args[0].equalsIgnoreCase("reload")) {
                StarInventory.languageFile.reloadconfig();
                languageFile.reloadconfig();
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("%file%", "language");
                String message = cmdUtils.replacePlaceholders(Objects.requireNonNull(languageFile.getSection("Commands").getString("ReloadSpecific")), placeholders);
                cmdUtils.sendMessage(commandSender, message);
            }
            if (args[1].equalsIgnoreCase("config") && args[0].equalsIgnoreCase("reload")) {
                plugin.saveConfig();
                plugin.reloadConfig();
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("%file%", "commands");
                String message = cmdUtils.replacePlaceholders(Objects.requireNonNull(languageFile.getSection("Commands").getString("ReloadSpecific")), placeholders);
                cmdUtils.sendMessage(commandSender, message);
            }
            if (args[1].equalsIgnoreCase("all") && args[0].equalsIgnoreCase("applychanges")) {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    StarInventory.getInventoryHandler().setInventory(player);
                }
                cmdUtils.sendMessage(commandSender, languageFile.getSection("Commands").getString("ApplyChanges"));
            }
            if (args[1].equalsIgnoreCase("self") && args[0].equalsIgnoreCase("applychanges") && commandSender instanceof Player player) {
                StarInventory.getInventoryHandler().setInventory(player);
                cmdUtils.sendMessage(commandSender, languageFile.getSection("Commands").getString("ApplyChanges"));
            }

        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (args.length == 1) {
            return List.of("reload", "applychanges");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("reload")) {
            return Stream.of("inventory", "language", "config")
                    .filter(suggestion -> suggestion.startsWith(args[1].toLowerCase()))
                    .toList();

        }
        if (args.length == 2 && args[0].equalsIgnoreCase("applychanges")) {
            return Stream.of("all", "self")
                    .filter(suggestion -> suggestion.startsWith(args[1].toLowerCase()))
                    .toList();

        }

        return List.of();
    }

}
