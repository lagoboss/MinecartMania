package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartActionEvent extends MinecartManiaEvent implements MinecartEvent {
    private static final long serialVersionUID = 118351448198694709L;
    private boolean action = false;
    private final MinecartManiaMinecart minecart;
    
    public MinecartActionEvent(final MinecartManiaMinecart cart) {
        super("MinecartActionEvent");
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
