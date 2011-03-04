package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartActionEvent extends org.bukkit.event.Event implements MinecartEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1183514481986947096L;
	private boolean action = false;
	private MinecartManiaMinecart minecart;
	
	public MinecartActionEvent(MinecartManiaMinecart cart) {
		super("MinecartActionEvent");
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
