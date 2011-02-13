package com.afforess.minecartmaniacore;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.EntityMinecart;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftMinecart;
import org.bukkit.craftbukkit.entity.CraftPoweredMinecart;
import org.bukkit.craftbukkit.entity.CraftStorageMinecart;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.material.Button;
import org.bukkit.material.Lever;
import org.bukkit.material.MaterialData;
import org.bukkit.material.RedstoneTorch;
import org.bukkit.material.RedstoneWire;
import org.bukkit.Location;

public class MinecartManiaWorld {
	private static ConcurrentHashMap<Integer,MinecartManiaMinecart> minecarts = new ConcurrentHashMap<Integer,MinecartManiaMinecart>();
	private static ConcurrentHashMap<Location,MinecartManiaChest> chests = new ConcurrentHashMap<Location,MinecartManiaChest>();
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
        	
        	MinecartManiaMinecart newCart = new MinecartManiaMinecart(minecart);
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
        } else {
        	return testChest;
        }
    }
	 
	/**
	 ** Returns true if the chest with the given vector was deleted, false if not.
	 ** @param the vector location of the chest to delete
	 **/
	 public static boolean delMinecartManiaChest(Location v) {
        if (chests.containsKey(v)) {
            chests.remove(v);
            return true;
        }
        return false;
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
		//return isBlockPowered(x+1, y, z) || isBlockPowered(x-1, y, z) || isBlockPowered(x, y, z+1) || isBlockPowered(x, y, z-1) || isBlockPowered(x, y-1, z);
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
		//MaterialData md = getWorld().getBlockAt(x, y, z).getState().getData();
		//if (md instanceof Redstone) {
		//	return ((Redstone) md).isPowered();
		//}
		//return false;
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
		if (md instanceof RedstoneWire) {
			getBlockAt(w, x, y, z).getState().setData(new MaterialData(getBlockAt(w, x, y, z).getState().getTypeId(), (byte)(power ?  16 : 0)));
		}
		else if (md instanceof RedstoneTorch) {
			getBlockAt(w, x, y, z).getState().setData(new MaterialData(power ? Material.REDSTONE_TORCH_ON : Material.REDSTONE_TORCH_OFF));
		}
		else if (md instanceof Lever || md instanceof Button) {
			getBlockAt(w, x, y, z).getState().setData(new MaterialData(getBlockAt(w, x, y, z).getState().getTypeId(), (byte)(power ? md.getData() | 0x8 : md.getData() & 0x7)));
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
	 ** @param w World to take effect in
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 ** @param Material type of minecart to spawn
	 ** @param Owner of this minecart (player or chest). Can be null
	 **/
	public static void spawnMinecart(World w, int x, int y, int z, Material type, Object owner) {
		Minecart m;
		if (type.getId() == Material.MINECART.getId()) {
			m = w.spawnMinecart(new Location(w, x + 0.5D, y, z + 0.5D));
		}
		else if (type.getId() == Material.POWERED_MINECART.getId()) {
			m = w.spawnPoweredMinecart(new Location(w, x + 0.5D, y, z + 0.5D));
		}
		else {
			m = w.spawnStorageMinecart(new Location(w, x + 0.5D, y, z + 0.5D));
		}
		if (owner != null) {
			if (owner instanceof Player) {
				minecarts.put(m.getEntityId(), new MinecartManiaMinecart(m, ((Player)owner).getName()));
			}
			if (owner instanceof MinecartManiaChest) {
				minecarts.put(m.getEntityId(), new MinecartManiaMinecart(m, ((MinecartManiaChest)owner).toString()));
			}
		}
	}
	
	/** Spawns a new thread that will run next server tick, invoking the given method, with the given class and parameters.
	 ** @param Method to run
	 ** @param Class to run the method from. If the class is static, null may be used.
	 ** @param Parameters for the thread.
	 **/
	public static void doAsyncTask(final Method m, final Object method, final Object...parameters){
		Runnable a = new Runnable() { public void run() { try { m.invoke(method, parameters); } catch (Exception e) { e.printStackTrace(); } } };
		MinecartManiaCore.server.getScheduler().scheduleAsyncDelayedTask(MinecartManiaCore.instance, a);
	}
	
	/** Spawns a new thread that will run next server tick, invoking the given static method and parameters.
	 ** @param Method to run
	 ** @param Parameters for the thread.
	 **/
	public static void doAsyncTask(final Method m, final Object...parameters) {
		doAsyncTask(m, null, parameters);
	}
	
	/** Spawns a new thread that will run next server tick, invoking the given method with the given class. The method should have no parameters.
	 ** @param Method to run
	 ** @param Class to run the method from.
	 **/
	public static void doAsyncTask(final Method m, final Object method) {
		doAsyncTask(m, method, (Object[])null);
	}
	
	/** Spawns a new thread that will run next server tick, invoking the given static method, with no parameters.
	 ** @param Method to run
	 **/
	public static void doAsyncTask(final Method m) {
		doAsyncTask(m, null, (Object[])null);
	}
}








