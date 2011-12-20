package com.afforess.minecartmaniacore.inventory;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.Inventory;

public class MinecartManiaBrewingStand extends MinecartManiaSingleContainer implements MinecartManiaInventory {
    
    private final Location location;
    private final ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<String, Object>();
    
    public MinecartManiaBrewingStand(final BrewingStand brewingStand) {
        super(brewingStand.getInventory());
        location = brewingStand.getBlock().getLocation();
    }
    
    public int getX() {
        return location.getBlockX();
    }
    
    public int getY() {
        return location.getBlockY();
    }
    
    public int getZ() {
        return location.getBlockZ();
    }
    
    public World getWorld() {
        return location.getWorld();
    }
    
    public Location getLocation() {
        return location;
    }
    
    public BrewingStand getBrewingStand() {
        return (BrewingStand) location.getBlock().getState();
    }
    
    /**
     ** Returns the value from the loaded data
     ** 
     * @param the string key the data value is associated with
     **/
    public Object getDataValue(final String key) {
        if (data.containsKey(key))
            return data.get(key);
        return null;
    }
    
    /**
     ** Creates a new data value if it does not already exists, or resets an existing value
     ** 
     * @param the string key the data value is associated with
     ** @param the value to store
     **/
    public void setDataValue(final String key, final Object value) {
        if (value == null) {
            data.remove(key);
        } else {
            data.put(key, value);
        }
    }
    
    @Override
    public Inventory getInventory() {
        return getBrewingStand().getInventory();
    }
}
