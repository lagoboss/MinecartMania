package com.afforess.minecartmaniacore;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.EntityMinecart;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftMinecart;
import org.bukkit.craftbukkit.entity.CraftPoweredMinecart;
import org.bukkit.craftbukkit.entity.CraftStorageMinecart;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Button;
import org.bukkit.material.Lever;
import org.bukkit.material.MaterialData;
import org.bukkit.Location;

public class MinecartManiaWorld {
	private static ConcurrentHashMap<Integer,MinecartManiaMinecart> minecarts = new ConcurrentHashMap<Integer,MinecartManiaMinecart>();
	private static ConcurrentHashMap<Location,MinecartManiaChest> chests = new ConcurrentHashMap<Location,MinecartManiaChest>();
	private static ConcurrentHashMap<Location,MinecartManiaDispenser> dispensers = new ConcurrentHashMap<Location,MinecartManiaDispenser>();
	private static ConcurrentHashMap<Location,MinecartManiaFurnace> furnaces = new ConcurrentHashMap<Location,MinecartManiaFurnace>();
	private static ConcurrentHashMap<String,MinecartManiaPlayer> players = new ConcurrentHashMap<String,MinecartManiaPlayer>();
	private static ConcurrentHashMap<String, Object> configuration = new ConcurrentHashMap<String,Object>();
	
	/**
	 ** Returns a new MinecartManiaMinecart from storage if it already exists, or creates and stores a new MinecartManiaMinecart object, and returns it
	 ** @param the minecart to wrap
	 **/
	 public static MinecartManiaMinecart getMinecartManiaMinecart(Minecart minecart) {
        MinecartManiaMinecart testMinecart = minecarts.get(new Integer(minecart.getEntityId()));
        if (testMinecart == null) {
        	
        	//Special handling to create storage and powered minecart correctly until Bukkit fixes their bug
        	CraftMinecart cm = (CraftMinecart)minecart;
        	EntityMinecart em = (EntityMinecart)cm.getHandle();
        	CraftServer cs = (CraftServer)MinecartManiaCore.server;
        	if (em.d == 1) {
        		CraftStorageMinecart csm = new CraftStorageMinecart(cs, em); 
        		minecart = (Minecart)csm;
        	}
        	if (em.d == 2) {
        		CraftPoweredMinecart csm = new CraftPoweredMinecart(cs, em); 
        		minecart = (Minecart)csm;
        	}
        	//End workaround
        	MinecartManiaMinecart newCart;
        	if (minecart instanceof StorageMinecart) {
        		newCart = new MinecartManiaStorageCart(minecart);
        	}
        	else {
        		newCart = new MinecartManiaMinecart(minecart);
        	}
        	minecarts.put(new Integer(minecart.getEntityId()), newCart);
        	return newCart;
        } else {
           return testMinecart;
        }
    }
	 
	/**
	 ** Returns true if the Minecart with the given entityID was deleted, false if not.
	 ** @param the id of the minecart to delete
	 **/
	 public static boolean delMinecartManiaMinecart(int entityID) {
        if (minecarts.containsKey(new Integer(entityID))) {
            minecarts.remove(new Integer(entityID));
            return true;
        }
        return false;
    }
	 
	 /**
	 ** Returns any minecart at the given location, or null if none is present
	 ** @param the x - coordinate to check
	 ** @param the y - coordinate to check
	 ** @param the z - coordinate to check
	 **/
	 public static MinecartManiaMinecart getMinecartManiaMinecartAt(int x, int y, int z) {
		 Iterator<Entry<Integer, MinecartManiaMinecart>> i = minecarts.entrySet().iterator();
		 while (i.hasNext()) {
			 Entry<Integer, MinecartManiaMinecart> e = i.next();
			 if (e.getValue().minecart.getLocation().getBlockX() == x) {
				 if (e.getValue().minecart.getLocation().getBlockY() == y) {
					 if (e.getValue().minecart.getLocation().getBlockZ() == z) {
						 return e.getValue();
					 }
				 }
			 }
		 }
		
		 return null;
	 }
	 
