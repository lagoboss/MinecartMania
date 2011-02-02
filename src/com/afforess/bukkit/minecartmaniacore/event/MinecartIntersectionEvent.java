package com.afforess.bukkit.minecartmaniacore.event;

import com.afforess.bukkit.minecartmaniacore.MinecartManiaMinecart;

public class MinecartIntersectionEvent extends org.bukkit.event.Event{
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
