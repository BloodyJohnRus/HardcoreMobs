package com.blxxdx.mobs.instance;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationInstance {
    public double x, y, z;
    public World world;
    public LocationInstance(double a, double b, double c, String wrl){
        x = a;
        y = b;
        z = c;
        world = Bukkit.getWorld(wrl);
    }
    public Location toLocation(){
        return new Location(world, x, y, z, 0, 0);
    }
}
