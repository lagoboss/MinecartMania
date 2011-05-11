package com.afforess.minecartmaniacore.signs;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.event.MinecartManiaSignFoundEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaSignUpdatedEvent;
import com.afforess.minecartmaniacore.utils.ComparableLocation;

public class SignManager {
	private static ConcurrentHashMap<Location, Sign> signList = new ConcurrentHashMap<Location, Sign>();
	
	public static Sign getSignAt(Location loc) {
		return getSignAt(loc, null);
	}
	
	public static Sign getSignAt(Location loc, Player player) {
		if (!(loc.getBlock().getState() instanceof org.bukkit.block.Sign)) {
			return null;
		}
		if (!(loc instanceof ComparableLocation)) {
			loc = new ComparableLocation(loc);
		}
		Sign temp = signList.get(loc);
		if (temp != null) {
			org.bukkit.block.Sign sign = (org.bukkit.block.Sign)loc.getBlock().getState();
			if (!temp.equals(sign)) {
				temp.update(sign);
				MinecartManiaSignFoundEvent mmsfe = new MinecartManiaSignUpdatedEvent(temp, player);
				MinecartManiaCore.server.getPluginManager().callEvent(mmsfe);
				temp = mmsfe.getSign();
				signList.put(loc, temp);
			}
			return temp;
		}
		temp = new MinecartManiaSign(loc);
		MinecartManiaSignFoundEvent mmsfe = new MinecartManiaSignFoundEvent(temp, player);
		MinecartManiaCore.server.getPluginManager().callEvent(mmsfe);
		mmsfe.logProcessTime();
		temp = mmsfe.getSign();
		signList.put(loc, temp);
		return temp;
	}
	
	public static void updateSign(Location location, Sign sign) {
		if (sign == null) {
			signList.remove(location);
		}
		else {
			signList.put(sign.getLocation(), sign);
		}
	}

}
