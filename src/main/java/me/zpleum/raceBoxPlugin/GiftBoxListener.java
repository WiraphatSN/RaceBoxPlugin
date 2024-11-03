package me.zpleum.raceBoxPlugin;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;

public class GiftBoxListener implements Listener {
    private final RaceBoxPlugin plugin;

    public GiftBoxListener(RaceBoxPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGiftBoxInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Location location = event.getRightClicked().getLocation();

        if (plugin.isGiftBox(location)) {
            event.setCancelled(true);
            plugin.applyRandomEffect(player);
            plugin.removeGiftBox(location);
        }
    }

    @EventHandler
    public void onGiftBoxDeath(MythicMobDeathEvent event) {
        Location location = event.getEntity().getLocation();
        if (plugin.isGiftBox(location)) {
            plugin.removeGiftBox(location);
        }
    }
}