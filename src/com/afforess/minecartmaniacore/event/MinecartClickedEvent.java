package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartClickedEvent extends MinecartManiaEvent implements MinecartEvent{
	private static final long serialVersionUID = -546574030917262990L;
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
