package com.afforess.minecartmaniacore.signs;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

/**
 * A sign that performs a specific action on a minecart (or it's passenger). 
 * @author Afforess
 */
public interface ActionSign extends Sign {
	
	/**
	 * Executes the action 
	 * @param minecart used in executing this action
	 * @return true if an action was exectued
	 */
	public boolean execute(MinecartManiaMinecart minecart);

}