	 public static ArrayList<MinecartManiaMinecart> getMinecartManiaMinecartList() {
		 Iterator<Entry<Integer, MinecartManiaMinecart>> i = minecarts.entrySet().iterator();
		 ArrayList<MinecartManiaMinecart> minecartList = new ArrayList<MinecartManiaMinecart>(minecarts.size());
		 while (i.hasNext()) {
			 minecartList.add(i.next().getValue());
		 }
		 return minecartList;
	 }
	 
	 /**
	 ** Returns a new MinecartManiaChest from storage if it already exists, or creates and stores a new MinecartManiaChest object, and returns it
	 ** @param the chest to wrap
	 **/
	 public static MinecartManiaChest getMinecartManiaChest(Chest chest) {
        MinecartManiaChest testChest = chests.get(new Location(chest.getWorld(), chest.getX(), chest.getY(), chest.getZ()));
        if (testChest == null) {
	        MinecartManiaChest newChest = new MinecartManiaChest(chest);
	        chests.put(new Location(chest.getWorld(), chest.getX(), chest.getY(), chest.getZ()), newChest);
	        return newChest;
        } 
        else {
        	//Verify that this block is still a chest (could have been changed)
        	if (MinecartManiaWorld.getBlockIdAt(testChest.getWorld(), testChest.getX(), testChest.getY(), testChest.getZ()) == Material.CHEST.getId()) {
        		return testChest;
        	}
        	else {
        		chests.remove(new Location(chest.getWorld(), chest.getX(), chest.getY(), chest.getZ()));
        		return null;
        	}
        }
    }
	 
	/**
	 ** Returns true if the chest with the given location was deleted, false if not.
	 ** @param the  location of the chest to delete
	 **/
	 public static boolean delMinecartManiaChest(Location v) {
        if (chests.containsKey(v)) {
            chests.remove(v);
            return true;
        }
        return false;
    }
	 
	 /**
	 ** Returns a new MinecartManiaDispenser from storage if it already exists, or creates and stores a new MinecartManiaDispenser object, and returns it
	 ** @param the dispenser to wrap
	 **/
	 public static MinecartManiaDispenser getMinecartManiaDispenser(Dispenser dispenser) {
        MinecartManiaDispenser testDispenser = dispensers.get(new Location(dispenser.getWorld(), dispenser.getX(), dispenser.getY(), dispenser.getZ()));
        if (testDispenser == null) {
        	MinecartManiaDispenser newDispenser = new MinecartManiaDispenser(dispenser);
        	dispensers.put(new Location(dispenser.getWorld(), dispenser.getX(), dispenser.getY(), dispenser.getZ()), newDispenser);
	        return newDispenser;
        } 
        else {
        	//Verify that this block is still a dispenser (could have been changed)
        	if (MinecartManiaWorld.getBlockIdAt(testDispenser.getWorld(), testDispenser.getX(), testDispenser.getY(), testDispenser.getZ()) == Material.DISPENSER.getId()) {
        		return testDispenser;
        	}
        	else {
        		dispensers.remove(new Location(dispenser.getWorld(), dispenser.getX(), dispenser.getY(), dispenser.getZ()));
        		return null;
        	}
        }
    }
	 
	/**
	 ** Returns true if the dispenser with the given location was deleted, false if not.
	 ** @param the location of the dispenser to delete
	 **/
	 public static boolean delMinecartManiaDispenser(Location v) {
        if (dispensers.containsKey(v)) {
        	dispensers.remove(v);
            return true;
        }
        return false;
    }
	 
	 /**
	 ** Returns a new MinecartManiaFurnace from storage if it already exists, or creates and stores a new MinecartManiaFurnace object, and returns it
	 ** @param the furnace to wrap
	 **/
	 public static MinecartManiaFurnace getMinecartManiaFurnace(Furnace furnace) {
		 MinecartManiaFurnace testFurnace = furnaces.get(new Location(furnace.getWorld(), furnace.getX(), furnace.getY(), furnace.getZ()));
        if (testFurnace == null) {
        	MinecartManiaFurnace newFurnace = new MinecartManiaFurnace(furnace);
        	furnaces.put(new Location(furnace.getWorld(), furnace.getX(), furnace.getY(), furnace.getZ()), newFurnace);
	        return newFurnace;
        } 
        else {
        	//Verify that this block is still a furnace (could have been changed)
        	if (MinecartManiaWorld.getBlockIdAt(testFurnace.getWorld(), testFurnace.getX(), testFurnace.getY(), testFurnace.getZ()) == Material.FURNACE.getId()) {
        		return testFurnace;
        	}
        	else {
        		furnaces.remove(new Location(furnace.getWorld(), furnace.getX(), furnace.getY(), furnace.getZ()));
        		return null;
        	}
        }
    }
	 
