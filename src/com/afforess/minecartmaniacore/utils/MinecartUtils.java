package com.afforess.minecartmaniacore.utils;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.Item;
import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaTaskScheduler;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.event.MinecartNearEntityEvent;


public class MinecartUtils {
	
	
	/**
	 * @deprecated
	 * Will be removed by 15/3/11
	 */
	public static boolean isMinecartTrack(Block block) {
		return block.getTypeId() == Material.RAILS.getId();
	}
	
	public static boolean validMinecartTrackAnyDirection(World w, int x, int y, int z, int range) {
		return validMinecartTrack(w, x, y, z, range, DirectionUtils.CompassDirection.NORTH) ||
			validMinecartTrack(w, x, y, z, range, DirectionUtils.CompassDirection.EAST) || 
			validMinecartTrack(w, x, y, z, range, DirectionUtils.CompassDirection.SOUTH) ||
			validMinecartTrack(w, x, y, z, range, DirectionUtils.CompassDirection.WEST);
	}
	
	public static boolean isSlopedTrack(World w, int x, int y, int z) {
		int data = MinecartManiaWorld.getBlockData(w, x, y, z);
		return data >= 0x2 && data <= 0x5;
	}
	
	
	//TODO this method is not a perfect detection of track. It will give false positives for having 2 sets of parallel track, and when double curves are used
	public static boolean validMinecartTrack(World w, int x, int y, int z, int range, DirectionUtils.CompassDirection facingDir) {
    	if (MinecartManiaWorld.getBlockAt(w, x, y, z).getTypeId() != Material.RAILS.getId()) {
    		y--;
    		if (MinecartManiaWorld.getBlockAt(w, x, y, z).getTypeId() != Material.RAILS.getId()) {
    			return false;
    		}
    	}
    	range--;
    	while (range > 0) {
    		if (facingDir == DirectionUtils.CompassDirection.NORTH) x--;
    		if (facingDir == DirectionUtils.CompassDirection.EAST) z--;
    		if (facingDir == DirectionUtils.CompassDirection.SOUTH) x++;
    		if (facingDir == DirectionUtils.CompassDirection.WEST) z++;
    		if (MinecartManiaWorld.getBlockAt(w, x, y-1, z).getTypeId() == Item.RAILS.getId()) y--;
    		if (MinecartManiaWorld.getBlockAt(w, x, y+1, z).getTypeId() == Item.RAILS.getId()) y++;
    		if (MinecartManiaWorld.getBlockAt(w, x, y, z).getTypeId() != Item.RAILS.getId()) return false;
    		
    		if (MinecartManiaWorld.getBlockAt(w, x-1, y, z).getTypeId() == Item.RAILS.getId()) facingDir = DirectionUtils.CompassDirection.NORTH;
    		if (MinecartManiaWorld.getBlockAt(w, x, y, z-1).getTypeId() == Item.RAILS.getId()) facingDir = DirectionUtils.CompassDirection.EAST;
    		if (MinecartManiaWorld.getBlockAt(w, x+1, y, z).getTypeId() == Item.RAILS.getId()) facingDir = DirectionUtils.CompassDirection.SOUTH;
    		if (MinecartManiaWorld.getBlockAt(w, x, y, z-1).getTypeId() == Item.RAILS.getId()) facingDir = DirectionUtils.CompassDirection.WEST;
    		range--;
    	}
    	
    	return true;
    }
	
