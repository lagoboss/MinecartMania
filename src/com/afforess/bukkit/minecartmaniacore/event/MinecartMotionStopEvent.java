package com.afforess.bukkit.minecartmaniacore.event;

import com.afforess.bukkit.minecartmaniacore.MinecartManiaMinecart;

public class MinecartMotionStopEvent extends org.bukkit.event.Event{
	private MinecartManiaMinecart minecart;
	
	public MinecartMotionStopEvent(MinecartManiaMinecart cart) {
		super("MinecartActionEvent");
		minecart = cart;
	}

	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}
}
