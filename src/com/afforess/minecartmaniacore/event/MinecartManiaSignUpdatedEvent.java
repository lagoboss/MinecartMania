package com.afforess.minecartmaniacore.event;

import org.bukkit.entity.Player;

import com.afforess.minecartmaniacore.signs.Sign;

public class MinecartManiaSignUpdatedEvent extends MinecartManiaSignFoundEvent {
    private static final long serialVersionUID = 4073510449721123708L;
    
    public MinecartManiaSignUpdatedEvent(Sign sign, Player player) {
        super(sign, player);
    }
    
}
