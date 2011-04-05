package com.afforess.minecartmaniacore.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

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

	public static void sortBlocksByDistance(final Location location, List<? extends BlockState> blocks)
	{
		Collections.sort(blocks, new BlockDistanceComparator(location));
	}
}

class BlockDistanceComparator implements Comparator<BlockState>
{
	private Location location;

	public BlockDistanceComparator(Location location)
	{
		this.location = location;
	}

	protected double getSquaredDistanceFromLocation(BlockState bs)
	{
		double x = bs.getX() - location.getX();
		double y = bs.getY() - location.getY();
		double z = bs.getZ() - location.getZ();
		return x*x + y*y + z*z;
	}
	
	@Override
	public int compare(BlockState bs1, BlockState bs2)
	{
		double d1 = getSquaredDistanceFromLocation(bs1);
		double d2 = getSquaredDistanceFromLocation(bs2);
		
		// If the distance differs, threshold it and return.
		if (d1 != d2)
			return (int)Math.min(Math.max(d1 - d2,-1), 1);
		
		int d;
		
		// If the distance of two blocks is the same, sort them by x, then y, then z.
		// There's no particular reason for this, just that we don't want to claim 
		// that two different blocks are the same
		
		d = (bs1.getX() - bs2.getX());
		if (d != 0)
			return Math.min(Math.max(d, -1), 1);
		
		d = (bs1.getY() - bs2.getY());
		if (d != 0)
			return Math.min(Math.max(d, -1), 1);

		d = (bs1.getZ() - bs2.getZ());
		
		return Math.min(Math.max(d, -1), 1);
	}
	
}