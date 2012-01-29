package com.afforess.minecartmaniacore.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.afforess.minecartmaniacore.signs.Sign;

public class MinecartManiaSignUpdatedEvent extends MinecartManiaSignFoundEvent {
    private static final long serialVersionUID = 4073510449721123708L;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public MinecartManiaSignUpdatedEvent(final Sign sign, final Player player) {
        super(sign, player);
    }
    
}
