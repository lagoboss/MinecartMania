package com.afforess.minecartmaniacore.signs;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;

import com.afforess.minecartmaniacore.MinecartManiaCore;
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
	protected int updateId = -1;
	protected ConcurrentHashMap<Object, Object> data = new ConcurrentHashMap<Object, Object>();
	
	public MinecartManiaSign(org.bukkit.block.Sign sign) {
		location = new ComparableLocation(sign.getBlock().getLocation());
		lines = getSign().getLines();
	}
	
	protected MinecartManiaSign(Location loc) {
		location = loc;
		lines = getSign().getLines();
	}
	
	protected final org.bukkit.block.Sign getSign() {
		return ((org.bukkit.block.Sign)location.getBlock().getState());
	}

	@Override
	public final String getLine(int line) {
		return lines[line];
	}

	@Override
	public final void setLine(int line, String text) {
		if (text.length() < 16) 
			lines[line] = text;
		else
			lines[line] = text.substring(0, 15);
		getSign().setLine(line, lines[line]);
		update();
	}
	
	@Override
	public final int getNumLines() {
		return lines.length;
	}
	
	@Override
	public void addBrackets() {
		for (int i = 0; i < getNumLines(); i++) {
			if (!getLine(i).isEmpty()) {
				setLine(i, WordUtils.capitalize(StringUtils.addBrackets((getLine(i)))));
			}
		}
	}

	@Override
	public final String[] getLines() {
		return lines;
	}

	@Override
	public final Location getLocation() {
		return location;
	}

	@Override
	public CompassDirection getFacingDirection() {
		return DirectionUtils.getSignFacingDirection(getSign());
	}

	@Override
	public final Object getDataValue(Object key) {
		return data.get(key);
	}

	@Override
	public final void setDataValue(Object key, Object value) {
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
	
	@Override
	public void copy(Sign sign) {
		if (sign instanceof MinecartManiaSign) {
			MinecartManiaSign temp = (MinecartManiaSign)sign;
			temp.data = this.data;
			temp.lines = this.lines;
			temp.actions = this.actions;
			update();
		}
		
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

	@SuppressWarnings("unchecked")
	@Override
	public Collection<SignAction> getSignActions() {
		return (Collection<SignAction>) actions.clone();
	}
	
	protected final void update() {
		if (this.updateId == -1) {
			this.updateId = MinecartManiaCore.server.getScheduler().scheduleSyncDelayedTask(MinecartManiaCore.instance, new SignTextUpdater(this.location), 5);
		}
	}
	
	public final void updated() {
		this.updateId = -1;
	}

}
