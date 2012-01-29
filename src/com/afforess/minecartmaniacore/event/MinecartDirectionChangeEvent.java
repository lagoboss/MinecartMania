package com.afforess.minecartmaniacore.event;

import org.bukkit.event.HandlerList;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;

public class MinecartDirectionChangeEvent extends MinecartManiaEvent {
    private static final long serialVersionUID = -3750213642051820863L;
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
        super("MinecartDirectionChangeEvent");
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
