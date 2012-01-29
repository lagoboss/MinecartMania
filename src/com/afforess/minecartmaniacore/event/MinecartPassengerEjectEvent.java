package com.afforess.minecartmaniacore.event;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartPassengerEjectEvent extends MinecartManiaEvent implements Cancellable {
    private static final long serialVersionUID = 7082195004734423157L;
    private final MinecartManiaMinecart minecart;
    private final Entity passenger;
    private boolean cancelled = false;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public MinecartPassengerEjectEvent(final MinecartManiaMinecart minecart, final Entity passenger) {
        super("MinecartPassengerEjectEvent");
        this.minecart = minecart;
        this.passenger = passenger;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public void setCancelled(final boolean cancel) {
        cancelled = cancel;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
    
    public Entity getPassenger() {
        return passenger;
    }
    
}
