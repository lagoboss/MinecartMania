package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartMotionStartEvent extends org.bukkit.event.Event{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6990909608013578344L;
	private MinecartManiaMinecart minecart;
	
	public MinecartMotionStartEvent(MinecartManiaMinecart cart) {
		super("MinecartMotionStartEvent");
		minecart = cart;
	}

	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}
}
