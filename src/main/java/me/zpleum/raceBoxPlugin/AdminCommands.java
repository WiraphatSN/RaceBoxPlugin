package me.zpleum.raceBoxPlugin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommands implements CommandExecutor {
    private final RaceBoxPlugin plugin;

    public AdminCommands(RaceBoxPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }

        if (!player.hasPermission("racebox.admin")) {
            plugin.sendMessage(player, "no_permission");
            return true;
        }

        if (args.length == 0) {
            sendHelpMessage(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "spawn":
                plugin.spawnGiftBox(player.getLocation());
                plugin.sendMessage(player, "gift_box_spawned");
                break;
            case "remove":
                if (plugin.isGiftBox(player.getLocation())) {
                    plugin.removeGiftBox(player.getLocation());
                    plugin.sendMessage(player, "gift_box_removed");
                } else {
                    plugin.sendMessage(player, "no_gift_box");
                }
                break;
            case "removeall":
                plugin.removeAllGiftBoxes();
                plugin.sendMessage(player, "all_gift_boxes_removed");
                break;
            default:
                sendHelpMessage(player);
                break;
        }

        return true;
    }

    private void sendHelpMessage(Player player) {
        player.sendMessage("§6RaceBox Admin Commands:");
        player.sendMessage("§e/zadmin spawn §7- Spawn a gift box at your location");
        player.sendMessage("§e/zadmin remove §7- Remove a gift box at your location");
        player.sendMessage("§e/zadmin removeall §7- Remove all gift boxes");
    }
}