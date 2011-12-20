package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartCaughtEvent extends MinecartManiaEvent implements MinecartEvent {
    private static final long serialVersionUID = 91568375046621L;
    private final MinecartManiaMinecart minecart;
    private boolean action = false;
    
    public MinecartCaughtEvent(final MinecartManiaMinecart cart) {
        super("MinecartLaunchedEvent");
        minecart = cart;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
    
    public boolean isActionTaken() {
        return action;
    }
    
    public void setActionTaken(final boolean Action) {
        action = Action;
    }
}
