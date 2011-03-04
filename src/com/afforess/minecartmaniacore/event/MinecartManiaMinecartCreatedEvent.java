package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartManiaMinecartCreatedEvent extends org.bukkit.event.Event{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1118677551141156375L;
	private MinecartManiaMinecart minecart;
	
	public MinecartManiaMinecartCreatedEvent(MinecartManiaMinecart cart) {
		super("MinecartManiaMinecartCreatedEvent");
		minecart = cart;
	}

	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}
}
