package com.blxxdx.mobs.instance;

import com.blxxdx.mobs.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BossBarInstance extends BukkitRunnable {
    public BossBar currentBossBar;
    private final MobInstance origin;
    public int radius;
    public boolean despawn = true;
    public BossBarInstance(MobInstance orig, int rad, String title, BarColor color, BarStyle style, BarFlag... flags){
        currentBossBar = Bukkit.createBossBar(TextUtils.applyColor(title), color, style, flags);
        radius = rad;
        origin = orig;
    }
    @Override
    public void run() {
        if(origin.currentEntity!=null) {
            currentBossBar.setProgress(origin.currentEntity.getHealth() / origin.health);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (origin.currentEntity.getLocation().getWorld().getName().equals(p.getWorld().getName())) {
                    if (origin.currentEntity.getLocation().distance(p.getLocation()) < radius) {
                        currentBossBar.addPlayer(p);
                    } else currentBossBar.removePlayer(p);
                }
            }
        }
        if(despawn) {
            currentBossBar.removeAll();
        }
    }
    public void stop(){
        despawn = true;
        currentBossBar.removeAll();
        cancel();
    }
}
