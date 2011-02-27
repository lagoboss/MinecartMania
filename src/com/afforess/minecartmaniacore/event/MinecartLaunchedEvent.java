package com.afforess.minecartmaniacore.event;

import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartLaunchedEvent extends org.bukkit.event.Event implements MinecartEvent {
	private MinecartManiaMinecart minecart;
	private boolean action = false;
	private Vector launchSpeed;
	public MinecartLaunchedEvent(MinecartManiaMinecart cart, Vector speed) {
		super("MinecartLaunchedEvent");
		minecart = cart;
		launchSpeed = speed;
	}

	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}
	
	public Vector getLaunchSpeed() {
		return launchSpeed.clone();
	}
	
	public void setLaunchSpeed(Vector speed) {
		launchSpeed = speed;
	}

	@Override
	public boolean isActionTaken() {
		return action;
	}

	@Override
	public void setActionTaken(boolean Action) {
		this.action = Action;
	}
}
