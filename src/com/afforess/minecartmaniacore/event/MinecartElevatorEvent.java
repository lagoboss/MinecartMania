package com.afforess.minecartmaniacore.event;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartElevatorEvent extends MinecartManiaEvent implements Cancellable{

	private static final long serialVersionUID = -514535876514879428L;
	private MinecartManiaMinecart minecart;
	private boolean cancelled = false;
	private Location location;
	public MinecartElevatorEvent(MinecartManiaMinecart minecart, Location teleport) {
		super("MinecartElevatorEvent");
		this.minecart = minecart;
		this.location = teleport;
	}

	public MinecartManiaMinecart getMinecart() {
		return this.minecart;
	}
	
	public Location getTeleportLocation() {
		return location.clone();
	}
	
	public void setTeleportLocation(Location location) {
		this.location = location;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}