	/**
	 ** Returns true if the furnaces with the given location was deleted, false if not.
	 ** @param the location of the furnaces to delete
	 **/
	 public static boolean delMinecartManiaFurnace(Location v) {
        if (furnaces.containsKey(v)) {
        	furnaces.remove(v);
            return true;
        }
        return false;
    }
	 
	 /**
	 ** Returns a new MinecartManiaPlayer from storage if it already exists, or creates and stores a new MinecartManiaPlayer object, and returns it
	 ** @param the player to wrap
	 **/	 
	 public static MinecartManiaPlayer getMinecartManiaPlayer(Player player) {
		 MinecartManiaPlayer testPlayer = players.get(player.getName());
		 if (testPlayer == null) {
			 testPlayer = new MinecartManiaPlayer(player.getName());
			 players.put(player.getName(), testPlayer);
		 }
		 return testPlayer;
	 }
	 
	/**
	 ** Returns the value from the loaded configuration
	 ** @param the string key the configuration value is associated with
	 **/
	 public static Object getConfigurationValue(String key) {
		 if (configuration.containsKey(key)) {
			 return configuration.get(key);
		 }
		 return null;
	 }
	 
	/**
	 ** Creates a new configuration value if it does not already exists, or resets an existing value
	 ** @param the string key the configuration value is associated with
	 ** @param the value to store
	 **/	 
	 public static void setConfigurationValue(String key, Object value) {
		 if (value == null) {
			 configuration.remove(key);
		 }
		 else {
			 configuration.put(key, value);
		 }
	 }
	 
	 public static ConcurrentHashMap<String, Object> getConfiguration() {
		 return configuration;
	 }
	 
	/**
	 ** Returns an integer value from the given object, if it exists
	 ** @param the object containing the value
	 **/		 
	 public static int getIntValue(Object o) {
		 if (o != null) {
			if (o instanceof Integer) {
				return ((Integer)o).intValue();
			}
		}
		return 0;
	 }
	 
	 public static double getDoubleValue(Object o) {
		 if (o != null) {
			if (o instanceof Double) {
				return ((Double)o).doubleValue();
			}
			//Attempt integer value
			return getIntValue(o);
		}
		return 0;
	 }
	 

	public static int getReverseBlockId() {
		return getIntValue(getConfigurationValue("Reverse Block"));
	}
	
	public static int getHighSpeedBoosterBlockId() {
		return getIntValue(getConfigurationValue("High Speed Booster Block"));
	}
	
	public static double getHighSpeedBoosterBlockMultiplier() {
		return getDoubleValue(getConfigurationValue("High Speed Booster Block Multiplier"));
	}
	
	public static int getLowSpeedBoosterBlockId() {
		return getIntValue(getConfigurationValue("Low Speed Booster Block"));
	}
	
	public static double getLowSpeedBoosterBlockMultiplier() {
		return getDoubleValue(getConfigurationValue("Low Speed Booster Block Multiplier"));
	}
	
	public static int getHighSpeedBrakeBlockId() {
		return getIntValue(getConfigurationValue("High Speed Brake Block"));
	}
	
	public static double getHighSpeedBrakeBlockDivisor() {
		return getDoubleValue(getConfigurationValue("High Speed Brake Block Divisor"));
	}
	
	public static int getLowSpeedBrakeBlockId() {
		return getIntValue(getConfigurationValue("Low Speed Brake Block"));
	}
	
	public static double getLowSpeedBrakeBlockDivisor() {
		return getDoubleValue(getConfigurationValue("Low Speed Brake Block Divisor"));
	}
	
	public static int getCatcherBlockId() {
		return getIntValue(getConfigurationValue("Catcher Block"));
	}
	
	public static int getEjectorBlockId() {
		return getIntValue(getConfigurationValue("Ejector Block"));
	}
	
