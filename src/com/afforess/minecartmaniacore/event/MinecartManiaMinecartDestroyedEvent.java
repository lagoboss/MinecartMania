package com.afforess.minecartmaniacore.event;

import org.bukkit.event.HandlerList;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartManiaMinecartDestroyedEvent extends MinecartManiaEvent {
    private static final long serialVersionUID = 7543954124417132875L;
    private final MinecartManiaMinecart minecart;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public MinecartManiaMinecartDestroyedEvent(final MinecartManiaMinecart cart) {
/* The MinecartmaniaEvent already know it's name
        super("MinecartManiaMinecartDestroyedEvent");
*/
        super();
        minecart = cart;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
}
