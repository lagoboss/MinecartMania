package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartMotionStartEvent extends MinecartManiaEvent {
    private static final long serialVersionUID = 699090908013578344L;
    private final MinecartManiaMinecart minecart;
    
    public MinecartMotionStartEvent(final MinecartManiaMinecart cart) {
        super("MinecartMotionStartEvent");
        minecart = cart;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
}
