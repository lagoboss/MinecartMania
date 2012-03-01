package com.afforess.minecartmaniacore.event;

import org.bukkit.event.HandlerList;

import com.afforess.minecartmaniacore.inventory.MinecartManiaChest;

public class ChestPoweredEvent extends MinecartManiaEvent {
    private static final long serialVersionUID = 458967445158658060L;
    private final MinecartManiaChest chest;
    private final boolean powered;
    private final boolean wasPowered;
    private boolean action = false;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public ChestPoweredEvent(final MinecartManiaChest chest, final boolean powered) {
/* The MinecartmaniaEvent already know it's name
        super("ChestPoweredEvent");
*/
        super();
        this.chest = chest;
        this.powered = powered;
        wasPowered = chest.isRedstonePower();
    }
    
    public MinecartManiaChest getChest() {
        return chest;
    }
    
    public boolean isPowered() {
        return powered;
    }
    
    public boolean wasPowered() {
        return wasPowered;
    }
    
    public boolean isActionTaken() {
        return action;
    }
    
    public void setActionTaken(final boolean b) {
        action = b;
    }
}
