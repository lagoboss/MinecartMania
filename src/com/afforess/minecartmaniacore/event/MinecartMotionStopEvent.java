package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartMotionStopEvent extends MinecartManiaEvent {
    private static final long serialVersionUID = 664515266475593295L;
    private final MinecartManiaMinecart minecart;
    
    public MinecartMotionStopEvent(final MinecartManiaMinecart cart) {
        super("MinecartMotionStopEvent");
        minecart = cart;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
}