	public static boolean isAtIntersection(World w, int x, int y, int z) {
		int paths = 0;

		int data = MinecartManiaWorld.getBlockData(w, x, y, z);
		
		if (data == 0 || data == 1) {
			if (MinecartManiaWorld.getBlockAt(w, x, y, z-1).getTypeId() == Item.RAILS.getId()) {
				if (MinecartManiaWorld.getBlockData(w, x, y, z-1) == 0) {
					paths++;
				}
			}
			if (MinecartManiaWorld.getBlockAt(w, x, y, z+1).getTypeId() == Item.RAILS.getId()) {
				if (MinecartManiaWorld.getBlockData(w, x, y, z+1) == 0) {
					paths++;
				}
			}
			if (MinecartManiaWorld.getBlockAt(w, x-1, y, z).getTypeId() == Item.RAILS.getId()) {
				if (MinecartManiaWorld.getBlockData(w, x-1, y, z) == 1) {
					paths++;
				}
			}
			if (MinecartManiaWorld.getBlockAt(w, x+1, y, z).getTypeId() == Item.RAILS.getId()) {
				if (MinecartManiaWorld.getBlockData(w, x+1, y, z) == 1) {
					paths++;
				}
			}
		}
		
		else if (data == 6) {
			if (MinecartManiaWorld.getBlockAt(w, x+1, y, z).getTypeId() == Item.RAILS.getId() && MinecartManiaWorld.getBlockAt(w, x, y, z+1).getTypeId() == Item.RAILS.getId()) {
				if (MinecartManiaWorld.getBlockData(w, x+1, y, z) == 1 && MinecartManiaWorld.getBlockData(w, x, y, z+1) == 0) {
					paths = 2;
					if (MinecartManiaWorld.getBlockAt(w, x-1, y, z).getTypeId() == Item.RAILS.getId()) {
						if (MinecartManiaWorld.getBlockData(w, x-1, y, z) == 1) {
							paths++; 
						}
					}
					if (MinecartManiaWorld.getBlockAt(w, x, y, z-1).getTypeId() == Item.RAILS.getId()) {
						if (MinecartManiaWorld.getBlockData(w, x, y, z-1) == 0) {
							paths++;
						}
					}
				}
			}
		}
		else if (data == 7) {
			if (MinecartManiaWorld.getBlockAt(w, x-1, y, z).getTypeId() == Item.RAILS.getId() && MinecartManiaWorld.getBlockAt(w, x, y, z+1).getTypeId() == Item.RAILS.getId()) {
				if (MinecartManiaWorld.getBlockData(w, x-1, y, z) == 1 && MinecartManiaWorld.getBlockData(w, x, y, z+1) == 0) {
					paths = 2;
					if (MinecartManiaWorld.getBlockAt(w, x+1, y, z).getTypeId() == Item.RAILS.getId()) {
						if (MinecartManiaWorld.getBlockData(w, x+1, y, z) == 1) {
							paths++;
						}
					}
					if (MinecartManiaWorld.getBlockAt(w, x, y, z-1).getTypeId() == Item.RAILS.getId()) {
						if (MinecartManiaWorld.getBlockData(w, x, y, z-1) == 0) {
							paths++;
						}
					}
				}
			}
		}
		else if (data == 8) {
			if (MinecartManiaWorld.getBlockAt(w, x-1, y, z).getTypeId() == Item.RAILS.getId() && MinecartManiaWorld.getBlockAt(w, x, y, z-1).getTypeId() == Item.RAILS.getId()) {
				if (MinecartManiaWorld.getBlockData(w, x-1, y, z) == 1 && MinecartManiaWorld.getBlockData(w, x, y, z-1) == 0) {
					paths = 2;
					if (MinecartManiaWorld.getBlockAt(w, x+1, y, z).getTypeId() == Item.RAILS.getId()) {
						if (MinecartManiaWorld.getBlockData(w, x+1, y, z) == 1) {
							paths++;
						}
					}
					if (MinecartManiaWorld.getBlockAt(w, x, y, z+1).getTypeId() == Item.RAILS.getId()) {
						if (MinecartManiaWorld.getBlockData(w, x, y, z+1) == 0) {
							paths++;
						}
					}
				}
			}
		}
		else if (data == 9) {
			if (MinecartManiaWorld.getBlockAt(w, x+1, y, z).getTypeId() == Item.RAILS.getId() && MinecartManiaWorld.getBlockAt(w, x, y, z-1).getTypeId() == Item.RAILS.getId()) {
				if (MinecartManiaWorld.getBlockData(w, x+1, y, z) == 1 && MinecartManiaWorld.getBlockData(w, x, y, z-1) == 0) {
					paths = 2;
					if (MinecartManiaWorld.getBlockAt(w, x-1, y, z).getTypeId() == Item.RAILS.getId()) {
						if (MinecartManiaWorld.getBlockData(w, x-1, y, z) == 1) {
							paths++;
						}
					}
					if (MinecartManiaWorld.getBlockAt(w, x, y, z+1).getTypeId() == Item.RAILS.getId()) {
						if (MinecartManiaWorld.getBlockData(w, x, y, z+1) == 0) {
							paths++;
						}
					}
				}
			}
		}
		
		return paths > 2;
	}
	
