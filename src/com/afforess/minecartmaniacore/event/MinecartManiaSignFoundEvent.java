package com.afforess.minecartmaniacore.event;

import org.bukkit.entity.Player;

import com.afforess.minecartmaniacore.signs.Sign;

public class MinecartManiaSignFoundEvent extends MinecartManiaEvent {
    private static final long serialVersionUID = -7633052520716796470L;
    private Sign sign;
    private final Player player;
    
    public MinecartManiaSignFoundEvent(final Sign sign, final Player player) {
        super("MinecartManiaSignFoundEvent");
        this.sign = sign;
        this.player = player;
    }
    
    public Sign getSign() {
        return sign;
    }
    
    public void setSign(final Sign sign) {
        this.sign = sign;
    }
    
    public Player getPlayer() {
        return player;
    }
    
}
