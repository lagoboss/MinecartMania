package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartBrakeEvent extends org.bukkit.event.Event{
	private static final long serialVersionUID = 5975437042757982570L;
	double divisor;
	MinecartManiaMinecart minecart;
	
	public MinecartBrakeEvent(MinecartManiaMinecart minecart, double divisor) {
		super("MinecartBrakeEvent");
		this.minecart = minecart;
		this.divisor = divisor;
	}

	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}
	
	public double getBrakeDivisor() {
		return divisor;
	}
	
	public void setBrakeDivisor(double divisor) {
		this.divisor = divisor;
	}
}