package com.afforess.minecartmaniacore.event;

import org.bukkit.event.Cancellable;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartKillEvent extends MinecartManiaEvent implements Cancellable {
    
    private static final long serialVersionUID = -5782963564694931261L;
    private boolean cancelled = false;
    private final MinecartManiaMinecart minecart;
    
    protected MinecartKillEvent(final MinecartManiaMinecart minecart) {
        super("MinecartKillEvent");
        this.minecart = minecart;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public void setCancelled(final boolean cancel) {
        cancelled = cancel;
    }
    
}
