package com.afforess.minecartmaniacore.utils;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaWorld;

public class SignUtils {
	private static Location cacheLoc = null;
	private static ArrayList<Sign> cache = null;
	private static int cacheRange = -1;
	
	//While it's reset often, tests indicate it is used in 2/5 of calls for standard minecarts, and 19/20 of calls for storage minecarts.
	private static ArrayList<Sign> getCache(Location loc, int range) {
		if (cacheLoc != null && loc.toVector().equals(cacheLoc.toVector())) {
			if (range == cacheRange) {
				return cache;
			}
		}
		return null;
	}
	
	private static void setCache(ArrayList<Sign> signList, Location loc, int range) {
		SignUtils.cacheLoc = loc;
		SignUtils.cacheRange = range;
		SignUtils.cache = signList;
	}
	
	public static boolean signMatches(Sign s1, Sign s2) {
		return s1.getBlock().getLocation().equals(s2.getBlock().getLocation());
	}

	public static Sign getSignAt(World w, int x, int y, int z) {
		if (MinecartManiaWorld.getBlockAt(w, x, y, z).getState() instanceof Sign) {
			return (Sign)MinecartManiaWorld.getBlockAt(w, x, y, z).getState();
		}
		return null;
	}

	public static ArrayList<Sign> getAdjacentSignList(MinecartManiaMinecart minecart, int range) {
		return getAdjacentSignList(minecart.minecart.getLocation(), range);
	}

	public static ArrayList<Sign> getAdjacentSignList(Location location, int range) {
		return getAdjacentSignList(location.getWorld(), location.getBlockX(), location.getBlockY()-1, location.getBlockZ(), range);
	}

	public static ArrayList<Sign> getAdjacentSignList(World w, int x, int y, int z, int range) {
		//Attempt using the cache

		Location loc = new Location(w, x, y, z);
		if (getCache(loc, range) != null) {
			return getCache(loc, range);
		}
		
		ArrayList<Sign> signList = new ArrayList<Sign>();
		for (int dx = -(range); dx <= range; dx++){
			for (int dy = -(range); dy <= range; dy++){
				for (int dz = -(range); dz <= range; dz++){
					Sign sign = getSignAt(w, x+dx, y+dy, z+dz);
					if (sign != null) {
						signList.add(sign);
					}
				}
			}
		}
		
		setCache(signList, loc, range);
		
		return signList;
	}

	public static ArrayList<Sign> getParallelSignList(MinecartManiaMinecart minecart) {
		return getParallelSignList(minecart.minecart.getLocation());
	}

	public static ArrayList<Sign> getParallelSignList(Location location) {
		return getParallelSignList(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	public static ArrayList<Sign> getParallelSignList(World w, int x, int y, int z){
		int range = 1;
		ArrayList<Sign> signList = new ArrayList<Sign>();
		for (int dx = -(range); dx <= range; dx++){
			Sign sign = getSignAt(w, x+dx, y, z);
			if (sign != null) {
				signList.add(sign);
			}
		}
		for (int dz = -(range); dz <= range; dz++){
			Sign sign = getSignAt(w, x, y, z+dz);
			if (sign != null) {
				signList.add(sign);
			}
		}
		return signList;
	}

	public static ArrayList<Sign> getSignBeneathList(MinecartManiaMinecart minecart, int range) {
		return getSignBeneathList(minecart.minecart.getLocation(), range);
	}

	public static ArrayList<Sign> getSignBeneathList(Location location, int range) {
		return getSignBeneathList(location.getWorld(), location.getBlockX(), location.getBlockY()-1, location.getBlockZ(), range);
	}

	public static ArrayList<Sign> getSignBeneathList(World w, int x, int y, int z, int range) {
		ArrayList<Sign> signList = new ArrayList<Sign>();

		for (int dy = -range; dy <= 0; dy++) {
			Sign sign = getSignAt(w, x, y+dy, z);
			if (sign != null) {
				signList.add(sign);
			}
		}
		return signList;
	}
}
