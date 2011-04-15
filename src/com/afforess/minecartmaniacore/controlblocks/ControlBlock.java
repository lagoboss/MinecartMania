package com.afforess.minecartmaniacore.controlblocks;

import org.bukkit.Location;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public interface ControlBlock {
	
	public boolean valid(Location location);
	
	public boolean action(Location location, MinecartManiaMinecart minecart);
	
	public boolean action(Location location, MinecartManiaMinecart minecart, int delayTicks);

}
