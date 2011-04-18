package com.afforess.minecartmaniacore.signs;

import org.bukkit.Location;

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

}
