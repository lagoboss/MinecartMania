package com.afforess.minecartmaniacore.signs;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

/**
 * An action specific to a sign
 * @author Afforess
 */
public interface SignAction{
	
	/**
	 * Executes the action 
	 * @param minecart used in executing this action
	 * @return true if an action was exectued
	 */
	public boolean execute(MinecartManiaMinecart minecart);
}
