package com.afforess.minecartmaniacore.utils;

import org.bukkit.Location;

public class ComparableLocation extends Location {
    public ComparableLocation(final Location location) {
        super(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
    
    @Override
    public int hashCode() {
        return getBlock().hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Location)
            return getBlock().equals(((Location) obj).getBlock());
        return false;
    }
    
}
