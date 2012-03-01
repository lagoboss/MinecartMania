package com.afforess.minecartmaniacore.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartSpeedMultiplierEvent extends MinecartManiaEvent implements Cancellable {
    
    private static final long serialVersionUID = -514535531614879428L;
    private final MinecartManiaMinecart minecart;
    private double multiplier;
    private final double origMultiplier;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public MinecartSpeedMultiplierEvent(final MinecartManiaMinecart minecart, final double multiplier) {
/* The MinecartmaniaEvent already know it's name
        super("MinecartSpeedAlterEvent");
*/
        super();
        this.minecart = minecart;
        this.multiplier = multiplier;
        origMultiplier = multiplier;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
    
    public double getSpeedMultiplier() {
        return multiplier;
    }
    
    public void setSpeedMultiplier(final double multiplier) {
        this.multiplier = multiplier;
    }
    
    public boolean isCancelled() {
        return multiplier == 1.0D;
    }
    
    public void setCancelled(final boolean cancelled) {
        if (cancelled) {
            multiplier = 1.0D;
        } else {
            multiplier = origMultiplier;
        }
    }
    
}
