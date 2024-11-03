package me.zpleum.raceBoxPlugin;

import io.lumine.mythic.api.skills.Skill;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import fr.nocsy.mcpets.MCPets;

import java.util.*;

public class RaceBoxPlugin extends JavaPlugin {
    private Map<Location, UUID> giftBoxes;
    private MythicBukkit mythicMobs;
    private MCPets mcPets;
    private Random random;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        giftBoxes = new HashMap<>();
        random = new Random();

        if (!setupDependencies()) {
            getLogger().severe("Required plugins not found! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("zadmin").setExecutor(new AdminCommands(this));
        getServer().getPluginManager().registerEvents(new GiftBoxListener(this), this);
        loadGiftBoxes();
    }

    private boolean setupDependencies() {
        if (getServer().getPluginManager().getPlugin("MythicMobs") == null) return false;
        if (getServer().getPluginManager().getPlugin("MCPets") == null) return false;

        mythicMobs = MythicBukkit.inst();
        mcPets = (MCPets) getServer().getPluginManager().getPlugin("MCPets");
        return true;
    }

    public void spawnGiftBox(Location location) {
        String mobName = getConfig().getString("mythicmobs.gift_box", "GiftBox");
        ActiveMob mob = mythicMobs.getMobManager().spawnMob(mobName, location);
        if (mob != null) {
            giftBoxes.put(location, mob.getUniqueId());
            saveGiftBoxes();
        }
    }

    public void removeGiftBox(Location location) {
        UUID mobId = giftBoxes.remove(location);
        if (mobId != null) {
            mythicMobs.getMobManager().getActiveMob(mobId).ifPresent(ActiveMob::remove);
            saveGiftBoxes();
        }
    }

    public void removeAllGiftBoxes() {
        for (UUID mobId : giftBoxes.values()) {
            mythicMobs.getMobManager().getActiveMob(mobId).ifPresent(ActiveMob::remove);
        }
        giftBoxes.clear();
        saveGiftBoxes();
    }

    public void applyRandomEffect(Player player) {
        String[] effects = {"speed_boost", "slow", "banana", "rocket"};
        String effect = effects[random.nextInt(effects.length)];

        String skillName = getConfig().getString("mythicmobs.effects." + effect + ".skill");
        if (skillName != null) {
            // ใช้ MythicMobs Skill API เพื่อใช้งาน skill
            Skill skill = mythicMobs.getSkillManager().getSkill(skillName).orElse(null);
            if (skill != null) {
                mythicMobs.getAPIHelper().castSkill(player, skillName);
                String message = getConfig().getString("messages.effect_applied")
                        .replace("{effect}", effect);
                sendMessage(player, message);
            }
        }
    }

    public void sendMessage(Player player, String key) {
        String prefix = getConfig().getString("messages.prefix", "");
        String message = getConfig().getString("messages." + key, key);
        player.sendMessage(prefix + message.replace('&', '§'));
    }

    private void loadGiftBoxes() {
        // โค้ดโหลดตำแหน่งกล่องของขวัญจาก config
    }

    private void saveGiftBoxes() {
        // โค้ดบันทึกตำแหน่งกล่องของขวัญลง config
    }

    public boolean isGiftBox(Location location) {
        return giftBoxes.containsKey(location);
    }

    public MythicBukkit getMythicMobs() {
        return mythicMobs;
    }

    public MCPets getMcPets() {
        return mcPets;
    }
}