package com.afforess.bukkit.minecartmaniacore.event;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;

import com.afforess.bukkit.minecartmaniacore.MinecartManiaChest;

public class ChestSpawnMinecartEvent extends org.bukkit.event.Event implements Cancellable{
	private MinecartManiaChest chest;
	private Location spawnLocation;
	private boolean cancelled = false;
	
	public ChestSpawnMinecartEvent(MinecartManiaChest chest, Location spawnLocation) {
		super("ChestPoweredEvent");
		this.chest = chest;
		this.spawnLocation = spawnLocation;
	}
	
	public MinecartManiaChest getChest(){
		return chest;
	}
	
	public Location getSpawnLocation() {
		return spawnLocation.clone();
	}
	
	public void setSpawnLocation(Location l) {
		spawnLocation = l;
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
