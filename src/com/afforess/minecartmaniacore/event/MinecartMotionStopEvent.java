package com.afforess.minecartmaniacore.event;

import org.bukkit.event.HandlerList;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartMotionStopEvent extends MinecartManiaEvent {
    private static final long serialVersionUID = 664515266475593295L;
    private final MinecartManiaMinecart minecart;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public MinecartMotionStopEvent(final MinecartManiaMinecart cart) {
        super("MinecartMotionStopEvent");
        minecart = cart;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
}
