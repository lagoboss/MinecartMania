package com.afforess.minecartmaniacore.event;

import org.bukkit.event.HandlerList;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartIntersectionEvent extends MinecartManiaEvent implements MinecartEvent {
    private static final long serialVersionUID = 207606044414759653L;
    private boolean action = false;
    private final MinecartManiaMinecart minecart;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public MinecartIntersectionEvent(final MinecartManiaMinecart cart) {
        super("MinecartIntersectionEvent");
        minecart = cart;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
    
    public boolean isActionTaken() {
        return action;
    }
    
    public void setActionTaken(final boolean b) {
        action = b;
    }
    
}
