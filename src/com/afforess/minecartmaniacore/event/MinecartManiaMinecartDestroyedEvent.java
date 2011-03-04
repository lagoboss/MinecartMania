package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartManiaMinecartDestroyedEvent extends org.bukkit.event.Event{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7543954124417132875L;
	private MinecartManiaMinecart minecart;
	
	public MinecartManiaMinecartDestroyedEvent(MinecartManiaMinecart cart) {
		super("MinecartManiaMinecartDestroyedEvent");
		minecart = cart;
	}

	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}
}
