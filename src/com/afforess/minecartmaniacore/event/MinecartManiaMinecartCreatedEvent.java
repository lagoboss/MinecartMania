package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartManiaMinecartCreatedEvent extends MinecartManiaEvent {
    private static final long serialVersionUID = 111867755114115637L;
    private final MinecartManiaMinecart minecart;
    
    public MinecartManiaMinecartCreatedEvent(final MinecartManiaMinecart cart) {
        super("MinecartManiaMinecartCreatedEvent");
        minecart = cart;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
}
