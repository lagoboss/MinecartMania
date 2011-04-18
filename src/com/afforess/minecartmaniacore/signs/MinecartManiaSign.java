package com.afforess.minecartmaniacore.signs;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;

import com.afforess.minecartmaniacore.utils.ComparableLocation;
import com.afforess.minecartmaniacore.utils.DirectionUtils;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;
import com.afforess.minecartmaniacore.utils.StringUtils;
import com.afforess.minecartmaniacore.utils.WordUtils;

public class MinecartManiaSign implements Sign {
	protected final Location location;
	protected volatile String[] lines;
	protected final ConcurrentHashMap<Object, Object> data = new ConcurrentHashMap<Object, Object>();
	
	public MinecartManiaSign(Sign sign) {
		location = new ComparableLocation(sign.getLocation());
		lines = getSign().getLines();
	}
	
	protected MinecartManiaSign(Location loc) {
		location = loc;
		lines = getSign().getLines();
	}
	
	protected org.bukkit.block.Sign getSign() {
		return ((org.bukkit.block.Sign)location.getBlock().getState());
	}

	@Override
	public String getLine(int line) {
		return lines[line];
	}

	@Override
	public void setLine(int line, String text) {
		lines[line] = text;
		getSign().setLine(line, text);
		getSign().update();
	}
	
	public int getNumLines() {
		return lines.length;
	}
	
	public void addBrackets() {
		for (int i = 0; i < getNumLines(); i++) {
			if (!getLine(i).isEmpty()) {
				getSign().setLine(i, WordUtils.capitalize(StringUtils.addBrackets((getLine(i)))));
			}
		}
		getSign().update();
	}

	@Override
	public String[] getLines() {
		return lines;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public CompassDirection getFacingDirection() {
		return DirectionUtils.getSignFacingDirection(getSign());
	}

	@Override
	public Object getDataValue(Object key) {
		return data.get(key);
	}

	@Override
	public void setDataValue(Object key, Object value) {
		if (value != null) {
			data.put(key, value);
		}
		else {
			data.remove(key);
		}
	}
	
	@Override
	public void update(org.bukkit.block.Sign sign) {
		lines = sign.getLines();
	}
	
	private static int hashCode(String[] lines) {
		int hash = 0;
		for (int i = 0; i < lines.length; i++) {
			if (!lines[i].isEmpty()) {
				hash += lines[i].hashCode();
			}
		}
		return hash;
	}
	
	@Override
	public int hashCode() {
		return hashCode(lines);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Sign) {
			return hashCode() == ((Sign)obj).hashCode();
		}
		else if (obj instanceof org.bukkit.block.Sign) {
			return hashCode() == hashCode(((org.bukkit.block.Sign)obj).getLines());
		}
		return false;
	}
}
