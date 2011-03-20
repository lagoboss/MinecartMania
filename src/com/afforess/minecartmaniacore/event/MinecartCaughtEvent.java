package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartCaughtEvent extends org.bukkit.event.Event implements MinecartEvent {
	
	private static final long serialVersionUID = 9156837504662L;
	private MinecartManiaMinecart minecart;
	private boolean action = false;
	public MinecartCaughtEvent(MinecartManiaMinecart cart) {
		super("MinecartLaunchedEvent");
		minecart = cart;
	}

	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}
	
	@Override
	public boolean isActionTaken() {
		return action;
	}

	@Override
	public void setActionTaken(boolean Action) {
		this.action = Action;
	}
}
