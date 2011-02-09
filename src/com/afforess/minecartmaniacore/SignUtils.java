package com.afforess.minecartmaniacore;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

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
	
	public static ArrayList<Sign> getParallelSignList(MinecartManiaMinecart minecart) {
		return getParallelSignList(minecart.minecart.getWorld(), minecart.getX(), minecart.getY(), minecart.getZ());
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
		return getSignBeneathList(minecart.minecart.getWorld(), minecart.getX(), minecart.getY()-1, minecart.getZ(), range);
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