	public static int getMaximumMinecartSpeedPercent() {
		return getIntValue(getConfigurationValue("Maximum Minecart Speed Percent"));
	}
	
	public static boolean isKeepMinecartsLoaded() {
		Object o = getConfigurationValue("Keep Minecarts Loaded");
		if (o != null) {
			Boolean value = (Boolean)o;
			return value.booleanValue();
		}
		return false;
	}
	
	public static boolean isMinecartsKillMobs() {
		Object o = getConfigurationValue("Minecarts Kill Mobs");
		if (o != null) {
			Boolean value = (Boolean)o;
			return value.booleanValue();
		}
		return true;
	}
	
	public static boolean isPressurePlateRails() {
		Object o = getConfigurationValue("Pressure Plate Rails");
		if (o != null) {
			Boolean value = (Boolean)o;
			return value.booleanValue();
		}
		return true;
	}
	
	public static boolean isReturnMinecartToOwner() {
		Object o = getConfigurationValue("Minecarts return to owner");
		if (o != null) {
			Boolean value = (Boolean)o;
			return value.booleanValue();
		}
		return true;
	}
	
	/**
	 ** Returns the block at the given x, y, z coordinates
	 ** @param w World to take effect in
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 **/	
	public static Block getBlockAt(World w, int x, int y, int z) {
		return w.getBlockAt(x, y, z);
	}
	
	/**
	 ** Returns the block type id at the given x, y, z coordinates
	 ** @param w World to take effect in
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 **/	
	public static int getBlockIdAt(World w, int x, int y, int z) {
		return w.getBlockTypeIdAt(x, y, z);
	}
	
	/**
	 ** Returns the block at the given x, y, z coordinates
	 ** @param w World to take effect in
	 ** @param new block type id
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 **/
	public static void setBlockAt(World w, int type, int x, int y, int z) {
		w.getBlockAt(x, y, z).setTypeId(type);
	}
	
	/**
	 ** Returns the block data at the given x, y, z coordinates
	 ** @param w World to take effect in
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 **/
	public static byte getBlockData(World w, int x, int y, int z) {
		return w.getBlockAt(x, y, z).getData();
	}
	
	/**
	 ** sets the block data at the given x, y, z coordinates
	 ** @param w World to take effect in
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 ** @param new data to set
	 **/
	public static void setBlockData(World w, int x, int y, int z, int data) {
		//Better to crash than to write bad data!
		if (data < 0 || data > Byte.MAX_VALUE) throw new IllegalArgumentException();
		w.getBlockAt(x, y, z).setData((byte) (data));
	}
	
	/**
	 ** Returns true if the block at the given x, y, z coordinates is indirectly powered
	 ** @param w World to take effect in
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 **/
	public static boolean isBlockIndirectlyPowered(World w, int x, int y, int z) {
		return getBlockAt(w, x, y, z).isBlockIndirectlyPowered();
	}
	
	/**
	 ** Returns true if the block at the given x, y, z coordinates is directly powered
	 ** @param w World to take effect in
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 **/
	public static boolean isBlockPowered(World w, int x, int y, int z) {
		return getBlockAt(w, x, y, z).isBlockPowered();
	}
	
	/**
	 ** Sets the block at the given x, y, z coordinates to the given power state, if possible
	 ** @param w World to take effect in
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 ** @param power state
	 **/
	public static void setBlockPowered(World w, int x, int y, int z, boolean power) {
		MaterialData md = getBlockAt(w, x, y, z).getState().getData();
		int data = getBlockData(w, x, y, z);
		if (getBlockAt(w, x, y, z).getType().equals(Material.DIODE_BLOCK_OFF) && power) {
			setBlockAt(w, Material.DIODE_BLOCK_ON.getId(), x, y, z);
			setBlockData(w, x, y, z, (byte)data);
		}
		else if (getBlockAt(w, x, y, z).getType().equals(Material.DIODE_BLOCK_ON) && !power) {
			setBlockAt(w, Material.DIODE_BLOCK_OFF.getId(), x, y, z);
			setBlockData(w, x, y, z, (byte)data);
		}
		else if (md instanceof Lever || md instanceof Button) {
			setBlockData(w, x, y, z, ((byte)(power ? data | 0x8 : data & 0x7)));
		}
	}
	
