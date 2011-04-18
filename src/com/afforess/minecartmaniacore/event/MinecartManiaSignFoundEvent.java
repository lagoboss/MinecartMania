package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.signs.Sign;

public class MinecartManiaSignFoundEvent extends MinecartManiaEvent{
	private static final long serialVersionUID = -7633052520716796470L;
	private Sign sign;

	public MinecartManiaSignFoundEvent(Sign sign) {
		super("MinecartManiaSignFoundEvent");
		this.sign = sign;
	}
	
	public Sign getSign() {
		return sign;
	}
	
	public void setSign(Sign sign) {
		this.sign = sign;
	}

}
