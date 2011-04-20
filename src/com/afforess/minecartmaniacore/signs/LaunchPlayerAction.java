package com.afforess.minecartmaniacore.signs;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class LaunchPlayerAction implements SignAction{
	protected Sign sign;
	public LaunchPlayerAction(Sign sign) {
		this.sign = sign;
	}

	@Override
	public boolean execute(MinecartManiaMinecart minecart) {
		minecart.launchCart();
		minecart.setDataValue("hold sign data", null);
		return true;
	}
}