	/**
	 ** Sets the block at the given x, y, z coordinates, as well as any block directly touch the given block to the given power state, if possible
	 ** @param w World to take effect in
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 ** @param power state
	 **/
	public static void setBlockIndirectlyPowered(World w, int x, int y, int z, boolean power) {
		setBlockPowered(w, x, y, z, power);
		setBlockPowered(w, x-1, y, z, power);
		setBlockPowered(w, x+1, y, z, power);
		setBlockPowered(w, x, y-1, z, power);
		setBlockPowered(w, x, y+1, z, power);
		setBlockPowered(w, x, y, z-1, power);
		setBlockPowered(w, x, y, z+1, power);
	}
	
	/** Spawns a minecart at the given coordinates. Includes a "fudge factor" to get the minecart to properly line up with minecart tracks.
	 ** @param Location to spawn the minecart at
	 ** @param Material type of minecart to spawn
	 ** @param Owner of this minecart (player or chest). Can be null
	 **/
	public static MinecartManiaMinecart spawnMinecart(Location l, Material type, Object owner) {
		return spawnMinecart(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ(), type, owner);
	}
	
	/** Spawns a minecart at the given coordinates. Includes a "fudge factor" to get the minecart to properly line up with minecart tracks.
	 ** @param w World to take effect in
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 ** @param Material type of minecart to spawn
	 ** @param Owner of this minecart (player or chest). Can be null
	 **/
	public static MinecartManiaMinecart spawnMinecart(World w, int x, int y, int z, Material type, Object owner) {
		Minecart m;
		CraftWorld cw = (CraftWorld)w;
		CraftServer cs = (CraftServer)MinecartManiaCore.server;
		if (type.getId() == Material.MINECART.getId()) {
			m = w.spawnMinecart(new Location(w, x + 0.5D, y, z + 0.5D));
		}
		else if (type.getId() == Material.POWERED_MINECART.getId()) {
			EntityMinecart em = new EntityMinecart(cw.getHandle(), x + 0.5D, y, z + 0.5D, 2);
			CraftPoweredMinecart csm = new CraftPoweredMinecart(cs, em);
			m = (Minecart)csm;
			cw.getHandle().a(em);
			//m = w.spawnPoweredMinecart(new Location(w, x + 0.5D, y, z + 0.5D));
		}
		else {
			//real men construct their minecarts from the bare ash, er, actually bukkit's implementation of spawning is broken.
			EntityMinecart em = new EntityMinecart(cw.getHandle(), x + 0.5D, y, z + 0.5D, 1);
			CraftStorageMinecart csm = new CraftStorageMinecart(cs, em);
			m = (Minecart)csm;
			cw.getHandle().a(em);
			//m = w.spawnStorageMinecart(new Location(w, x + 0.5D, y, z + 0.5D));
		}
		MinecartManiaMinecart minecart = null;
		if (owner != null) {
			if (owner instanceof Player) {
				if (m instanceof StorageMinecart) {
					minecart = new MinecartManiaStorageCart(m, ((Player)owner).getName());
				}
				else {
					minecart = new MinecartManiaMinecart(m, ((Player)owner).getName());
				}
				
				minecarts.put(m.getEntityId(), minecart);
			}
			if (owner instanceof MinecartManiaChest) {
				if (m instanceof StorageMinecart) {
					minecart = new MinecartManiaStorageCart(m, ((MinecartManiaChest)owner).toString());
				}
				else {
					minecart = new MinecartManiaMinecart(m, ((MinecartManiaChest)owner).toString());
				}
				minecarts.put(m.getEntityId(), minecart);
			}
		}
		else {
			if (m instanceof StorageMinecart) {
				minecart = new MinecartManiaStorageCart(m);
			}
			else {
				minecart = new MinecartManiaMinecart(m);
			}
			minecarts.put(m.getEntityId(), minecart);
		}
		return minecart;
	}
	
	public static void kill(final Entity e) {
		e.remove();

	}
	
	public static void dropItem(final Location loc, final ItemStack item) {
		//force this to run on the main thread
		MinecartManiaCore.server.getScheduler().scheduleSyncDelayedTask(MinecartManiaCore.instance, new Runnable() { public void run() {
			loc.getWorld().dropItem(loc, item);
			}});
	}
}








