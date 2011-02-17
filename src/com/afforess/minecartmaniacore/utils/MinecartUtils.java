package com.afforess.minecartmaniacore.utils;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaTaskScheduler;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.event.MinecartNearItemDropEvent;


public class MinecartUtils {

	public static boolean isMinecartTrack(Block block) {
		if (block.getType().equals(Material.RAILS)) return true;
		if (MinecartManiaWorld.isPressurePlateRails()) {
			if (block.getType().equals(Material.STONE_PLATE)) return true;
			if (block.getType().equals(Material.WOOD_PLATE)) return true;
		}
		return false;
	}
	
	public static boolean validMinecartTrackAnyDirection(World w, int x, int y, int z, int range) {
		return validMinecartTrack(w, x, y, z, range, DirectionUtils.CompassDirection.NORTH) ||
			validMinecartTrack(w, x, y, z, range, DirectionUtils.CompassDirection.EAST) || 
			validMinecartTrack(w, x, y, z, range, DirectionUtils.CompassDirection.SOUTH) ||
			validMinecartTrack(w, x, y, z, range, DirectionUtils.CompassDirection.WEST);
	}
	
	public static boolean validMinecartTrack(World w, int x, int y, int z, int range, DirectionUtils.CompassDirection facingDir) {
    	if (!isMinecartTrack(MinecartManiaWorld.getBlockAt(w, x, y, z))) {
    		y--;
    		if (!isMinecartTrack(MinecartManiaWorld.getBlockAt(w, x, y, z))) {
    			return false;
    		}
    	}
    	range--;
    	while (range > 0) {
    		if (facingDir == DirectionUtils.CompassDirection.NORTH) x--;
    		if (facingDir == DirectionUtils.CompassDirection.EAST) z--;
    		if (facingDir == DirectionUtils.CompassDirection.SOUTH) x++;
    		if (facingDir == DirectionUtils.CompassDirection.WEST) z++;
    		if (isMinecartTrack(MinecartManiaWorld.getBlockAt(w, x, y-1, z))) y--;
    		if (isMinecartTrack(MinecartManiaWorld.getBlockAt(w, x, y+1, z))) y++;
    		if (!isMinecartTrack(MinecartManiaWorld.getBlockAt(w, x, y, z))) return false;
    		
    		if (isMinecartTrack(MinecartManiaWorld.getBlockAt(w, x-1, y, z))) facingDir = DirectionUtils.CompassDirection.NORTH;
    		if (isMinecartTrack(MinecartManiaWorld.getBlockAt(w, x, y, z-1))) facingDir = DirectionUtils.CompassDirection.EAST;
    		if (isMinecartTrack(MinecartManiaWorld.getBlockAt(w, x+1, y, z))) facingDir = DirectionUtils.CompassDirection.SOUTH;
    		if (isMinecartTrack(MinecartManiaWorld.getBlockAt(w, x, y, z+1))) facingDir = DirectionUtils.CompassDirection.WEST;
    		range--;
    	}
    	
    	return true;
    }
	
