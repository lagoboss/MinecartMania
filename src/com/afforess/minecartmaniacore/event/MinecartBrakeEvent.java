package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartBrakeEvent extends MinecartManiaEvent{
	private static final long serialVersionUID = 597543704275798257L;
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