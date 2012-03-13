package com.afforess.minecartmaniacore.event;

import org.bukkit.event.HandlerList;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;

public class MinecartDirectionChangeEvent extends MinecartManiaEvent {
    private final MinecartManiaMinecart minecart;
    private final CompassDirection previous;
    private final CompassDirection current;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public MinecartDirectionChangeEvent(final MinecartManiaMinecart minecart, final CompassDirection previous, final CompassDirection current) {
        /*
         * The MinecartmaniaEvent already know it's name super("MinecartDirectionChangeEvent");
         */
        super();
        this.minecart = minecart;
        this.previous = previous;
        this.current = current;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
    
    public CompassDirection getPreviousDirection() {
        return previous;
    }
    
    public CompassDirection getCurrentDirection() {
        return current;
    }
}
