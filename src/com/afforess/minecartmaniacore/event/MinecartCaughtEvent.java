package com.afforess.minecartmaniacore.event;

import org.bukkit.event.HandlerList;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartCaughtEvent extends MinecartManiaEvent implements MinecartEvent {
    private static final long serialVersionUID = 91568375046621L;
    private final MinecartManiaMinecart minecart;
    private boolean action = false;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public MinecartCaughtEvent(final MinecartManiaMinecart cart) {
        super("MinecartLaunchedEvent");
        minecart = cart;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
    
    public boolean isActionTaken() {
        return action;
    }
    
    public void setActionTaken(final boolean Action) {
        action = Action;
    }
}
