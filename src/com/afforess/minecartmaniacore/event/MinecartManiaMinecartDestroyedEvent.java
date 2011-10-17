package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartManiaMinecartDestroyedEvent extends MinecartManiaEvent {
    private static final long serialVersionUID = 7543954124417132875L;
    private MinecartManiaMinecart minecart;
    
    public MinecartManiaMinecartDestroyedEvent(MinecartManiaMinecart cart) {
        super("MinecartManiaMinecartDestroyedEvent");
        minecart = cart;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
}
