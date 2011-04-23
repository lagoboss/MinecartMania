package com.afforess.minecartmaniacore.utils;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.Wolf;
import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.Item;
import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;

public class MinecartUtils {

	public static boolean isTrack(Block block) {
		return isTrack(block.getTypeId());
	}
	
	public static boolean isTrack(Item item) {
		return isTrack(item.getId());
	}
	
	public static boolean isTrack(int id) {
		return id == Item.RAILS.getId() || id == Item.POWERED_RAIL.getId() || id == Item.DETECTOR_RAIL.getId();
	}
	
	public static boolean isTrack(Location location) {
		return isTrack(location.getBlock().getTypeId());
	}
	
	public static boolean validMinecartTrackAnyDirection(World w, int x, int y, int z, int range) {
		return validMinecartTrack(w, x, y, z, range, DirectionUtils.CompassDirection.NORTH) ||
			validMinecartTrack(w, x, y, z, range, DirectionUtils.CompassDirection.EAST) || 
			validMinecartTrack(w, x, y, z, range, DirectionUtils.CompassDirection.SOUTH) ||
			validMinecartTrack(w, x, y, z, range, DirectionUtils.CompassDirection.WEST);
	}
	
	public static boolean isSlopedTrack(Block rail) {
		return isSlopedTrack(rail.getWorld(), rail.getX(), rail.getY(), rail.getZ());
	}
	
	public static boolean isSlopedTrack(World w, int x, int y, int z) {
		int data = MinecartManiaWorld.getBlockData(w, x, y, z);
		return data >= 0x2 && data <= 0x5;
	}
	
	public static boolean isCurvedTrack(Block rail) {
		int data = rail.getData();
		return data > 5 && data < 10;
	}
	