	public static void doMinecartNearEntityCheck(final MinecartManiaMinecart minecart, List<Entity> entities) {
		//Set a flag to stop this event from happening twice
		minecart.setDataValue("MinecartNearEntityEvent", true);
		ArrayList<MinecartNearEntityEvent> deadQueue = new ArrayList<MinecartNearEntityEvent>(50);
    	Vector location = minecart.minecart.getLocation().toVector();
    	int rangeSquared = minecart.getEntityDetectionRange() * minecart.getEntityDetectionRange();
    	boolean killmobs = MinecartManiaWorld.isMinecartsKillMobs();
    	
    	for (Entity e : entities) {
    		
    		if (MinecartManiaWorld.isDead(e)) {
    			continue;
    		}
    		double distance = e.getLocation().toVector().distanceSquared(location);
    		
			if (distance <= rangeSquared) {
				MinecartNearEntityEvent mnee = new MinecartNearEntityEvent(minecart, e);
				//by default drop arrows
				boolean kill = e instanceof Arrow;
				//kill nearby animals before we bump into them
				if (distance <= 2) {
					kill = kill || killmobs && (e instanceof LivingEntity && (!(e instanceof Player)));
				}
				mnee.setActionTaken(kill);
				mnee.setDrop(e instanceof Arrow ? new ItemStack(Material.ARROW, 1) : null);
				MinecartManiaCore.server.getPluginManager().callEvent(mnee);
				//If cancelled, kill them once we are done calling events
				if (mnee.isActionTaken()) {
					deadQueue.add(mnee);
				}
				else {
					clearedItemFromRails(e, minecart);
				}
			}
    	}
    	
    	for (MinecartNearEntityEvent e : deadQueue) {
    		if (e.getDrop() != null) {
    			MinecartManiaWorld.dropItem(e.getEntity().getLocation(), e.getDrop());
    		}
    		e.getEntity().remove();
    	}
    	//Reset the flag
    	minecart.setDataValue("MinecartNearEntityEvent", null);
	}
	
	private static boolean clearedItemFromRails(Entity e, MinecartManiaMinecart minecart) {
		if (MinecartManiaWorld.getMinecartsClearRailsSetting() != 0) {
			if (e.getEntityId() == minecart.minecart.getEntityId()) {
				return false;
			}
			if (e instanceof Vehicle) {
				return false;
			}
			if (minecart.minecart.getPassenger() != null && minecart.minecart.getPassenger().getEntityId() == e.getEntityId()) {
				return false;
			}
			if (MinecartManiaWorld.getMinecartsClearRailsSetting() == 1 && e instanceof LivingEntity) {
				return false;
			}
			if (MinecartManiaWorld.getMinecartsClearRailsSetting() == 2 && e instanceof Player) {
				return false;
			}
			if (e instanceof Player && minecart.isOwner(e)) {
				return false;
			}
		
			if (minecart.isApproaching(e.getLocation().toVector())) {
				Location current = e.getLocation().clone();
				if (minecart.getMotionX() != 0.0D) {
					Location test = current.clone();
					test.setZ(current.getZ()-3);
					Location loc = EntityUtils.getValidLocation(test.getBlock(), 1);
					if (loc != null) {
						e.teleportTo(loc);
						return true;
					}
					test.setZ(current.getZ()+3);
					loc = EntityUtils.getValidLocation(test.getBlock(), 1);
					if (loc != null) {
						e.teleportTo(loc);
						return true;
					}
				}
				if (minecart.getMotionZ() != 0.0D) {
					Location test = current.clone();
					test.setX(current.getX()-3);
					Location loc = EntityUtils.getValidLocation(test.getBlock(), 1);
					if (loc != null) {
						e.teleportTo(loc);
						return true;
					}
					test.setX(current.getX()+3);
					loc = EntityUtils.getValidLocation(test.getBlock(), 1);
					if (loc != null) {
						e.teleportTo(loc);
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void updateNearbyItems(MinecartManiaMinecart minecart) {
		if (minecart.getDataValue("MinecartNearEntityEvent") != null) {
			return;
		}
		Object[] param = { minecart, minecart.minecart.getWorld().getEntities() };
		@SuppressWarnings("rawtypes")
		Class[] paramtype = { MinecartManiaMinecart.class, List.class };
		try {
			//No reason to keep this on the main thread, fire it on a second thread
			MinecartManiaTaskScheduler.doAsyncTask(MinecartUtils.class.getMethod("doMinecartNearEntityCheck", paramtype), 5, param);
		} catch (Exception e1) {
			
		}
	}	
}
