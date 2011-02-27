package com.afforess.minecartmaniacore;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

public class MinecartManiaPlayer extends MinecartManiaSingleContainer implements MinecartManiaInventory{
	private final String name;
	private String lastStation = "";
	private ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<String,Object>();
	public MinecartManiaPlayer(String name) {
		super(MinecartManiaCore.server.getPlayer(name).getInventory());
		this.name = name;
	}
	
	/** 
	 * Returns the player that this MinecartManiaPlayer represents
	 * MinecartManiaPlayer' represent a player in whatever state they are in, online or offline. Because of this, getPlayer will return null when offline.
	 * @return player
	 */
	public Player getPlayer() {
		return MinecartManiaCore.server.getPlayer(name);
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isOnline() {
		return getPlayer() != null;
	}
	
	public String getLastStation() {
		return lastStation;
	}
	
	public void setLastStation(String s) {
		lastStation = s;
	}
	
	/**
	 ** Returns the value from the loaded data
	 ** @param the string key the data value is associated with
	 **/
	 public Object getDataValue(String key) {
		 if (data.containsKey(key)) {
			 return data.get(key);
		 }
		 return null;
	 }
	 
	/**
	 ** Creates a new data value if it does not already exists, or resets an existing value
	 ** @param the string key the data value is associated with
	 ** @param the value to store
	 **/	 
	 public void setDataValue(String key, Object value) {
		 if (value == null) {
			 data.remove(key);
		 }else {
			 data.put(key, value);
		 }
	 }
}
