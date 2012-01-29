package com.afforess.minecartmaniacore.event;

import org.bukkit.event.HandlerList;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartClickedEvent extends MinecartManiaEvent implements MinecartEvent {
    private static final long serialVersionUID = -546574030917262990L;
    boolean action = false;
    MinecartManiaMinecart minecart;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public MinecartClickedEvent(final MinecartManiaMinecart minecart) {
        super("MinecartClickedEvent");
        this.minecart = minecart;
    }
    
    public boolean isActionTaken() {
        return action;
    }
    
    public void setActionTaken(final boolean Action) {
        action = Action;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
    
}
