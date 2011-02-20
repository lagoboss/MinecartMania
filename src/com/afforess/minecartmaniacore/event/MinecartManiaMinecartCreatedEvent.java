package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartManiaMinecartCreatedEvent extends org.bukkit.event.Event{

	private MinecartManiaMinecart minecart;
	
	public MinecartManiaMinecartCreatedEvent(MinecartManiaMinecart cart) {
		super("MinecartManiaMinecartCreatedEvent");
		minecart = cart;
	}

	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}
}
