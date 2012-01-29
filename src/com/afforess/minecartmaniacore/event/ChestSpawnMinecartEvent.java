package com.afforess.minecartmaniacore.event;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.afforess.minecartmaniacore.inventory.MinecartManiaChest;

public class ChestSpawnMinecartEvent extends MinecartManiaEvent implements Cancellable {
    private static final long serialVersionUID = 725637829972458807L;
    private final MinecartManiaChest chest;
    private Location spawnLocation;
    private boolean cancelled = false;
    private int type;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public ChestSpawnMinecartEvent(final MinecartManiaChest chest, final Location spawnLocation, final int type) {
        super("ChestPoweredEvent");
        this.chest = chest;
        this.spawnLocation = spawnLocation;
        this.type = type;
    }
    
    public MinecartManiaChest getChest() {
        return chest;
    }
    
    public Location getSpawnLocation() {
        return spawnLocation.clone();
    }
    
    public void setSpawnLocation(final Location l) {
        spawnLocation = l;
    }
    
    /**
     * The type of minecart to be spawned. 0 - Standard. 1 - Powered. 2 - Storage.
     * 
     * @return type.
     */
    public int getMinecartType() {
        return type;
    }
    
    /**
     * Sets the type of minecart to be spawned. 0 - Standard. 1 - Powered. 2 - Storage.
     */
    public void setMinecartType(final int type) {
        this.type = type;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public void setCancelled(final boolean cancel) {
        cancelled = cancel;
    }
    
}
