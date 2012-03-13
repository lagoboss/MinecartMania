package com.afforess.minecartmaniacore.event;

import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartLaunchedEvent extends MinecartManiaEvent implements MinecartEvent {
    private final MinecartManiaMinecart minecart;
    private boolean action = false;
    private Vector launchSpeed;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public MinecartLaunchedEvent(final MinecartManiaMinecart cart, final Vector speed) {
        /*
         * The MinecartmaniaEvent already know it's name super("MinecartLaunchedEvent");
         */
        super();
        minecart = cart;
        launchSpeed = speed;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
    
    public Vector getLaunchSpeed() {
        return launchSpeed.clone();
    }
    
    public void setLaunchSpeed(final Vector speed) {
        launchSpeed = speed;
    }
    
    public boolean isActionTaken() {
        return action;
    }
    
    public void setActionTaken(final boolean Action) {
        action = Action;
    }
}
