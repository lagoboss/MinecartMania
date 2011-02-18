package com.afforess.minecartmaniacore.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaTaskScheduler;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.event.MinecartNearEntityEvent;


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
	
	public static void doMinecartNearEntityCheck(MinecartManiaMinecart minecart) {
		List<Entity> entities = minecart.minecart.getWorld().getEntities();
		ArrayList<MinecartNearEntityEvent> deadQueue = new ArrayList<MinecartNearEntityEvent>(50);
    	Vector location = minecart.minecart.getLocation().toVector();
    	int rangeSquared = minecart.getEntityDetectionRange() * minecart.getEntityDetectionRange();
    	for (Entity e : entities) {
			if (e.getLocation().toVector().distanceSquared(location) <= rangeSquared) {
				MinecartNearEntityEvent mnee = new MinecartNearEntityEvent(minecart, e);
				//by default drop arrows
				mnee.setCancelled(e instanceof Arrow);
				mnee.setDrop(e instanceof Arrow ? new ItemStack(Material.ARROW, 1) : null);
				MinecartManiaCore.server.getPluginManager().callEvent(mnee);
				//If cancelled, kill them once we are done calling events
				if (mnee.isCancelled()) {
					deadQueue.add(mnee);
				}
			}
    	}
    	
    	for (MinecartNearEntityEvent e : deadQueue) {
    		if (e.getDrop() != null) {
    			MinecartManiaWorld.dropItem(e.getEntity().getLocation(), e.getDrop());
    		}
    		MinecartManiaWorld.kill(e.getEntity());
    	}
	}
	
	public static void updateNearbyItems(MinecartManiaMinecart minecart) {
		Object[] param = { minecart };
		@SuppressWarnings("rawtypes")
		Class[] paramtype = { MinecartManiaMinecart.class };
		try {
			//No reason to keep this on the main thread, fire it on a second thread
			MinecartManiaTaskScheduler.doAsyncTask(MinecartUtils.class.getMethod("doMinecartNearEntityCheck", paramtype), 5, param);
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
		
}
