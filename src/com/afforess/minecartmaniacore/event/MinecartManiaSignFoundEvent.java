package com.afforess.minecartmaniacore.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.afforess.minecartmaniacore.signs.Sign;

public class MinecartManiaSignFoundEvent extends MinecartManiaEvent {
    private Sign sign;
    private final Player player;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public MinecartManiaSignFoundEvent(final Sign sign, final Player player) {
        /*
         * The MinecartmaniaEvent already know it's name super("MinecartManiaSignFoundEvent");
         */
        super();
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
