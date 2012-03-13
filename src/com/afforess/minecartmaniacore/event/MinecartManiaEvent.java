package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.debug.DebugTimer;

public abstract class MinecartManiaEvent extends org.bukkit.event.Event {
    private final DebugTimer timer;
    
    /*
     * Can/should not try to access the superclass private name. The constructor name parameter is useless as it is not settable. So call getEventName instead.
     * 
     * protected MinecartManiaEvent(final String name) { super(name); timer = new DebugTimer(name);
     */
    protected MinecartManiaEvent() {
        super();
        timer = new DebugTimer(getEventName());
    }
    
    public void logProcessTime() {
        timer.logProcessTime();
    }
}
