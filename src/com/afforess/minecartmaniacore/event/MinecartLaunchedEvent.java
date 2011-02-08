package com.afforess.minecartmaniacore.event;

import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartLaunchedEvent extends org.bukkit.event.Event implements org.bukkit.event.Cancellable {
	private MinecartManiaMinecart minecart;
	private boolean cancel = false;
	private Vector launchSpeed;
	public MinecartLaunchedEvent(MinecartManiaMinecart cart, Vector speed) {
		super("MinecartLaunchedEvent");
		minecart = cart;
		launchSpeed = speed;
	}

	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
	
	public Vector getLaunchSpeed() {
		return launchSpeed.clone();
	}
	
	public void setLaunchSpeed(Vector speed) {
		launchSpeed = speed;
	}
}
