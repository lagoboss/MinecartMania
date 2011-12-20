package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartClickedEvent extends MinecartManiaEvent implements MinecartEvent {
    private static final long serialVersionUID = -546574030917262990L;
    boolean action = false;
    MinecartManiaMinecart minecart;
    
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
