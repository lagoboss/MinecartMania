package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartBoostEvent extends MinecartManiaEvent{
	private static final long serialVersionUID = 597543704275798255L;
	double multiplier;
	MinecartManiaMinecart minecart;
	
	public MinecartBoostEvent(MinecartManiaMinecart minecart, double multiplier) {
		super("MinecartBoostEvent");
		this.minecart = minecart;
		this.multiplier = multiplier;
	}

	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}
	
	public double getBoostMultiplier() {
		return multiplier;
	}
	
	public void setBoostMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}
}
