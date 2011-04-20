package com.afforess.minecartmaniacore.signs;

import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class LaunchMinecartAction implements SignAction {
	private volatile Vector launchSpeed = null;
	protected Sign sign;
	public LaunchMinecartAction(Sign sign) {
		this.sign = sign;
	}

	@Override
	public boolean execute(MinecartManiaMinecart minecart) {
		Vector launch = calculateLaunchSpeed(false);
		if (launch == null) {
			minecart.setMotion(minecart.getPreviousDirectionOfMotion(), 0.6D);
		}
		else {
			minecart.minecart.setVelocity(launch);
		}
		
		return true;
	}

	private Vector calculateLaunchSpeed(boolean force) {
		if (launchSpeed == null || force) {
			for (int i = 0; i < sign.getNumLines(); i++) {
				if (sign.getLine(i).toLowerCase().contains("launch north")) {
					launchSpeed = new Vector(-0.6D, 0, 0);
				}
				else if (sign.getLine(i).toLowerCase().contains("launch east")) {
					launchSpeed = new Vector(0, 0, -0.6D);
				}
				if (sign.getLine(i).toLowerCase().contains("launch south")) {
					launchSpeed = new Vector(0.6D, 0, 0);
				}
				if (sign.getLine(i).toLowerCase().contains("launch west")) {
					launchSpeed = new Vector(0, 0, 0.6D);
				}
				if (sign.getLine(i).toLowerCase().contains("previous dir")) {
					launchSpeed = null;
				}
			}
			sign.addBrackets();
		}
		return launchSpeed;
	}

}
