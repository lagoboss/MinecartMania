package com.afforess.minecartmaniacore.signs;

import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class LaunchSign extends MinecartManiaSign implements ActionSign {

	private volatile Vector launchSpeed = null;
	public LaunchSign(Sign sign) {
		super(sign);
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
	
	@Override
	public void update(org.bukkit.block.Sign sign) {
		super.update(sign);
		calculateLaunchSpeed(true);
	}

	private Vector calculateLaunchSpeed(boolean force) {
		if (launchSpeed == null || force) {
			for (int i = 0; i < getNumLines(); i++) {
				if (getLine(i).toLowerCase().contains("launch north")) {
					launchSpeed = new Vector(-0.6D, 0, 0);
				}
				else if (getLine(i).toLowerCase().contains("launch east")) {
					launchSpeed = new Vector(0, 0, -0.6D);
				}
				if (getLine(i).toLowerCase().contains("launch south")) {
					launchSpeed = new Vector(0.6D, 0, 0);
				}
				if (getLine(i).toLowerCase().contains("launch west")) {
					launchSpeed = new Vector(0, 0, 0.6D);
				}
				if (getLine(i).toLowerCase().contains("previous dir")) {
					launchSpeed = null;
				}
			}
			this.addBrackets();
		}
		return launchSpeed;
	}

}
