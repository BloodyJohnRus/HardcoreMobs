package com.blxxdx.mobs;

import com.blxxdx.mobs.command.Command;
import com.blxxdx.mobs.instance.BossBarInstance;
import com.blxxdx.mobs.instance.LocationInstance;
import com.blxxdx.mobs.instance.MobInstance;
import com.blxxdx.mobs.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

/* mobs.yml structure:
 * name:
 *   displayName: name
 *   health: 20
 *   type: ZOMBIE
 *   spawnLocation:
 *     x: 1
 *     y: 1
 *     z: 1
 *     world: world
 *   equipment: (optional)
 *     HEAD: ItemStack
 *     CHEST: ItemStack
 *     LEGS: ItemStack
 *     FEET: ItemStack
 *     HAND: ItemStack
 *     OFF_HAND: ItemStack
 *   bossBar:
 *     title: Name
 *     color: BarColor
 *     style: BarStyle
 *     flags: BarFlags...
 *     radius: 10
 *   eventScripts:
 *     attack: []
 *     death: []
 *   preSpawnScript: []
 *   spawnScript: []
 */
public class MobsMain extends JavaPlugin {
    private static MobsMain instance;
    public FileConfiguration mobs;
    public static String path;
    public MobsManager manager;
    public static MobsMain getInstance() { return instance; }
    public void load(){
        if(!new File(path + "mobs.yml").exists())
            try { new File(path + "mobs.yml").createNewFile(); } catch (Exception ignored) {}
        mobs = YamlConfiguration.loadConfiguration(new File(path+"mobs.yml"));
        loadMobs();
    }
    public void reload(){
        for(MobInstance inst: manager.instances.values())
            inst.stop();
        manager.instances.clear();
        load();
    }
    public void loadMobs(){
        for(String key: mobs.getKeys(false)){
            try {
                var section = mobs.getConfigurationSection(key);
                MobInstance inst = new MobInstance();
                String displayName = TextUtils.applyColor(section.getString("displayName"));
                int health = section.getInt("health");
                EntityType type = EntityType.valueOf(section.getString("type").toUpperCase());
                var lS = section.getConfigurationSection("spawnLocation");
                LocationInstance spawn = new LocationInstance(lS.getDouble("x"), lS.getDouble("y"), lS.getDouble("z"), lS.getString("world"));
                Map<EquipmentSlot, ItemStack> equipment = new HashMap<>();
                if (section.contains("equipment")) {
                    for (String t : section.getConfigurationSection("equipment").getKeys(false)) {
                        EquipmentSlot slot = EquipmentSlot.valueOf(t.toUpperCase());
                        ItemStack item = section.getConfigurationSection("equipment").getItemStack(t).clone();
                        equipment.put(slot, item);
                    }
                }
                Map<String, List<String>> eventScripts = new HashMap<>();
                for (String n : section.getConfigurationSection("eventScripts").getKeys(false)) {
                    getLogger().info(ChatColor.YELLOW + "Loaded event script: " + n);
                    eventScripts.put(n, section.getConfigurationSection("eventScripts").getStringList(n));
                }
                List<String> preSpawn = section.getStringList("preSpawnScript");
                List<String> spawnScript = section.getStringList("spawnScript");
                var bS = section.getConfigurationSection("bossBar");
                String title = TextUtils.applyColor(bS.getString("title"));
                BarColor color = BarColor.valueOf(bS.getString("color").toUpperCase());
                BarStyle style = BarStyle.valueOf(bS.getString("style").toUpperCase());
                int radius = bS.getInt("radius");
                BarFlag[] flags = new BarFlag[3];
                int j = 0;
                for (String flag : bS.getStringList("flags")) {
                    flags[j] = (BarFlag.valueOf(flag.toUpperCase()));
                    j++;
                }
                inst.equipment = equipment;
                inst.spawnLocation = spawn;
                inst.name = displayName;
                inst.health = health;
                inst.entityType = type;
                inst.pluginName = key;
                inst.preSpawnScript = preSpawn;
                inst.spawnScript = spawnScript;
                inst.eventScripts = eventScripts;
                inst.instance = new BossBarInstance(inst, radius, title, color, style, flags);
                inst.instance.runTaskTimer(this, 0, 1);
                getServer().getPluginManager().registerEvents(inst, this);
                manager.instances.put(key, inst);
                Bukkit.getLogger().info("Loaded mob: " + displayName);
            }
            catch (Exception e){
                getLogger().info(ChatColor.RED + "Failed to load mob " + key);
                getLogger().info(ChatColor.YELLOW + "Bro, check your mob config.");
            }
        }
    }
    public void onEnable() {
        instance = this;
        manager = new MobsManager();
        path = getDataFolder() + "/";
        getLogger().info(TextUtils.applyColor("&#E900FF&lH&#E912ED&la&#E924DB&lr&#E937C8&ld&#EA49B6&lc&#EA5BA4&lo&#EA6D92&lr&#EA8080&le&#EA926D&lM&#EAA45B&lo&#EAB649&lb&#EBC837&ls &#EBED12&lv&#EBFF00&l1"));
        load();
        getCommand("hmobs").setExecutor(new Command());
        getLogger().info(ChatColor.GREEN + "Plugin was successfully loaded.");
    }
    public void onDisable(){
        for(MobInstance inst: manager.instances.values())
            inst.stop();
    }
}