	public static boolean validMinecartTrack(Location loc, int range, CompassDirection direction) {
		return validMinecartTrack(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), range, direction);
	}
	
	//TODO this method is not a perfect detection of track. It will give false positives for having 2 sets of parallel track, and when double curves are used
	public static boolean validMinecartTrack(World w, int x, int y, int z, int range, CompassDirection direction) {
		if (!isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z))) {
			y--;
			if (!isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z))) {
				return false;
			}
		}
		range--;
		while (range > 0) {
			if (direction == CompassDirection.NORTH) x--;
			else if (direction == CompassDirection.EAST) z--;
			else if (direction == CompassDirection.SOUTH) x++;
			else if (direction == CompassDirection.WEST) z++;
			if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y-1, z))) y--;
			else if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y+1, z))) y++;
			if (!isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z))) return false;
			
			if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x-1, y, z))) direction = CompassDirection.NORTH;
			else if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z-1))) direction = CompassDirection.EAST;
			else if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x+1, y, z))) direction = CompassDirection.SOUTH;
			else if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z+1))) direction = CompassDirection.WEST;
			range--;
		}
		return true;
	}
	
	public static boolean isAtIntersection(Location loc) {
		return isAtIntersection(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}
	
	public static boolean isAtIntersection(World w, int x, int y, int z) {
		int paths = 0;

		int data = MinecartManiaWorld.getBlockData(w, x, y, z);
		
		if (data == 0 || data == 1) {
			if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z-1))) {
				if (MinecartManiaWorld.getBlockData(w, x, y, z-1) == 0) {
					paths++;
				}
			}
			if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z+1))) {
				if (MinecartManiaWorld.getBlockData(w, x, y, z+1) == 0) {
					paths++;
				}
			}
			if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x-1, y, z))) {
				if (MinecartManiaWorld.getBlockData(w, x-1, y, z) == 1) {
					paths++;
				}
			}
			if (isTrack(MinecartManiaWorld.getBlockAt(w, x+1, y, z).getTypeId())) {
				if (MinecartManiaWorld.getBlockData(w, x+1, y, z) == 1) {
					paths++;
				}
			}
		}
		
		else if (data == 6) {
			if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x+1, y, z)) && isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z+1))) {
				if (MinecartManiaWorld.getBlockData(w, x+1, y, z) == 1 && MinecartManiaWorld.getBlockData(w, x, y, z+1) == 0) {
					paths = 2;
					if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x-1, y, z))) {
						if (MinecartManiaWorld.getBlockData(w, x-1, y, z) == 1) {
							paths++; 
						}
					}
					if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z-1))) {
						if (MinecartManiaWorld.getBlockData(w, x, y, z-1) == 0) {
							paths++;
						}
					}
				}
			}
		}
		else if (data == 7) {
			if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x-1, y, z)) && isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z+1))) {
				if (MinecartManiaWorld.getBlockData(w, x-1, y, z) == 1 && MinecartManiaWorld.getBlockData(w, x, y, z+1) == 0) {
					paths = 2;
					if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x+1, y, z))) {
						if (MinecartManiaWorld.getBlockData(w, x+1, y, z) == 1) {
							paths++;
						}
					}
					if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z-1))) {
						if (MinecartManiaWorld.getBlockData(w, x, y, z-1) == 0) {
							paths++;
						}
					}
				}
			}
		}
		else if (data == 8) {
			if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x-1, y, z)) && isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z-1))) {
				if (MinecartManiaWorld.getBlockData(w, x-1, y, z) == 1 && MinecartManiaWorld.getBlockData(w, x, y, z-1) == 0) {
					paths = 2;
					if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x+1, y, z))) {
						if (MinecartManiaWorld.getBlockData(w, x+1, y, z) == 1) {
							paths++;
						}
					}
					if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z+1))) {
						if (MinecartManiaWorld.getBlockData(w, x, y, z+1) == 0) {
							paths++;
						}
					}
				}
			}
		}
		else if (data == 9) {
			if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x+1, y, z)) && isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z-1))) {
				if (MinecartManiaWorld.getBlockData(w, x+1, y, z) == 1 && MinecartManiaWorld.getBlockData(w, x, y, z-1) == 0) {
					paths = 2;
					if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x-1, y, z))) {
						if (MinecartManiaWorld.getBlockData(w, x-1, y, z) == 1) {
							paths++;
						}
					}
					if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z+1))) {
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
		Vector location = minecart.minecart.getLocation().toVector();
		for (Entity e : entities) {
			
			if (e.isDead()) {
				continue;
			}

			double distance = e.getLocation().toVector().distanceSquared(location);
			if (distance < 6) {		
				if (minecart.isStorageMinecart() && e instanceof org.bukkit.entity.Item
					&& ((MinecartManiaStorageCart)minecart).addItem(((org.bukkit.entity.Item)e).getItemStack())) {
					e.remove();
				}
				else if (shouldKillEntity(minecart, e)) {
					e.remove();
				}
				else if (clearedItemFromRails(e, minecart)){
					;
				}
			}
		}
		//Reset the flag
		minecart.setDataValue("MinecartNearEntityEvent", null);
	}
	
	private static boolean shouldKillEntity(MinecartManiaMinecart minecart, Entity entity) {
		if (entity instanceof Arrow) {
			return true; //special case, replaces them with arrow itemstack
		}
		if (MinecartManiaWorld.isMinecartsKillMobs()) {
			if (entity instanceof LivingEntity) {
				if (entity instanceof HumanEntity) {
					return false;
				}
				if (entity instanceof Wolf) {
					return false;
				}
				if (minecart.minecart.getPassenger() != null) {
					if (minecart.minecart.getPassenger().getEntityId() == entity.getEntityId()) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
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
				Location current = e.getLocation();
				if (minecart.getMotionX() != 0.0D) {
					Location test = current.clone();
					test.setZ(current.getZ()-3);
					Location loc = EntityUtils.getValidLocation(test.getBlock(), 1);
					if (loc != null) {
						e.teleport(loc);
						return true;
					}
					test.setZ(current.getZ()+3);
					loc = EntityUtils.getValidLocation(test.getBlock(), 1);
					if (loc != null) {
						e.teleport(loc);
						return true;
					}
				}
				if (minecart.getMotionZ() != 0.0D) {
					Location test = current.clone();
					test.setX(current.getX()-3);
					Location loc = EntityUtils.getValidLocation(test.getBlock(), 1);
					if (loc != null) {
						e.teleport(loc);
						return true;
					}
					test.setX(current.getX()+3);
					loc = EntityUtils.getValidLocation(test.getBlock(), 1);
					if (loc != null) {
						e.teleport(loc);
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void updateNearbyItems(final MinecartManiaMinecart minecart) {
		if (minecart.getDataValue("MinecartNearEntityEvent") != null) {
			return;
		}
		final List<Entity> list = minecart.minecart.getWorld().getEntities();
		Thread update = new Thread() {
			public void run() {
				doMinecartNearEntityCheck(minecart, list);
			}
		};
		update.start();
	}	
}
