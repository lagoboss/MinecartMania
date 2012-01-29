package com.afforess.minecartmaniacore.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartKillEvent extends MinecartManiaEvent implements Cancellable {
    
    private static final long serialVersionUID = -5782963564694931261L;
    private boolean cancelled = false;
    private final MinecartManiaMinecart minecart;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    protected MinecartKillEvent(final MinecartManiaMinecart minecart) {
        super("MinecartKillEvent");
        this.minecart = minecart;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public void setCancelled(final boolean cancel) {
        cancelled = cancel;
    }
    
}
