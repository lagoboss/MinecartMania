package com.afforess.minecartmaniacore.event;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartElevatorEvent extends MinecartManiaEvent implements Cancellable {
    
    private static final long serialVersionUID = -514535876514879428L;
    private final MinecartManiaMinecart minecart;
    private boolean cancelled = false;
    private Location location;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public MinecartElevatorEvent(final MinecartManiaMinecart minecart, final Location teleport) {
        super("MinecartElevatorEvent");
        this.minecart = minecart;
        location = teleport;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
    
    public Location getTeleportLocation() {
        return location.clone();
    }
    
    public void setTeleportLocation(final Location location) {
        this.location = location;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public void setCancelled(final boolean cancel) {
        cancelled = cancel;
    }
}