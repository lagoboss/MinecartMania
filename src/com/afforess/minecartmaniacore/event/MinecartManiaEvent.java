package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.debug.DebugTimer;

@SuppressWarnings("serial")
public abstract class MinecartManiaEvent extends org.bukkit.event.Event{
	private final DebugTimer timer;

	protected MinecartManiaEvent(String name) {
		super(name);
		timer = new DebugTimer(name);
	}
	
	public void logProcessTime() {
		timer.logProcessTime();
	}
}