	public static boolean isAtIntersection(World w, int x, int y, int z) {
		int paths = 0;

		int data = MinecartManiaWorld.getBlockData(w, x, y, z);
		
		if (data == 0 || data == 1) {
			if (MinecartManiaWorld.getBlockAt(w, x, y, z-1).getType().equals(Material.RAILS)) {
				if (MinecartManiaWorld.getBlockData(w, x, y, z-1) == 0) {
					paths++;
				}
			}
			if (MinecartManiaWorld.getBlockAt(w, x, y, z+1).getType().equals(Material.RAILS)) {
				if (MinecartManiaWorld.getBlockData(w, x, y, z+1) == 0) {
					paths++;
				}
			}
			if (MinecartManiaWorld.getBlockAt(w, x-1, y, z).getType().equals(Material.RAILS)) {
				if (MinecartManiaWorld.getBlockData(w, x-1, y, z) == 1) {
					paths++;
				}
			}
			if (MinecartManiaWorld.getBlockAt(w, x+1, y, z).getType().equals(Material.RAILS)) {
				if (MinecartManiaWorld.getBlockData(w, x+1, y, z) == 1) {
					paths++;
				}
			}
		}
		
		else if (data == 6) {
			if (MinecartManiaWorld.getBlockAt(w, x+1, y, z).getType().equals(Material.RAILS) && MinecartManiaWorld.getBlockAt(w, x, y, z+1).getType().equals(Material.RAILS)) {
				if (MinecartManiaWorld.getBlockData(w, x+1, y, z) == 1 && MinecartManiaWorld.getBlockData(w, x, y, z+1) == 0) {
					paths = 2;
					if (MinecartManiaWorld.getBlockAt(w, x-1, y, z).getType().equals(Material.RAILS)) {
						if (MinecartManiaWorld.getBlockData(w, x-1, y, z) == 1) {
							paths++; 
						}
					}
					if (MinecartManiaWorld.getBlockAt(w, x, y, z-1).getType().equals(Material.RAILS)) {
						if (MinecartManiaWorld.getBlockData(w, x, y, z-1) == 0) {
							paths++;
						}
					}
				}
			}
		}
		else if (data == 7) {
			if (MinecartManiaWorld.getBlockAt(w, x-1, y, z).getType().equals(Material.RAILS) && MinecartManiaWorld.getBlockAt(w, x, y, z+1).getType().equals(Material.RAILS)) {
				if (MinecartManiaWorld.getBlockData(w, x-1, y, z) == 1 && MinecartManiaWorld.getBlockData(w, x, y, z+1) == 0) {
					paths = 2;
					if (MinecartManiaWorld.getBlockAt(w, x+1, y, z).getType().equals(Material.RAILS)) {
						if (MinecartManiaWorld.getBlockData(w, x+1, y, z) == 1) {
							paths++;
						}
					}
					if (MinecartManiaWorld.getBlockAt(w, x, y, z-1).getType().equals(Material.RAILS)) {
						if (MinecartManiaWorld.getBlockData(w, x, y, z-1) == 0) {
							paths++;
						}
					}
				}
			}
		}
		else if (data == 8) {
			if (MinecartManiaWorld.getBlockAt(w, x-1, y, z).getType().equals(Material.RAILS) && MinecartManiaWorld.getBlockAt(w, x, y, z-1).getType().equals(Material.RAILS)) {
				if (MinecartManiaWorld.getBlockData(w, x-1, y, z) == 1 && MinecartManiaWorld.getBlockData(w, x, y, z-1) == 0) {
					paths = 2;
					if (MinecartManiaWorld.getBlockAt(w, x+1, y, z).getType().equals(Material.RAILS)) {
						if (MinecartManiaWorld.getBlockData(w, x+1, y, z) == 1) {
							paths++;
						}
					}
					if (MinecartManiaWorld.getBlockAt(w, x, y, z+1).getType().equals(Material.RAILS)) {
						if (MinecartManiaWorld.getBlockData(w, x, y, z+1) == 0) {
							paths++;
						}
					}
				}
			}
		}
		else if (data == 9) {
			if (MinecartManiaWorld.getBlockAt(w, x+1, y, z).getType().equals(Material.RAILS) && MinecartManiaWorld.getBlockAt(w, x, y, z-1).getType().equals(Material.RAILS)) {
				if (MinecartManiaWorld.getBlockData(w, x+1, y, z) == 1 && MinecartManiaWorld.getBlockData(w, x, y, z-1) == 0) {
					paths = 2;
					if (MinecartManiaWorld.getBlockAt(w, x-1, y, z).getType().equals(Material.RAILS)) {
						if (MinecartManiaWorld.getBlockData(w, x-1, y, z) == 1) {
							paths++;
						}
					}
					if (MinecartManiaWorld.getBlockAt(w, x, y, z+1).getType().equals(Material.RAILS)) {
						if (MinecartManiaWorld.getBlockData(w, x, y, z+1) == 0) {
							paths++;
						}
					}
				}
			}
		}
		
		return paths > 2;
	}

	public static void testNearbyItems(MinecartManiaMinecart minecart) {
		List<Entity> entities = minecart.minecart.getWorld().getEntities();
    	Vector location = minecart.minecart.getLocation().toVector();
    	int rangeSquared = 4;
    	if (MinecartManiaWorld.getConfigurationValue("Nearby Items Range") != null) {
    		rangeSquared = MinecartManiaWorld.getIntValue(MinecartManiaWorld.getConfigurationValue("Nearby Items Range"));
    		rangeSquared = rangeSquared * rangeSquared;
    	}
    	for (Entity e : entities) {
    		if (e instanceof Item) {
    			if (e.getLocation().toVector().distanceSquared(location) <= rangeSquared) {
    				Object[] param = { new MinecartNearItemDropEvent(minecart, (Item)e) };
    				@SuppressWarnings("rawtypes")
					Class[] paramtype = { Event.class };
    				try {
    					//No reason to keep this on the main thread, fire it on a second thread
						MinecartManiaTaskScheduler.doAsyncTask(PluginManager.class.getMethod("callEvent", paramtype), MinecartManiaCore.server.getPluginManager(), param);
					} catch (SecurityException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NoSuchMethodException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
    			}
    		}
    	}
	}
}
