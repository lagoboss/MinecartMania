package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartIntersectionEvent extends MinecartManiaEvent implements MinecartEvent {
    private static final long serialVersionUID = 207606044414759653L;
    private boolean action = false;
    private final MinecartManiaMinecart minecart;
    
    public MinecartIntersectionEvent(final MinecartManiaMinecart cart) {
        super("MinecartIntersectionEvent");
        minecart = cart;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
    
    public boolean isActionTaken() {
        return action;
    }
    
    public void setActionTaken(final boolean b) {
        action = b;
    }
    
}
