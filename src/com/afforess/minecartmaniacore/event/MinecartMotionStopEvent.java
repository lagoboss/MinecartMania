package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartMotionStopEvent extends org.bukkit.event.Event{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6645152664755993295L;
	private MinecartManiaMinecart minecart;
	
	public MinecartMotionStopEvent(MinecartManiaMinecart cart) {
		super("MinecartMotionStopEvent");
		minecart = cart;
	}

	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}
}
