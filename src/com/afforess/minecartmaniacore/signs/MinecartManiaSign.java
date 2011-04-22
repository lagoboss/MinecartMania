package com.afforess.minecartmaniacore.signs;

import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.utils.ComparableLocation;
import com.afforess.minecartmaniacore.utils.DirectionUtils;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;
import com.afforess.minecartmaniacore.utils.StringUtils;
import com.afforess.minecartmaniacore.utils.WordUtils;

public class MinecartManiaSign implements Sign{
	protected final Location location;
	protected volatile String[] lines;
	protected HashSet<SignAction> actions = new HashSet<SignAction>();
	protected final ConcurrentHashMap<Object, Object> data = new ConcurrentHashMap<Object, Object>();
	
	public MinecartManiaSign(org.bukkit.block.Sign sign) {
		location = new ComparableLocation(sign.getBlock().getLocation());
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
		if (text.length() < 16) 
			lines[line] = text;
		else
			lines[line] = text.substring(0, 15);
		getSign().setLine(line, lines[line]);
		getSign().update();
	}
	
	public int getNumLines() {
		return lines.length;
	}
	
	public void addBrackets() {
		for (int i = 0; i < getNumLines(); i++) {
			if (!getLine(i).isEmpty()) {
				setLine(i, WordUtils.capitalize(StringUtils.addBrackets((getLine(i)))));
			}
		}
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
		actions = new HashSet<SignAction>();
	}
	
	private int hashCode(String[] lines) {
		int hash = getLocation().hashCode();
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

	@Override
	public void addSignAction(SignAction action) {
		actions.add(action);
	}

	@Override
	public boolean removeSignAction(SignAction action) {
		return actions.remove(action);
	}

	@Override
	public boolean hasSignAction(SignAction action) {
		return actions.contains(action);
	}
	
	@Override
	public boolean hasSignAction(Class<? extends SignAction> action) {
		Iterator<SignAction> i = actions.iterator();
		while(i.hasNext()){
			SignAction executor = i.next();
			if (action.isInstance(executor)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean executeActions(MinecartManiaMinecart minecart, boolean sync) {
		for (SignAction action : actions) {
			if (!sync && action.async()) {
				(new SignActionThread(minecart, action)).start();
			}
			else {
				action.execute(minecart);
			}
		}
		return actions.size() > 0;
	}

	@Override
	public boolean executeActions(MinecartManiaMinecart minecart) {
		return executeActions(minecart, false);
	}
	
	@Override
	public boolean executeAction(MinecartManiaMinecart minecart, Class<? extends SignAction> action) {
		Iterator<SignAction> i = actions.iterator();
		boolean success = false;
		while(i.hasNext()){
			SignAction executor = i.next();
			if (action.isInstance(executor)) {
				if (executor.execute(minecart)) {
					success = true;
				}
			}
		}
		return success;
	}


}
