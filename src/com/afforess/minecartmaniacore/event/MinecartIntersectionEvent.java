package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartIntersectionEvent extends org.bukkit.event.Event implements MinecartEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2076060444147596537L;
	private boolean action = false;
	private MinecartManiaMinecart minecart;
	
	public MinecartIntersectionEvent(MinecartManiaMinecart cart) {
		super("MinecartIntersectionEvent");
		minecart = cart;
	}
	
	
	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}
	
	public boolean isActionTaken() {
		return action;
	}
	
	public void setActionTaken(boolean b) {
		action = b;
	}

}
