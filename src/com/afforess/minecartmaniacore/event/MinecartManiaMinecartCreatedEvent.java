package com.afforess.minecartmaniacore.event;

import org.bukkit.event.HandlerList;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartManiaMinecartCreatedEvent extends MinecartManiaEvent {
    private static final long serialVersionUID = 111867755114115637L;
    private final MinecartManiaMinecart minecart;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public MinecartManiaMinecartCreatedEvent(final MinecartManiaMinecart cart) {
/* The MinecartmaniaEvent already know it's name
        super("MinecartManiaMinecartCreatedEvent");
*/
        super();
        minecart = cart;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
}
