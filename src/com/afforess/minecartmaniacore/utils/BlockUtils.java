package com.afforess.minecartmaniacore.utils;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.afforess.minecartmaniacore.MinecartManiaWorld;

public class BlockUtils {
	
	public static ArrayList<Block> getAdjacentBlocks(Location location, int range) {
		//default constructor size is purely for efficiency reasons - and to show off my math skills
		ArrayList<Block> blockList = new ArrayList<Block>((int)Math.floor(Math.pow(1 + (range * 2), 3)));
		Block center = MinecartManiaWorld.getBlockAt(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
		for (int dx = -(range); dx <= range; dx++){
			for (int dy = -(range); dy <= range; dy++){
				for (int dz = -(range); dz <= range; dz++){
					blockList.add(center.getRelative(dx, dy, dz));
				}
			}
		}
		return blockList;
	}
	
	public static ArrayList<Block> getBlocksBeneath(Location location, int range) {
		ArrayList<Block> blockList = new ArrayList<Block>();
		for (int dy = -range; dy <= 0; dy++) {
			blockList.add(MinecartManiaWorld.getBlockAt(location.getWorld(), location.getBlockX(), location.getBlockY()+dy, location.getBlockZ()));
		}
		return blockList;
	}

}
