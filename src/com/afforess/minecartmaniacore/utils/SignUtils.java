package com.afforess.minecartmaniacore.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.config.MinecartManiaConfiguration;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.SignManager;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;

public class SignUtils {
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
	
	public static ArrayList<Sign> getAdjacentSignList(MinecartManiaMinecart minecart, int range, boolean force) {
		return getAdjacentSignList(minecart.minecart.getLocation(), range, force);
	}

	public static ArrayList<Sign> getAdjacentSignList(Location location, int range) {
		return getAdjacentSignList(location.getWorld(), location.getBlockX(), location.getBlockY()-1, location.getBlockZ(), range);
	}
	
	public static ArrayList<Sign> getAdjacentSignList(Location location, int range, boolean force) {
		return getAdjacentSignList(location.getWorld(), location.getBlockX(), location.getBlockY()-1, location.getBlockZ(), range);
	}
	
	public static ArrayList<Sign> getAdjacentSignList(World w, int x, int y, int z, int range) { 
		return getAdjacentSignList(w, x, y, z, range, false);
	}

	public static ArrayList<Sign> getAdjacentSignList(World w, int x, int y, int z, int range, boolean force) {
		ArrayList<Sign> signList = new ArrayList<Sign>();
		if (!force && MinecartManiaConfiguration.isLimitedSignRange()) {
			signList.addAll(getParallelSignList(w, x, y, z));
			signList.addAll(getSignBeneathList(w, x, y, z, 2));
			return signList;
		}
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
		return signList;
	}
	
	public static ArrayList<com.afforess.minecartmaniacore.signs.Sign> getAdjacentMinecartManiaSignList(Location location, int range) {
		ArrayList<Sign> list = getAdjacentSignList(location, range);
		ArrayList<com.afforess.minecartmaniacore.signs.Sign> signList = new ArrayList<com.afforess.minecartmaniacore.signs.Sign>(list.size());
		for (Sign s : list) {
			signList.add(SignManager.getSignAt(new Location(s.getWorld(), s.getX(), s.getY(), s.getZ())));
		}
		return signList;
	}
	
	public static ArrayList<com.afforess.minecartmaniacore.signs.Sign> getAdjacentMinecartManiaSignList(Location location, int range, boolean force) {
		ArrayList<Sign> list = getAdjacentSignList(location, range, force);
		ArrayList<com.afforess.minecartmaniacore.signs.Sign> signList = new ArrayList<com.afforess.minecartmaniacore.signs.Sign>(list.size());
		for (Sign s : list) {
			signList.add(SignManager.getSignAt(new Location(s.getWorld(), s.getX(), s.getY(), s.getZ())));
		}
		return signList;
	}
	
	public static ArrayList<com.afforess.minecartmaniacore.signs.Sign> getMinecartManiaSignBeneathList(Location location, int range) {
		ArrayList<Sign> list = getSignBeneathList(location, range);
		ArrayList<com.afforess.minecartmaniacore.signs.Sign> signList = new ArrayList<com.afforess.minecartmaniacore.signs.Sign>(list.size());
		for (Sign s : list) {
			signList.add(SignManager.getSignAt(new Location(s.getWorld(), s.getX(), s.getY(), s.getZ())));
		}
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
	
	public static void sortByDistance(final Location location, List<? extends com.afforess.minecartmaniacore.signs.Sign> signs) {
		Collections.sort(signs, new SignDistanceComparator(location));
	}
}

class SignDistanceComparator implements Comparator<com.afforess.minecartmaniacore.signs.Sign>
{
	private Location location;

	public SignDistanceComparator(Location location)
	{
		this.location = location;
	}

	protected double getSquaredDistanceFromLocation(com.afforess.minecartmaniacore.signs.Sign sign)
	{
		double x = sign.getLocation().getX() - location.getX();
		double y = sign.getLocation().getY() - location.getY();
		double z = sign.getLocation().getZ() - location.getZ();
		return x*x + y*y + z*z;
	}
	
	@Override
	public int compare(com.afforess.minecartmaniacore.signs.Sign sign1, com.afforess.minecartmaniacore.signs.Sign sign2)
	{
		double d1 = getSquaredDistanceFromLocation(sign1);
		double d2 = getSquaredDistanceFromLocation(sign1);
		
		// If the distance differs, threshold it and return.
		if (d1 != d2)
			return (int)Math.min(Math.max(d1 - d2,-1), 1);
		
		int d;
		
		// If the distance of two blocks is the same, sort them by x, then y, then z.
		// There's no particular reason for this, just that we don't want to claim 
		// that two different blocks are the same
		
		d = (sign1.getLocation().getBlockX() - sign2.getLocation().getBlockX());
		if (d != 0)
			return Math.min(Math.max(d, -1), 1);
		
		d = (sign1.getLocation().getBlockY() - sign2.getLocation().getBlockY());
		if (d != 0)
			return Math.min(Math.max(d, -1), 1);

		d = (sign1.getLocation().getBlockZ() - sign2.getLocation().getBlockZ());
		
		return Math.min(Math.max(d, -1), 1);
	}
	
}
