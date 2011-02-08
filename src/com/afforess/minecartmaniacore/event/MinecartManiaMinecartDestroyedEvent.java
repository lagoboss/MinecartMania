package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartManiaMinecartDestroyedEvent  extends org.bukkit.event.Event{

	private MinecartManiaMinecart minecart;
	
	public MinecartManiaMinecartDestroyedEvent(MinecartManiaMinecart cart) {
		super("MinecartManiaMinecartDestroyedEvent");
		minecart = cart;
	}

	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}
}
