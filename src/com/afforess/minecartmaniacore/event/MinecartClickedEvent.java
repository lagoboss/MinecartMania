package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartClickedEvent extends org.bukkit.event.Event implements MinecartEvent{
	boolean action = false;
	MinecartManiaMinecart minecart;
	
	public MinecartClickedEvent(MinecartManiaMinecart minecart) {
		super("MinecartClickedEvent");
		this.minecart = minecart;
	}

	@Override
	public boolean isActionTaken() {
		return action;
	}

	@Override
	public void setActionTaken(boolean Action) {
		this.action = Action;
	}

	@Override
	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}

}
