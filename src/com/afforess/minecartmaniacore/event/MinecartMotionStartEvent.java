package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartMotionStartEvent extends org.bukkit.event.Event{
	private MinecartManiaMinecart minecart;
	
	public MinecartMotionStartEvent(MinecartManiaMinecart cart) {
		super("MinecartMotionStartEvent");
		minecart = cart;
	}

	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}
}
