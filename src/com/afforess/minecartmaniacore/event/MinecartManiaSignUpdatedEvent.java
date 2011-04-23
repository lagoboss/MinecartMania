package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.signs.Sign;

public class MinecartManiaSignUpdatedEvent extends MinecartManiaSignFoundEvent{
	private static final long serialVersionUID = 4073510449721123708L;

	public MinecartManiaSignUpdatedEvent(Sign sign) {
		super(sign);
	}

}
