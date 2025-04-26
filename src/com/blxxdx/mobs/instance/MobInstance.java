package com.blxxdx.mobs.instance;

import com.blxxdx.mobs.MobsMain;
import com.blxxdx.mobs.utils.MapUtil;
import com.blxxdx.mobs.utils.TextUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.DamageSource;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import ru.leymooo.antirelog.Antirelog;

import javax.annotation.Nullable;
import java.util.*;

public class MobInstance implements Listener {

    private String replaceArgs(String text, boolean command){
        var prefix = text.split(" ")[0];
        if(command) {
            var message = text.substring((text.split(" ")[0] + " ").length());
            if (message.startsWith("%center%")) {
                message = TextUtils.getCenteredMessage(message.replace("%center%", "")
                        .replace("%name%", TextUtils.applyColor(name)));
            } else {
                message = message.replace("%name%", TextUtils.applyColor(name));
                if (message.contains("%top1%"))
                    if (!playerDamages.isEmpty()) {
                        message = message.replace("%top1%", new ArrayList<>(playerDamages.keySet()).get(0));
                    } else
                        message = message.replace("%top1%", "null");
                if (message.contains("%top1_damage%"))
                    if (!playerDamages.isEmpty()) {
                        message = message.replace("%top1_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(0))));
                    }
                    else {
                        message = message.replace("%top1_damage%", "null");
                    }
                if (message.contains("%top2%"))
                    if (playerDamages.size() > 1) {
                        message = message.replace("%top2%", new ArrayList<>(playerDamages.keySet()).get(1));
                    }
                    else {
                        message = message.replace("%top2%", "null");
                    }
                if (message.contains("%top2_damage%"))
                    if (playerDamages.size() > 1) {
                        message = message.replace("%top2_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(1))));
                    }
                    else {
                        message = message.replace("%top2_damage%", "null");
                    }
                if (message.contains("%top3%"))
                    if (playerDamages.size() > 2) {
                        message = message.replace("%top3%", new ArrayList<>(playerDamages.keySet()).get(2));
                    }
                    else {
                        message = message.replace("%top3%", "null");
                    }
                if (message.contains("%top3_damage%"))
                    if (playerDamages.size() > 2) {
                        message = message.replace("%top3_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(2))));
                    }
                    else {
                        message = message.replace("%top3_damage%", "null");
                    }
            }
            return prefix + " " + message;
        }
        else {
            var message = text;
            if (message.startsWith("%center%")) {
                message = TextUtils.getCenteredMessage(message.replace("%center%", "")
                        .replace("%name%", TextUtils.applyColor(name)));
            } else {
                message = message.replace("%name%", TextUtils.applyColor(name));
                if (message.contains("%top1%"))
                    if (!playerDamages.isEmpty()) {
                        message = message.replace("%top1%", new ArrayList<>(playerDamages.keySet()).get(0));
                    } else
                        message = message.replace("%top1%", "null");
                if (message.contains("%top1_damage%"))
                    if (!playerDamages.isEmpty()) {
                        message = message.replace("%top1_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(0))));
                    }
                    else {
                        message = message.replace("%top1_damage%", "null");
                    }
                if (message.contains("%top2%"))
                    if (playerDamages.size() > 1) {
                        message = message.replace("%top2%", new ArrayList<>(playerDamages.keySet()).get(1));
                    }
                    else {
                        message = message.replace("%top2%", "null");
                    }
                if (message.contains("%top2_damage%"))
                    if (playerDamages.size() > 1) {
                        message = message.replace("%top2_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(1))));
                    }
                    else {
                        message = message.replace("%top2_damage%", "null");
                    }
                if (message.contains("%top3%"))
                    if (playerDamages.size() > 2) {
                        message = message.replace("%top3%", new ArrayList<>(playerDamages.keySet()).get(2));
                    }
                    else {
                        message = message.replace("%top3%", "null");
                    }
                if (message.contains("%top3_damage%"))
                    if (playerDamages.size() > 2) {
                        message = message.replace("%top3_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(2))));
                    }
                    else {
                        message = message.replace("%top3_damage%", "null");
                    }
            }
            return message;
        }
    }
    public void executeScript(List<String> script, boolean without, @Nullable Map<String, String> extraVars){
        for(int index = 0; index<script.size(); index++) {
            String line = script.get(index);

            if(line.startsWith("[DIRECT]")){
                String orig = line.substring("[DIRECT] ".length());
                if (extraVars != null) {
                    for (String arg : extraVars.keySet()) {
                        if (orig.contains("%" + arg + "%"))
                            orig = orig.replace("%" + arg + "%", extraVars.get(arg));
                    }
                }
                String name = PlaceholderAPI.setPlaceholders(null, replaceArgs(orig.split(" ")[0], false));
                StringBuilder message = new StringBuilder(orig.split(" ")[1]);
                for(int i = 2; i<orig.split(" ").length; i++){
                    message.append(" ").append(orig.split(" ")[i]);
                }
                message = new StringBuilder(PlaceholderAPI.setPlaceholders(null, replaceArgs(message.toString(), false)));
                if(Bukkit.getPlayer(name)!=null)
                    if(Bukkit.getPlayer(name).isOnline())
                        Bukkit.getPlayer(name).sendMessage(TextUtils.applyColor(message.toString()));
            }
            if(line.startsWith("[ATTACK]")){
                if(currentEntity!=null) {
                    String message = line.substring("[ATTACK] ".length());
                    if (extraVars != null) {
                        for (String arg : extraVars.keySet()) {
                            if (message.contains("%" + arg + "%")) {
                                message = message.replace("%" + arg + "%", extraVars.get(arg));
                            }
                        }
                    }
                    message = PlaceholderAPI.setPlaceholders(null, replaceArgs(message, false));
                    String name = message.split(" ")[0];
                    double damage = Double.parseDouble(message.split(" ")[1]);
                    if(Bukkit.getPlayer(name)!=null)
                        if(Bukkit.getPlayer(name).isOnline())
                            ((CraftPlayer) Bukkit.getPlayer(name)).getHandle().damageEntity(DamageSource.MAGIC, (float) damage);
                }
            }
            if(line.startsWith("[SET]")){
                String orig = line.substring("[SET] ".length());
                String name = orig.split(" ")[0];
                String value = PlaceholderAPI.setPlaceholders(null, replaceArgs(orig.split(" ")[1], false));
                customValues.put(name, value);
            }
            if (line.startsWith("[CHECK]")){
                String orig = line.substring("[CHECK] ".length());
                String condition = orig.split(" ")[0];
                if(condition.contains("==") || condition.contains("!=")){
                    String substring = orig.substring((condition + " ").length());
                    if(extraVars!=null) {
                        for (String arg : extraVars.keySet()) {
                            if (substring.contains("%" + arg + "%"))
                                substring = substring.replace("%" + arg + "%", extraVars.get(arg));
                        }
                    }
                    if(condition.contains("==")){
                        String[] vars = PlaceholderAPI.setPlaceholders(null, replaceArgs(condition, false)).split("==");
                        if(vars[0].equals(vars[1])){
                            executeScript(script, replaceArgs(substring, true), false, index, extraVars);
                        }
                    }
                    if(condition.contains("!=")){
                        String[] vars = PlaceholderAPI.setPlaceholders(null, replaceArgs(condition, false)).split("!=");
                        if(!vars[0].equals(vars[1])){
                            executeScript(script, replaceArgs(substring, true), false, index, extraVars);
                        }
                    }
                }
            }
            if (line.startsWith("[CONSOLE]")) {
                String message = line.substring("[CONSOLE] ".length())
                        .replace("%name%", TextUtils.applyColor(name));
                if (extraVars != null) {
                    for (String arg : extraVars.keySet()) {
                        if (message.contains("%" + arg + "%")) {
                            message = message.replace("%" + arg + "%", extraVars.get(arg));
                        }
                    }
                }
                if (message.contains("%top1%") && !message.contains("'%top1%'"))
                    if (!playerDamages.isEmpty())
                        message = message.replace("%top1%", new ArrayList<>(playerDamages.keySet()).get(0));
                if (message.contains("%top1_damage%") && !message.contains("'%top1_damage%'"))
                    if (!playerDamages.isEmpty())
                        message = message.replace("%top1_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(0))));
                if (message.contains("%top2%") && !message.contains("'%top2%'"))
                    if (playerDamages.size() > 1)
                        message = message.replace("%top2%", new ArrayList<>(playerDamages.keySet()).get(1));
                if (message.contains("%top2_damage%") && !message.contains("'%top2_damage%'"))
                    if (playerDamages.size() > 1)
                        message = message.replace("%top2_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(1))));
                if (message.contains("%top3%") && !message.contains("'%top3%'"))
                    if (playerDamages.size() > 2)
                        message = message.replace("%top3%", new ArrayList<>(playerDamages.keySet()).get(2));
                if (message.contains("%top3_damage%") && !message.contains("'%top3_damage%'"))
                    if (playerDamages.size() > 2)
                        message = message.replace("%top3_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(2))));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), TextUtils.applyColor(message));
            }
            if (line.startsWith("[BROADCAST]")) {
                String message = line.substring("[BROADCAST] ".length());
                if (message.startsWith("%center%")) {
                    message = TextUtils.getCenteredMessage(message.replace("%center%", "")
                            .replace("%name%", TextUtils.applyColor(name)));
                }
                else {
                    message = message.replace("%name%", TextUtils.applyColor(name));
                    if (message.contains("%top1%") && !message.contains("'%top1%'"))
                        if (!playerDamages.isEmpty())
                            message = message.replace("%top1%", new ArrayList<>(playerDamages.keySet()).get(0));
                    if (message.contains("%top1_damage%") && !message.contains("'%top1_damage%'"))
                        if (!playerDamages.isEmpty())
                            message = message.replace("%top1_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(0))));
                    if (message.contains("%top2%") && !message.contains("'%top2%'"))
                        if (playerDamages.size() > 1)
                            message = message.replace("%top2%", new ArrayList<>(playerDamages.keySet()).get(1));
                    if (message.contains("%top2_damage%") && !message.contains("'%top2_damage%'"))
                        if (playerDamages.size() > 1)
                            message = message.replace("%top2_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(1))));
                    if (message.contains("%top3%") && !message.contains("'%top3%'"))
                        if (playerDamages.size() > 2)
                            message = message.replace("%top3%", new ArrayList<>(playerDamages.keySet()).get(2));
                    if (message.contains("%top3_damage%") && !message.contains("'%top3_damage%'"))
                        if (playerDamages.size() > 2)
                            message = message.replace("%top3_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(2))));
                }
                if (extraVars != null) {
                    for (String arg : extraVars.keySet()) {
                        if (message.contains("%" + arg + "%")) {
                            message = message.replace("%" + arg + "%", extraVars.get(arg));
                        }
                    }
                }
                Bukkit.broadcastMessage(TextUtils.applyColor(message));
            }
            if (line.startsWith("[PLAYERS]")) {
                int radius = Integer.parseInt(line.split(" ")[1]);
                String command = line.substring((line.split(" ")[0] + " " + radius + " ").length());
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if(!p.getLocation().getWorld().getName().equals(currentEntity.getLocation().getWorld().getName())) continue;
                    if (p.getLocation().distance(currentEntity.getLocation()) < radius) {
                        String message = TextUtils.applyColor(command.replace("%player%", p.getName())
                                .replace("%name%", TextUtils.applyColor(name)));
                        if (extraVars != null) {
                            for (String arg : extraVars.keySet()) {
                                if (message.contains("%" + arg + "%")) {
                                    message = message.replace("%" + arg + "%", extraVars.get(arg));
                                }
                            }
                        }
                        if (message.contains("%top1%") && !message.contains("'%top1%'"))
                            if (!playerDamages.isEmpty())
                                message = message.replace("%top1%", new ArrayList<>(playerDamages.keySet()).get(0));
                        if (message.contains("%top1_damage%") && !message.contains("'%top1_damage%'"))
                            if (!playerDamages.isEmpty())
                                message = message.replace("%top1_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(0))));
                        if (message.contains("%top2%") && !message.contains("'%top2%'"))
                            if (playerDamages.size() > 1)
                                message = message.replace("%top2%", new ArrayList<>(playerDamages.keySet()).get(1));
                        if (message.contains("%top2_damage%") && !message.contains("'%top2_damage%'"))
                            if (playerDamages.size() > 1)
                                message = message.replace("%top2_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(1))));
                        if (message.contains("%top3%") && !message.contains("'%top3%'"))
                            if (playerDamages.size() > 2)
                                message = message.replace("%top3%", new ArrayList<>(playerDamages.keySet()).get(2));
                        if (message.contains("%top3_damage%") && !message.contains("'%top3_damage%'")) {
                            if (playerDamages.size() > 2)
                                message = message.replace("%top3_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(2))));
                        }
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(p, message));
                    }
                }
            }

            if (line.startsWith("[RANDOM_PLAYER]")) {
                if(currentEntity!=null) {
                    int radius = Integer.parseInt(line.split(" ")[1]);
                    String command = line.substring((line.split(" ")[0] + " " + radius + " ").length());
                    Player p = List.copyOf(Bukkit.getOnlinePlayers()).get(new Random().nextInt(Bukkit.getOnlinePlayers().size()));
                    if (!p.getLocation().getWorld().getName().equals(currentEntity.getLocation().getWorld().getName()))
                        return;
                    if (p.getLocation().distance(currentEntity.getLocation()) < radius) {
                        String message = TextUtils.applyColor(command.replace("%player%", p.getName())
                                .replace("%name%", TextUtils.applyColor(name)));
                        if (extraVars != null) {
                            for (String arg : extraVars.keySet()) {
                                if (message.contains("%" + arg + "%")) {
                                    message = message.replace("%" + arg + "%", extraVars.get(arg));
                                }
                            }
                        }
                        if (message.contains("%top1%") && !message.contains("'%top1%'"))
                            if (!playerDamages.isEmpty())
                                message = message.replace("%top1%", new ArrayList<>(playerDamages.keySet()).get(0));
                        if (message.contains("%top1_damage%") && !message.contains("'%top1_damage%'"))
                            if (!playerDamages.isEmpty())
                                message = message.replace("%top1_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(0))));
                        if (message.contains("%top2%") && !message.contains("'%top2%'"))
                            if (playerDamages.size() > 1)
                                message = message.replace("%top2%", new ArrayList<>(playerDamages.keySet()).get(1));
                        if (message.contains("%top2_damage%") && !message.contains("'%top2_damage%'"))
                            if (playerDamages.size() > 1)
                                message = message.replace("%top2_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(1))));
                        if (message.contains("%top3%") && !message.contains("'%top3%'"))
                            if (playerDamages.size() > 2)
                                message = message.replace("%top3%", new ArrayList<>(playerDamages.keySet()).get(2));
                        if (message.contains("%top3_damage%") && !message.contains("'%top3_damage%'"))
                            if (playerDamages.size() > 2)
                                message = message.replace("%top3_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(2))));
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(p, message));
                    }
                }
            }

            if (line.startsWith("[RANDOM]")) {
                int chance = Integer.parseInt(line.split(" ")[1]);
                String command = line.substring((line.split(" ")[0] + " " + chance + " ").length());
                if (new Random().nextDouble() * 100 < chance)
                    executeScript(script, command, false, 0, extraVars);
            }
            if (line.startsWith("[MESSAGE]")) {
                if(currentEntity!=null) {
                    int radius = Integer.parseInt(line.split(" ")[1]);
                    String command = line.substring((line.split(" ")[0] + " " + radius + " ").length());
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!p.getLocation().getWorld().getName().equals(currentEntity.getLocation().getWorld().getName()))
                            continue;
                        if (p.getLocation().distance(currentEntity.getLocation()) < radius) {
                            String message = TextUtils.applyColor(PlaceholderAPI.setPlaceholders(p, command.replace("%player%", p.getName())
                                    .replace("%name%", TextUtils.applyColor(name))));
                            if (extraVars != null) {
                                for (String arg : extraVars.keySet()) {
                                    if (message.contains("%" + arg + "%")) {
                                        message = message.replace("%" + arg + "%", extraVars.get(arg));
                                    }
                                }
                            }
                            if (command.startsWith("%center%"))
                                message = TextUtils.getCenteredMessage(message.replace("%center%", ""));
                            if (message.contains("%top1%") && !message.contains("'%top1%'"))
                                if (!playerDamages.isEmpty())
                                    message = message.replace("%top1%", new ArrayList<>(playerDamages.keySet()).get(0));
                            if (message.contains("%top1_damage%") && !message.contains("'%top1_damage%'"))
                                if (!playerDamages.isEmpty())
                                    message = message.replace("%top1_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(0))));
                            if (message.contains("%top2%") && !message.contains("'%top2%'"))
                                if (playerDamages.size() > 1)
                                    message = message.replace("%top2%", new ArrayList<>(playerDamages.keySet()).get(1));
                            if (message.contains("%top2_damage%") && !message.contains("'%top2_damage%'"))
                                if (playerDamages.size() > 1)
                                    message = message.replace("%top2_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(1))));
                            if (message.contains("%top3%") && !message.contains("'%top3%'"))
                                if (playerDamages.size() > 2)
                                    message = message.replace("%top3%", new ArrayList<>(playerDamages.keySet()).get(2));
                            if (message.contains("%top3_damage%") && !message.contains("'%top3_damage%'"))
                                if (playerDamages.size() > 2)
                                    message = message.replace("%top3_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(2))));
                            p.sendMessage(message);
                        }
                    }
                }
            }
            if (line.startsWith("[ACTIONBAR]")) {
                if(currentEntity!=null) {
                    int radius = Integer.parseInt(line.split(" ")[1]);
                    String command = line.substring((line.split(" ")[0] + " " + radius + " ").length());
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!p.getLocation().getWorld().getName().equals(currentEntity.getLocation().getWorld().getName()))
                            continue;
                        if (p.getLocation().distance(currentEntity.getLocation()) < radius) {
                            String message =
                                    TextUtils.applyColor(PlaceholderAPI.setPlaceholders(p, command.replace("%player%", p.getName())
                                            .replace("%name%", TextUtils.applyColor(name))));
                            if (extraVars != null) {
                                for (String arg : extraVars.keySet()) {
                                    if (message.contains("%" + arg + "%")) {
                                        message = message.replace("%" + arg + "%", extraVars.get(arg));
                                    }
                                }
                            }
                            if (command.startsWith("%center%"))
                                message = TextUtils.getCenteredMessage(message.replace("%center%", ""));
                            if (message.contains("%top1%") && !message.contains("'%top1%'"))
                                if (!playerDamages.isEmpty())
                                    message = message.replace("%top1%", new ArrayList<>(playerDamages.keySet()).get(0));
                            if (message.contains("%top1_damage%") && !message.contains("'%top1_damage%'"))
                                if (!playerDamages.isEmpty())
                                    message = message.replace("%top1_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(0))));
                            if (message.contains("%top2%") && !message.contains("'%top2%'"))
                                if (playerDamages.size() > 1)
                                    message = message.replace("%top2%", new ArrayList<>(playerDamages.keySet()).get(1));
                            if (message.contains("%top2_damage%") && !message.contains("'%top2_damage%'"))
                                if (playerDamages.size() > 1)
                                    message = message.replace("%top2_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(1))));
                            if (message.contains("%top3%") && !message.contains("'%top3%'"))
                                if (playerDamages.size() > 2)
                                    message = message.replace("%top3%", new ArrayList<>(playerDamages.keySet()).get(2));
                            if (message.contains("%top3_damage%") && !message.contains("'%top3_damage%'"))
                                if (playerDamages.size() > 2)
                                    message = message.replace("%top3_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(2))));
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
                        }
                    }
                }
            }
            if (line.startsWith("[SOUND]")) {
                var split = line.split(" ");
                Sound sound = Sound.valueOf(split[1].toUpperCase());
                float volume = Float.parseFloat(split[2]);
                float pitch = Float.parseFloat(split[3]);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.playSound(p.getLocation(), sound, volume, pitch);
                }
            }
            if (line.startsWith("[SOUND_GLOBAL]")) {
                var split = line.split(" ");
                Sound sound = Sound.valueOf(split[1].toUpperCase());
                float volume = Float.parseFloat(split[2]);
                float pitch = Float.parseFloat(split[3]);
                spawnLocation.world.playSound(spawnLocation.toLocation(), sound, volume, pitch);
            }
            if (line.startsWith("[PLAYERSOUND]")) {

                if(currentEntity!=null) {
                    var split = line.split(" ");
                    int radius = Integer.parseInt(split[1]);
                    Sound sound = Sound.valueOf(split[2].toUpperCase());
                    float volume = Float.parseFloat(split[3]);
                    float pitch = Float.parseFloat(split[4]);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!p.getLocation().getWorld().getName().equals(currentEntity.getLocation().getWorld().getName()))
                            continue;
                        if (p.getLocation().distance(currentEntity.getLocation()) < radius)
                            p.playSound(p.getLocation(), sound, volume, pitch);
                    }
                }
            }
            if (line.startsWith("[COOLDOWN]")) {
                var split = line.split(" ");
                int radius = Integer.parseInt(split[1]);
                Material material = Material.valueOf(split[2].toUpperCase());
                int cooldown = Integer.parseInt(split[3]);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if(!p.getLocation().getWorld().getName().equals(currentEntity.getLocation().getWorld().getName())) continue;
                    if (p.getLocation().distance(currentEntity.getLocation()) < radius)
                        p.setCooldown(material, cooldown);
                }
            }
            if (line.startsWith("[DELAY]")) {
                long delay = Long.parseLong(line.split(" ")[1]);
                StringBuilder lin = new StringBuilder(line.split(" ")[2]);
                for(int i = 3; i<line.split(" ").length; i++)
                    lin.append(" ").append(line.split(" ")[i]);
                String finalLin = lin.toString();
                int finalIndex1 = index;
                Bukkit.broadcastMessage(finalLin);
                BukkitRunnable run = new BukkitRunnable() {
                    @Override
                    public void run() {
                        executeScript(script, finalLin, false, finalIndex1, extraVars);
                    }
                };
                run.runTaskLater(MobsMain.getInstance(), delay);
                scriptRunners.put(UUID.randomUUID().toString(), run);
            }
            if(line.startsWith("[SPAWN]"))
                spawn();
            if(line.startsWith("[EFFECT]")){
                var split = line.split(" ");
                var type = PotionEffectType.getByName(split[1].toUpperCase());
                int duration = Integer.parseInt(split[2])*20;
                int amp = Integer.parseInt(split[3]);
                if(currentEntity!=null && type!=null){
                    currentEntity.addPotionEffect(new PotionEffect(type, duration, amp));
                }
            }
            if (line.startsWith("[ANTIRELOG]")) {

                if (currentEntity != null) {
                    var split = line.split(" ");
                    int radius = Integer.parseInt(split[1]);
                    List<Player> radiusPlayers = new ArrayList<>();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!p.getLocation().getWorld().getName().equals(currentEntity.getLocation().getWorld().getName()))
                            continue;
                        if (p.getLocation().distance(currentEntity.getLocation()) < radius) radiusPlayers.add(p);
                    }
                    Antirelog ar = (Antirelog) Bukkit.getPluginManager().getPlugin("AntiRelog");
                    if (radiusPlayers.size() > 1) {
                        Player random = radiusPlayers.get(new Random().nextInt(radiusPlayers.size()));
                        for (Player p : radiusPlayers) {
                            if (random.getName().equals(p.getName())) continue;
                            if (ar != null) {
                                ar.getPvpManager().playerDamagedByPlayer(p, random);
                            }
                        }
                    }
                }
            }
            if (line.startsWith("[REPEATBREAK]")) {
                String name = line.split(" ")[1];
                if (scriptRunners.containsKey(name))
                    scriptRunners.get(name).cancel();
            }
            if (line.startsWith("[REPEAT]")) {
                int delay = Integer.parseInt(line.split(" ")[1]);
                String name = line.split(" ")[2];
                int stopIndex = findLoopEnd(index, script);
                if (stopIndex == -1) stopIndex = script.size() - 1;
//                Bukkit.broadcastMessage("Detected new repeat: " + name + " | Start: " + index + " | End: " + stopIndex);
                int finalIndex = index;
                int finalStopIndex = stopIndex;
                BukkitRunnable run = new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (int i = finalIndex; i < finalStopIndex; i++)
                            executeScript(script, script.get(i), false, 0, extraVars);
                    }
                };
                run.runTaskTimer(MobsMain.getInstance(), 0, delay);
                scriptRunners.put(name, run);
                index = stopIndex;
            }
            if (line.startsWith("[FOR]")) {
                int times = Integer.parseInt(line.split(" ")[1]);
                int delay = Integer.parseInt(line.split(" ")[2]);
                int stopIndex = findForLoopEnd(index, script);
                if (stopIndex == -1) stopIndex = script.size() - 1;
//                Bukkit.broadcastMessage("Detected new for | Start: " + index + " | End: " + stopIndex);
                int finalIndex = index;
                int finalStopIndex = stopIndex;
                for(int i = 0; i<times; i++)
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (int i = finalIndex; i < finalStopIndex; i++)
                                executeScript(script, script.get(i), true, i, extraVars);
                        }
                    }.runTaskLater(MobsMain.getInstance(), (long) delay * i);
                index = stopIndex;
            }
        }
    }
    public void executeScript(List<String> script, String line, Boolean forEnabled, int index, @Nullable Map<String, String> extraVars) {
        //Bukkit.broadcastMessage("Executing: " + line);

        if(line.startsWith("[DIRECT]")){
            String orig = line.substring("[DIRECT] ".length());
            if (extraVars != null) {
                for (String arg : extraVars.keySet()) {
                    if (orig.contains("%" + arg + "%"))
                        orig = orig.replace("%" + arg + "%", extraVars.get(arg));
                }
            }
            String name = PlaceholderAPI.setPlaceholders(null, replaceArgs(orig.split(" ")[0], false));
            StringBuilder message = new StringBuilder(orig.split(" ")[1]);
            for(int i = 2; i<orig.split(" ").length; i++){
                message.append(" ").append(orig.split(" ")[i]);
            }
            message = new StringBuilder(PlaceholderAPI.setPlaceholders(null, replaceArgs(message.toString(), false)));
            if(Bukkit.getPlayer(name)!=null)
                if(Bukkit.getPlayer(name).isOnline())
                    Bukkit.getPlayer(name).sendMessage(TextUtils.applyColor(message.toString()));
        }

        if(line.startsWith("[ATTACK]")){
            if(currentEntity!=null) {
                String message = line.substring("[ATTACK] ".length());
                if (extraVars != null) {
                    for (String arg : extraVars.keySet()) {
                        if (message.contains("%" + arg + "%")) {
                            message = message.replace("%" + arg + "%", extraVars.get(arg));
                        }
                    }
                }
                message = PlaceholderAPI.setPlaceholders(null, replaceArgs(message, false));
                String name = message.split(" ")[0];
                double damage = Double.parseDouble(message.split(" ")[1]);
                if(Bukkit.getPlayer(name)!=null)
                    if(Bukkit.getPlayer(name).isOnline())
                        ((CraftPlayer) Bukkit.getPlayer(name)).getHandle().damageEntity(DamageSource.MAGIC, (float) damage);
            }
        }
        if(line.startsWith("[SET]")){
            String orig = line.substring("[SET] ".length());
            String name = orig.split(" ")[0];
            String value = PlaceholderAPI.setPlaceholders(null, replaceArgs(orig.split(" ")[1], false));
            customValues.put(name, value);
        }
        if (line.startsWith("[CHECK]")){
            String orig = line.substring("[CHECK] ".length());
            String condition = orig.split(" ")[0];
            if(condition.contains("==") || condition.contains("!=")){
                String substring = orig.substring((condition + " ").length());
                if(extraVars!=null) {
                    for (String arg : extraVars.keySet()) {
                        if (substring.contains("%" + arg + "%"))
                            substring = substring.replace("%" + arg + "%", extraVars.get(arg));
                    }
                }
                if(condition.contains("==")){
                    String[] vars = PlaceholderAPI.setPlaceholders(null, replaceArgs(condition, false)).split("==");
                    if(vars[0].equals(vars[1])){
                        executeScript(script, replaceArgs(substring, true), false, index, extraVars);
                    }
                }
                if(condition.contains("!=")){
                    String[] vars = PlaceholderAPI.setPlaceholders(null, replaceArgs(condition, false)).split("!=");
                    if(!vars[0].equals(vars[1])){
                        executeScript(script, replaceArgs(substring, true), false, index, extraVars);
                    }
                }
            }
        }
        if (line.startsWith("[CONSOLE]")) {
            String message = line.substring("[CONSOLE] ".length())
                    .replace("%name%", TextUtils.applyColor(name));
            if (extraVars != null) {
                for (String arg : extraVars.keySet()) {
                    if (message.contains("%" + arg + "%")) {
                        message = message.replace("%" + arg + "%", extraVars.get(arg));
                    }
                }
            }
            if (message.contains("%top1%") && !message.contains("'%top1%'"))
                if (!playerDamages.isEmpty())
                    message = message.replace("%top1%", new ArrayList<>(playerDamages.keySet()).get(0));
            if (message.contains("%top1_damage%") && !message.contains("'%top1_damage%'"))
                if (!playerDamages.isEmpty())
                    message = message.replace("%top1_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(0))));
            if (message.contains("%top2%") && !message.contains("'%top2%'"))
                if (playerDamages.size() > 1)
                    message = message.replace("%top2%", new ArrayList<>(playerDamages.keySet()).get(1));
            if (message.contains("%top2_damage%") && !message.contains("'%top2_damage%'"))
                if (playerDamages.size() > 1)
                    message = message.replace("%top2_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(1))));
            if (message.contains("%top3%") && !message.contains("'%top3%'"))
                if (playerDamages.size() > 2)
                    message = message.replace("%top3%", new ArrayList<>(playerDamages.keySet()).get(2));
            if (message.contains("%top3_damage%") && !message.contains("'%top3_damage%'"))
                if (playerDamages.size() > 2)
                    message = message.replace("%top3_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(2))));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), TextUtils.applyColor(message));
        }
        if (line.startsWith("[BROADCAST]")) {
            String message = line.substring("[BROADCAST] ".length());
            if (message.startsWith("%center%")) {
                message = TextUtils.getCenteredMessage(message.replace("%center%", "")
                        .replace("%name%", TextUtils.applyColor(name)));
            }
            else {
                message = message.replace("%name%", TextUtils.applyColor(name));
                if (message.contains("%top1%") && !message.contains("'%top1%'"))
                    if (!playerDamages.isEmpty())
                        message = message.replace("%top1%", new ArrayList<>(playerDamages.keySet()).get(0));
                if (message.contains("%top1_damage%") && !message.contains("'%top1_damage%'"))
                    if (!playerDamages.isEmpty())
                        message = message.replace("%top1_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(0))));
                if (message.contains("%top2%") && !message.contains("'%top2%'"))
                    if (playerDamages.size() > 1)
                        message = message.replace("%top2%", new ArrayList<>(playerDamages.keySet()).get(1));
                if (message.contains("%top2_damage%") && !message.contains("'%top2_damage%'"))
                    if (playerDamages.size() > 1)
                        message = message.replace("%top2_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(1))));
                if (message.contains("%top3%") && !message.contains("'%top3%'"))
                    if (playerDamages.size() > 2)
                        message = message.replace("%top3%", new ArrayList<>(playerDamages.keySet()).get(2));
                if (message.contains("%top3_damage%") && !message.contains("'%top3_damage%'"))
                    if (playerDamages.size() > 2)
                        message = message.replace("%top3_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(2))));
            }
            if (extraVars != null) {
                for (String arg : extraVars.keySet()) {
                    if (message.contains("%" + arg + "%")) {
                        message = message.replace("%" + arg + "%", extraVars.get(arg));
                    }
                }
            }
            Bukkit.broadcastMessage(TextUtils.applyColor(message));
        }
        if (line.startsWith("[PLAYERS]")) {
            int radius = Integer.parseInt(line.split(" ")[1]);
            String command = line.substring((line.split(" ")[0] + " " + radius + " ").length());
            for (Player p : Bukkit.getOnlinePlayers()) {
                if(!p.getLocation().getWorld().getName().equals(currentEntity.getLocation().getWorld().getName())) continue;
                if (p.getLocation().distance(currentEntity.getLocation()) < radius) {
                    String message = TextUtils.applyColor(command.replace("%player%", p.getName())
                            .replace("%name%", TextUtils.applyColor(name)));
                    if (extraVars != null) {
                        for (String arg : extraVars.keySet()) {
                            if (message.contains("%" + arg + "%")) {
                                message = message.replace("%" + arg + "%", extraVars.get(arg));
                            }
                        }
                    }
                    if (message.contains("%top1%") && !message.contains("'%top1%'"))
                        if (!playerDamages.isEmpty())
                            message = message.replace("%top1%", new ArrayList<>(playerDamages.keySet()).get(0));
                    if (message.contains("%top1_damage%") && !message.contains("'%top1_damage%'"))
                        if (!playerDamages.isEmpty())
                            message = message.replace("%top1_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(0))));
                    if (message.contains("%top2%") && !message.contains("'%top2%'"))
                        if (playerDamages.size() > 1)
                            message = message.replace("%top2%", new ArrayList<>(playerDamages.keySet()).get(1));
                    if (message.contains("%top2_damage%") && !message.contains("'%top2_damage%'"))
                        if (playerDamages.size() > 1)
                            message = message.replace("%top2_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(1))));
                    if (message.contains("%top3%") && !message.contains("'%top3%'"))
                        if (playerDamages.size() > 2)
                            message = message.replace("%top3%", new ArrayList<>(playerDamages.keySet()).get(2));
                    if (message.contains("%top3_damage%") && !message.contains("'%top3_damage%'")) {
                        if (playerDamages.size() > 2)
                            message = message.replace("%top3_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(2))));
                    }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(p, message));
                }
            }
        }

        if (line.startsWith("[RANDOM_PLAYER]")) {
            if(currentEntity!=null) {
                int radius = Integer.parseInt(line.split(" ")[1]);
                String command = line.substring((line.split(" ")[0] + " " + radius + " ").length());
                Player p = List.copyOf(Bukkit.getOnlinePlayers()).get(new Random().nextInt(Bukkit.getOnlinePlayers().size()));
                if (!p.getLocation().getWorld().getName().equals(currentEntity.getLocation().getWorld().getName()))
                    return;
                if (p.getLocation().distance(currentEntity.getLocation()) < radius) {
                    String message = TextUtils.applyColor(command.replace("%player%", p.getName())
                            .replace("%name%", TextUtils.applyColor(name)));
                    if (extraVars != null) {
                        for (String arg : extraVars.keySet()) {
                            if (message.contains("%" + arg + "%")) {
                                message = message.replace("%" + arg + "%", extraVars.get(arg));
                            }
                        }
                    }
                    if (message.contains("%top1%") && !message.contains("'%top1%'"))
                        if (!playerDamages.isEmpty())
                            message = message.replace("%top1%", new ArrayList<>(playerDamages.keySet()).get(0));
                    if (message.contains("%top1_damage%") && !message.contains("'%top1_damage%'"))
                        if (!playerDamages.isEmpty())
                            message = message.replace("%top1_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(0))));
                    if (message.contains("%top2%") && !message.contains("'%top2%'"))
                        if (playerDamages.size() > 1)
                            message = message.replace("%top2%", new ArrayList<>(playerDamages.keySet()).get(1));
                    if (message.contains("%top2_damage%") && !message.contains("'%top2_damage%'"))
                        if (playerDamages.size() > 1)
                            message = message.replace("%top2_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(1))));
                    if (message.contains("%top3%") && !message.contains("'%top3%'"))
                        if (playerDamages.size() > 2)
                            message = message.replace("%top3%", new ArrayList<>(playerDamages.keySet()).get(2));
                    if (message.contains("%top3_damage%") && !message.contains("'%top3_damage%'"))
                        if (playerDamages.size() > 2)
                            message = message.replace("%top3_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(2))));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(p, message));
                }
            }
        }

        if (line.startsWith("[RANDOM]")) {
            int chance = Integer.parseInt(line.split(" ")[1]);
            String command = line.substring((line.split(" ")[0] + " " + chance + " ").length());
            if (new Random().nextDouble() * 100 < chance)
                executeScript(script, command, false, 0, extraVars);
        }
        if (line.startsWith("[MESSAGE]")) {
            if(currentEntity!=null) {
                int radius = Integer.parseInt(line.split(" ")[1]);
                String command = line.substring((line.split(" ")[0] + " " + radius + " ").length());
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!p.getLocation().getWorld().getName().equals(currentEntity.getLocation().getWorld().getName()))
                        continue;
                    if (p.getLocation().distance(currentEntity.getLocation()) < radius) {
                        String message = TextUtils.applyColor(PlaceholderAPI.setPlaceholders(p, command.replace("%player%", p.getName())
                                .replace("%name%", TextUtils.applyColor(name))));
                        if (extraVars != null) {
                            for (String arg : extraVars.keySet()) {
                                if (message.contains("%" + arg + "%")) {
                                    message = message.replace("%" + arg + "%", extraVars.get(arg));
                                }
                            }
                        }
                        if (command.startsWith("%center%"))
                            message = TextUtils.getCenteredMessage(message.replace("%center%", ""));
                        if (message.contains("%top1%") && !message.contains("'%top1%'"))
                            if (!playerDamages.isEmpty())
                                message = message.replace("%top1%", new ArrayList<>(playerDamages.keySet()).get(0));
                        if (message.contains("%top1_damage%") && !message.contains("'%top1_damage%'"))
                            if (!playerDamages.isEmpty())
                                message = message.replace("%top1_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(0))));
                        if (message.contains("%top2%") && !message.contains("'%top2%'"))
                            if (playerDamages.size() > 1)
                                message = message.replace("%top2%", new ArrayList<>(playerDamages.keySet()).get(1));
                        if (message.contains("%top2_damage%") && !message.contains("'%top2_damage%'"))
                            if (playerDamages.size() > 1)
                                message = message.replace("%top2_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(1))));
                        if (message.contains("%top3%") && !message.contains("'%top3%'"))
                            if (playerDamages.size() > 2)
                                message = message.replace("%top3%", new ArrayList<>(playerDamages.keySet()).get(2));
                        if (message.contains("%top3_damage%") && !message.contains("'%top3_damage%'"))
                            if (playerDamages.size() > 2)
                                message = message.replace("%top3_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(2))));
                        p.sendMessage(message);
                    }
                }
            }
        }
        if (line.startsWith("[ACTIONBAR]")) {
            if(currentEntity!=null) {
                int radius = Integer.parseInt(line.split(" ")[1]);
                String command = line.substring((line.split(" ")[0] + " " + radius + " ").length());
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!p.getLocation().getWorld().getName().equals(currentEntity.getLocation().getWorld().getName()))
                        continue;
                    if (p.getLocation().distance(currentEntity.getLocation()) < radius) {
                        String message =
                                TextUtils.applyColor(PlaceholderAPI.setPlaceholders(p, command.replace("%player%", p.getName())
                                        .replace("%name%", TextUtils.applyColor(name))));
                        if (extraVars != null) {
                            for (String arg : extraVars.keySet()) {
                                if (message.contains("%" + arg + "%")) {
                                    message = message.replace("%" + arg + "%", extraVars.get(arg));
                                }
                            }
                        }
                        if (command.startsWith("%center%"))
                            message = TextUtils.getCenteredMessage(message.replace("%center%", ""));
                        if (message.contains("%top1%") && !message.contains("'%top1%'"))
                            if (!playerDamages.isEmpty())
                                message = message.replace("%top1%", new ArrayList<>(playerDamages.keySet()).get(0));
                        if (message.contains("%top1_damage%") && !message.contains("'%top1_damage%'"))
                            if (!playerDamages.isEmpty())
                                message = message.replace("%top1_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(0))));
                        if (message.contains("%top2%") && !message.contains("'%top2%'"))
                            if (playerDamages.size() > 1)
                                message = message.replace("%top2%", new ArrayList<>(playerDamages.keySet()).get(1));
                        if (message.contains("%top2_damage%") && !message.contains("'%top2_damage%'"))
                            if (playerDamages.size() > 1)
                                message = message.replace("%top2_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(1))));
                        if (message.contains("%top3%") && !message.contains("'%top3%'"))
                            if (playerDamages.size() > 2)
                                message = message.replace("%top3%", new ArrayList<>(playerDamages.keySet()).get(2));
                        if (message.contains("%top3_damage%") && !message.contains("'%top3_damage%'"))
                            if (playerDamages.size() > 2)
                                message = message.replace("%top3_damage%", String.format("%.2f", playerDamages.get(new ArrayList<>(playerDamages.keySet()).get(2))));
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
                    }
                }
            }
        }
        if (line.startsWith("[SOUND]")) {
            var split = line.split(" ");
            Sound sound = Sound.valueOf(split[1].toUpperCase());
            float volume = Float.parseFloat(split[2]);
            float pitch = Float.parseFloat(split[3]);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playSound(p.getLocation(), sound, volume, pitch);
            }
        }
        if (line.startsWith("[SOUND_GLOBAL]")) {
            var split = line.split(" ");
            Sound sound = Sound.valueOf(split[1].toUpperCase());
            float volume = Float.parseFloat(split[2]);
            float pitch = Float.parseFloat(split[3]);
            spawnLocation.world.playSound(spawnLocation.toLocation(), sound, volume, pitch);
        }
        if (line.startsWith("[PLAYERSOUND]")) {

            if(currentEntity!=null) {
                var split = line.split(" ");
                int radius = Integer.parseInt(split[1]);
                Sound sound = Sound.valueOf(split[2].toUpperCase());
                float volume = Float.parseFloat(split[3]);
                float pitch = Float.parseFloat(split[4]);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!p.getLocation().getWorld().getName().equals(currentEntity.getLocation().getWorld().getName()))
                        continue;
                    if (p.getLocation().distance(currentEntity.getLocation()) < radius)
                        p.playSound(p.getLocation(), sound, volume, pitch);
                }
            }
        }
        if (line.startsWith("[COOLDOWN]")) {
            var split = line.split(" ");
            int radius = Integer.parseInt(split[1]);
            Material material = Material.valueOf(split[2].toUpperCase());
            int cooldown = Integer.parseInt(split[3]);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if(!p.getLocation().getWorld().getName().equals(currentEntity.getLocation().getWorld().getName())) continue;
                if (p.getLocation().distance(currentEntity.getLocation()) < radius)
                    p.setCooldown(material, cooldown);
            }
        }
        if (line.startsWith("[DELAY]")) {
            long delay = Long.parseLong(line.split(" ")[1]);
            StringBuilder lin = new StringBuilder(line.split(" ")[2]);
            for(int i = 3; i<line.split(" ").length; i++)
                lin.append(" ").append(line.split(" ")[i]);
            String finalLin = lin.toString();
            Bukkit.broadcastMessage(finalLin);
            BukkitRunnable run = new BukkitRunnable() {
                @Override
                public void run() {
                    executeScript(script, finalLin, false, index, extraVars);
                }
            };
            run.runTaskLater(MobsMain.getInstance(), delay);
            scriptRunners.put(UUID.randomUUID().toString(), run);
        }
        if(line.startsWith("[SPAWN]"))
            spawn();
        if(line.startsWith("[EFFECT]")){
            var split = line.split(" ");
            var type = PotionEffectType.getByName(split[1].toUpperCase());
            int duration = Integer.parseInt(split[2])*20;
            int amp = Integer.parseInt(split[3]);
            if(currentEntity!=null && type!=null){
                currentEntity.addPotionEffect(new PotionEffect(type, duration, amp));
            }
        }
        if (line.startsWith("[ANTIRELOG]")) {

            if (currentEntity != null) {
                var split = line.split(" ");
                int radius = Integer.parseInt(split[1]);
                List<Player> radiusPlayers = new ArrayList<>();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!p.getLocation().getWorld().getName().equals(currentEntity.getLocation().getWorld().getName()))
                        continue;
                    if (p.getLocation().distance(currentEntity.getLocation()) < radius) radiusPlayers.add(p);
                }
                Antirelog ar = (Antirelog) Bukkit.getPluginManager().getPlugin("AntiRelog");
                if (radiusPlayers.size() > 1) {
                    Player random = radiusPlayers.get(new Random().nextInt(radiusPlayers.size()));
                    for (Player p : radiusPlayers) {
                        if (random.getName().equals(p.getName())) continue;
                        if (ar != null) {
                            ar.getPvpManager().playerDamagedByPlayer(p, random);
                        }
                    }
                }
            }
        }
        if (line.startsWith("[REPEATBREAK]")) {
            String name = line.split(" ")[1];
            if (scriptRunners.containsKey(name))
                scriptRunners.get(name).cancel();
        }
        if(forEnabled){
            if (line.startsWith("[REPEAT]")) {
                int delay = Integer.parseInt(line.split(" ")[1]);
                String name = line.split(" ")[2];
                int stopIndex = findLoopEnd(index, script);
                if (stopIndex == -1) stopIndex = script.size() - 1;
//                Bukkit.broadcastMessage("Detected new repeat: " + name + " | Start: " + index + " | End: " + stopIndex);
                int finalStopIndex = stopIndex;
                BukkitRunnable run = new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (int i = index; i < finalStopIndex; i++)
                            executeScript(script, script.get(i), false, 0, extraVars);
                    }
                };
                run.runTaskTimer(MobsMain.getInstance(), 0, delay);
                scriptRunners.put(name, run);
            }
        }
    }
    private int findLoopEnd(int startIndex, List<String> script){
        for(int i = startIndex; i<script.size(); i++){
            if(script.get(i).startsWith("[REPEATEND]")) return i;
        }
        return -1;
    }
    private int findForLoopEnd(int startIndex, List<String> script){
        for(int i = startIndex; i<script.size(); i++){
            if(script.get(i).startsWith("[FOREND]")) return i;
        }
        return -1;
    }
    public Map<String, Double> playerDamages;
    public int health = 20;
    public EntityType entityType = EntityType.ZOMBIE;
    public Map<EquipmentSlot, ItemStack> equipment;
    public String name = "&7Boss";
    public LocationInstance spawnLocation;
    public List<String> preSpawnScript, spawnScript;
    public Map<String, List<String>> eventScripts;
    public Map<String, BukkitRunnable> scriptRunners;
    public Map<String, String> customValues;
    public BossBarInstance instance;
    public LivingEntity currentEntity = null;
    public String pluginName;

    public MobInstance(){
        playerDamages = new HashMap<>();
        preSpawnScript = new ArrayList<>();
        spawnScript = new ArrayList<>();
        eventScripts = new HashMap<>();
        scriptRunners = new HashMap<>();
        equipment = new HashMap<>();
        customValues = new HashMap<>();
    }

    public void prepare(){
        playerDamages.clear();
        customValues.clear();
        Bukkit.getScheduler().runTask(MobsMain.getInstance(), ()->executeScript(preSpawnScript, false, customValues));
    }
    public void spawn(){
        Bukkit.getScheduler().runTask(MobsMain.getInstance(), ()->{
            executeScript(spawnScript, false, customValues);
            if(entityType.isAlive())
            {
                currentEntity = (LivingEntity) spawnLocation.world.spawnEntity(spawnLocation.toLocation(), entityType);
                if(currentEntity instanceof Ageable)
                    ((Ageable) currentEntity).setAdult();
                for(Entity en: currentEntity.getPassengers()) {
                    currentEntity.removePassenger(en);
                    en.remove();
                }
                currentEntity.setAI(true);
                currentEntity.setMaximumAir(213213312);
                currentEntity.setRemoveWhenFarAway(false);
                currentEntity.setCustomName(TextUtils.applyColor(name));
                currentEntity.setCustomNameVisible(true);
                currentEntity.setCollidable(true);
                currentEntity.setCanPickupItems(false);
                currentEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
                currentEntity.setHealth(health);
                currentEntity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999999, 255,true, false));
                for(EquipmentSlot slot: equipment.keySet())
                    if(currentEntity.getEquipment()!=null)
                        currentEntity.getEquipment().setItem(slot, equipment.get(slot));
                if(instance!=null){
                    instance.despawn = false;
                }
            }
            else {
                Bukkit.getLogger().info(ChatColor.RED + "Cannot spawn this mob, because it's not alive!");
            }
        });
    }
    public void stop(){
        for(BukkitRunnable run: scriptRunners.values())
            run.cancel();
        if(currentEntity!=null) {
            currentEntity.remove();
            currentEntity = null;
        }
        instance.stop();
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void damage(EntityDamageByEntityEvent ev){
        if(currentEntity==null) return;
        if(ev.getDamager() instanceof Player) {
            if(!(ev.getEntity() instanceof LivingEntity)) return;
            var player = (Player) ev.getDamager();
            if (ev.getEntity().getUniqueId().equals(currentEntity.getUniqueId())) {
                playerDamages.put(player.getName(), playerDamages.containsKey(player.getName()) ? playerDamages.get(player.getName()) + ev.getDamage() : ev.getDamage());
                playerDamages = MapUtil.sortByValue(playerDamages);
                if (eventScripts.containsKey("damage")) {
                    Map<String, String> extra = new HashMap<>(customValues);
                    extra.put("damage", String.format("%.2f", ev.getDamage()));
                    extra.put("attacker", player.getName());
                    executeScript(eventScripts.get("damage"), false, extra);
                }
            }
        }
        else {
            if(ev.getEntity() instanceof Player) {
                if (ev.getDamager().getUniqueId().equals(currentEntity.getUniqueId())) {
                    if (eventScripts.containsKey("attack")) {
                        Map<String, String> extra = new HashMap<>(customValues);
                        executeScript(eventScripts.get("attack"), false, extra);
                    }
                }
            }
        }
    }

    @EventHandler
    public void kill(EntityDeathEvent ev){
        if(currentEntity==null) return;
        if(!(ev.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)) return;
        if(!(((EntityDamageByEntityEvent) ev.getEntity().getLastDamageCause()).getDamager() instanceof Player)) return;
        Player player = (Player) ((EntityDamageByEntityEvent) ev.getEntity().getLastDamageCause()).getDamager();
        if(!ev.getEntity().getUniqueId().equals(currentEntity.getUniqueId())) return;
        playerDamages = MapUtil.sortByValue(playerDamages);
        for(BukkitRunnable run: scriptRunners.values())
            run.cancel();
        instance.despawn = true;
        if(eventScripts.containsKey("death")) {
            Map<String, String> extra = new HashMap<>(customValues);
            extra.put("killer", player.getName());
            executeScript(eventScripts.get("death"), false, extra);
        }
        currentEntity = null;
    }
}
