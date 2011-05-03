package com.afforess.minecartmaniacore.signs;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

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

	@Override
	public boolean async() {
		return true;
	}

	@Override
	public boolean valid(Sign sign) {
		for (String line : sign.getLines()) {
			if (line.toLowerCase().contains("launch player")) {
				sign.addBrackets();
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "launchplayersign";
	}

	@Override
	public String getFriendlyName() {
		return "Launch Player Sign";
	}
	
	
}
