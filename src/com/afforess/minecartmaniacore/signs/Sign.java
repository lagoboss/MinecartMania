package com.afforess.minecartmaniacore.signs;

import org.bukkit.Location;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;

/**
 * A wrapper of a bukkit sign
 * 
 * @author Afforess
 */
public interface Sign {
	
	/**
	 * Get's the text from the given line of the sign
	 * @param line to get the text of
	 * @return the text from the sign
	 */
	public String getLine(int line);
	
	/**
	 * Set's the text of the given line of the sign
	 * @param line to get the text of
	 * @param text to set at the line
	 */
	public void setLine(int line, String text);
	
	/**
	 * Get's all the lines from the sign
	 * @return lines of the sign
	 */
	public String[] getLines();
	
	/**
	 * Get's the number of lines on the sign
	 * @return number of lines
	 */
	public int getNumLines();
	
	/**
	 * Adds brackets to this sign
	 */
	public void addBrackets();
	
	/**
	 * Get's the location of the sign
	 * @return location of the sign
	 */
	public Location getLocation();
	
	/**
	 * Get's the direction that this sign is facing
	 * @return direction the sign is facing
	 */
	public CompassDirection getFacingDirection();
	
	/**
	 * Get's the object (or null, if no object) associated with the given key
	 * @param key associated with the key
	 * @return object stored
	 */
	public Object getDataValue(Object key);
	
	/**
	 * Set's the object at the given key, or removes it if null is the value
	 * @param key to set the value at
	 * @param value to set
	 */
	public void setDataValue(Object key, Object value);
	
	/**
	 * Updates's the contents of the sign
	 * @param sign
	 */
	public void update(org.bukkit.block.Sign sign);
	
	/**
	 * Adds a sign action to the list of actions this sign must execute.
	 * @param action to add
	 */
	public void addSignAction(SignAction action);
	
	/**
	 * Removes a sign action from the list of actions this sign must exectue.
	 * @param action to remove.
	 * @return true if the action was removed.
	 */
	public boolean removeSignAction(SignAction action);
	
	/**
	 * Checks to see if this sign has this action attached to it
	 * @param action to check
	 * @return true if the sign has the given action
	 */
	public boolean hasSignAction(SignAction action);
	
	/**
	 * Executes all the actions attached to this sign.
	 * @param minecart to execute the actions for.
	 * @return true if at least one action was executed.
	 */
	public boolean executeActions(MinecartManiaMinecart minecart);

}
