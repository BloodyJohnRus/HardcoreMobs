package com.blxxdx.mobs.command;

import com.blxxdx.mobs.MobsMain;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, String[] strings) {
        if(strings.length>0){
            String subcmd = strings[0];
            if(subcmd.equals("spawn")){
                if(strings.length>1){
                    String name = strings[1];
                    if(MobsMain.getInstance().manager.instances.containsKey(name)) {
                        if (MobsMain.getInstance().manager.instances.get(name).currentEntity == null) {
                            MobsMain.getInstance().manager.instances.get(name).prepare();
                            if (commandSender instanceof Player)
                                commandSender.sendMessage(ChatColor.GREEN + "Spawned boss " + MobsMain.getInstance().manager.instances.get(name).name);
                        }
                        else
                            commandSender.sendMessage(ChatColor.RED + "Can't spawn another boss. Only one in a time!");
                    }
                }
            }
            if(subcmd.equals("tp")){
                if(strings.length>1) {
                    String name = strings[1];
                    if(MobsMain.getInstance().manager.instances.containsKey(name))
                        if(commandSender instanceof Player) {
                            try {
                                if(MobsMain.getInstance().manager.instances.get(name).currentEntity==null) {
                                    ((Player) commandSender).teleport(MobsMain.getInstance().manager.instances.get(name).spawnLocation.toLocation().add(0, 1, 0));
                                    commandSender.sendMessage(ChatColor.GREEN + "Teleported you to spawn location " + MobsMain.getInstance().manager.instances.get(name).name);
                                }
                                else {
                                    ((Player) commandSender).teleport(MobsMain.getInstance().manager.instances.get(name).currentEntity);
                                    commandSender.sendMessage(ChatColor.GREEN + "Teleported you to current location of " + MobsMain.getInstance().manager.instances.get(name).name);
                                }
                            }
                            catch (Exception ignored){
                                commandSender.sendMessage(ChatColor.RED + "Error while teleporting you. Probably, your world wasn't loaded!");
                                commandSender.sendMessage(ChatColor.RED + "Reload plugin. If it not works, it's lowkey ain't my fault.");
                            }
                        }
                }
            }
            if(subcmd.equals("reload")){
                long time = System.currentTimeMillis();
                MobsMain.getInstance().reload();
                commandSender.sendMessage(ChatColor.YELLOW + "Plugin was reloaded in " + (System.currentTimeMillis()-time) + " ms.");
            }
        }
        return true;
    }
}
