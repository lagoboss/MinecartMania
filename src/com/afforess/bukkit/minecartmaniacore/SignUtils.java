package com.afforess.bukkit.minecartmaniacore;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.block.Sign;

import com.afforess.bukkit.minecartmaniacore.MinecartManiaMinecart;

public class SignUtils {
	
	
	public static Sign getSignAt(World w, int x, int y, int z) {
		if (MinecartManiaWorld.getBlockAt(w, x, y, z).getState() instanceof Sign) {
			return (Sign)MinecartManiaWorld.getBlockAt(w, x, y, z).getState();
		}
		return null;
	}
	
	public static ArrayList<Sign> getAdjacentSignList(MinecartManiaMinecart minecart, int range) {
		return getAdjacentSignList(minecart.minecart.getWorld(), minecart.getX(), minecart.getY()-1, minecart.getZ(), range);
	}
	
	public static ArrayList<Sign> getAdjacentSignList(World w, int x, int y, int z, int range) {
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
		return signList;
	}
	
	public static ArrayList<Sign> getParallelSignList(World w, int x, int y, int z)
	{
		int range = 1;
		ArrayList<Sign> signList = new ArrayList<Sign>();
		for (int dx = -(range); dx <= range; dx++){
			for (int dz = -(range); dz <= range; dz++){
				Sign sign = getSignAt(w, x+dx, y, z+dz);
				if (sign != null) {
					signList.add(sign);
				}
			}
		}
		return signList;
	}
	
	
	
}
