package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartMotionStopEvent extends org.bukkit.event.Event{
	private MinecartManiaMinecart minecart;
	
	public MinecartMotionStopEvent(MinecartManiaMinecart cart) {
		super("MinecartMotionStopEvent");
		minecart = cart;
	}

	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}
}
