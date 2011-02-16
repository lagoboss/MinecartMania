package com.afforess.minecartmaniacore;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MinecartManiaChest {

	private final Location chest;
	private boolean RedstonePower;
	private ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<String,Object>();
	public MinecartManiaChest(Chest chest) {
		this.chest = chest.getBlock().getLocation();
		setRedstonePower(MinecartManiaWorld.isBlockIndirectlyPowered(chest.getWorld(), getX(), getY(), getZ()));
	}
	
	public int getX() {
		return this.chest.getBlockX();
	}
	
	public int getY() {
		return this.chest.getBlockY();
	}
	
	public int getZ() {
		return this.chest.getBlockZ();
	}
	
	public World getWorld() {
		return this.chest.getWorld();
	}
	
	public Location getLocation() {
		return chest;
	}
	
	public Chest getChest() {
		return (Chest)MinecartManiaWorld.getBlockAt(chest.getWorld(), getX(), getY(), getZ()).getState();
	}
	
	
	/**
	 ** Returns the neighbor chest to this chest, or null if none exists
	 **/
	public MinecartManiaChest getNeighborChest() {
		return getNeighborChest(chest.getWorld(), getX(), getY(), getZ());
	}
	
	/**
	 ** Returns the neighbor chest to this chest, or null if none exists
	 ** @param x coordinate to search
	 ** @param y coordinate to search
	 ** @param z coordinate to search
	 **/
	 public static MinecartManiaChest getNeighborChest(World w, int x, int y, int z)
	 {
    	if (MinecartManiaWorld.getBlockAt(w, x - 1, y, z).getType().equals(Material.CHEST)) {
            return MinecartManiaWorld.getMinecartManiaChest((Chest)MinecartManiaWorld.getBlockAt(w, x - 1, y, z).getState());
        }
        if(MinecartManiaWorld.getBlockAt(w, x + 1, y, z).getType().equals(Material.CHEST)) {
        	return MinecartManiaWorld.getMinecartManiaChest((Chest)MinecartManiaWorld.getBlockAt(w, x + 1, y, z).getState());
        }
        if(MinecartManiaWorld.getBlockAt(w, x, y, z - 1).getType().equals(Material.CHEST)) {
        	return MinecartManiaWorld.getMinecartManiaChest((Chest)MinecartManiaWorld.getBlockAt(w, x, y, z - 1).getState());
        }
        if (MinecartManiaWorld.getBlockAt(w, x, y, z + 1).getType().equals(Material.CHEST)) {
        	return MinecartManiaWorld.getMinecartManiaChest((Chest)MinecartManiaWorld.getBlockAt(w, x, y, z + 1).getState());
		}

		return null;
    }
	 
	 /**
	 ** Returns the value from the loaded data
	 ** @param the string key the data value is associated with
	 **/
	 public Object getDataValue(String key) {
		 if (data.containsKey(key)) {
			 return data.get(key);
		 }
		 return null;
	 }
	 
	/**
	 ** Creates a new data value if it does not already exists, or resets an existing value
	 ** @param the string key the data value is associated with
	 ** @param the value to store
	 **/	 
	 public void setDataValue(String key, Object value) {
		 if (value == null) {
			 data.remove(key);
		 }else {
			 data.put(key, value);
		 }
	 }
	
	/**
	 ** attempts to add an itemstack to this chest. It adds items in a 'smart' manner, merging with existing itemstacks, until they
	 ** reach the maximum size (64). If it fails, it will not alter the chest's previous contents.
	 ** Causes of failure: Full Chest, Concurrent modification.
	 ** @param item to add
	 **/
	public boolean addItem(ItemStack item) {
		if (item == null) {
			return true;
		}
		if (item.getTypeId() == Material.AIR.getId()) {
			return true;
		}
		Inventory inventory =  getChest().getInventory();
		//Backup contents
		ItemStack[] backup = inventory.getContents().clone();
		ItemStack backupItem = new ItemStack(item.getTypeId(), item.getAmount(), item.getDurability());
		
		//First attempt to merge the itemstack with existing item stacks that aren't full (< 64)
		for (int i = 0; i < inventory.getSize(); i++) {
			if (inventory.getItem(i) != null) {
				if (inventory.getItem(i).getTypeId() == item.getTypeId()) {
					if (inventory.getItem(i).getAmount() + item.getAmount() <= 64) {
						inventory.setItem(i, new ItemStack(item.getTypeId(), inventory.getItem(i).getAmount() + item.getAmount()));
						update();
						return true;
					}
					else {
						int diff = inventory.getItem(i).getAmount() + item.getAmount() - 64;
						inventory.setItem(i, new ItemStack(item.getTypeId(), inventory.getItem(i).getAmount() + item.getAmount()));
						item = new ItemStack(item.getTypeId(), diff);
					}
				}
			}
		}
		
		//Attempt to add the item to an empty slot
		int emptySlot = inventory.firstEmpty();
		if (emptySlot > -1) {
			inventory.setItem(emptySlot, item);
			update();
			return true;
		}
		
		
		//Try to merge the itemstack with the neighbor chest, if we have one
		MinecartManiaChest neighbor = getNeighborChest();
		if (neighbor != null) {
			//flag to prevent infinite recursion
			if (getDataValue("neighbor") == null) {
				neighbor.setDataValue("neighbor", Boolean.TRUE);
				if (getNeighborChest().addItem(item)) {
					update();
					return true;
				}
			}
			else {
				//reset flag
				setDataValue("neighbor", null);
			}
		}
			
		//if we fail, reset the inventory and item back to previous values
		inventory.setContents(backup);
		item = backupItem;
		return false;
	}
	
	/**
	 ** attempts to add a single item to this chest. If it fails, it will not alter the chest's previous contents
	 ** @param itemtype to add
	 **/
	public boolean addItem(int type) {
		return addItem(new ItemStack(type, 1));
	}
	
	/**
	 ** attempts to add an itemstack to this chest. If it fails, it will not alter the chest's previous contents
	 ** @param itemtype to add
	 ** @param the amount to add
	 **/
	public boolean addItem(int type, int amount) {
		return addItem(new ItemStack(type, amount));
	}
	
	/**
	 ** attempts to remove the specified amount of an item type from this chest. If it fails, it will not alter the chests previous contents.
	 ** @param itemtype to remove
	 ** @param the amount to remove
	 **/
	public boolean removeItem(int type, int amount) {
		Inventory inventory =  getChest().getInventory();
		//Backup contents
		ItemStack[] backup = inventory.getContents().clone();
		
		for (int i = 0; i < inventory.getSize(); i++) {
			if (inventory.getItem(i) != null) {
				if (inventory.getItem(i).getTypeId() == type) {
					if (inventory.getItem(i).getAmount() - amount > 0) {
						inventory.setItem(i, new ItemStack(type, inventory.getItem(i).getAmount() - amount));
						update();
						return true;
					}
					else if (inventory.getItem(i).getAmount() - amount == 0) {
						inventory.clear(i);
						update();
						return true;
					}
					else{
						amount -=  inventory.getItem(i).getAmount();
						inventory.clear(i);
					}
				}
			}
		}
		
		MinecartManiaChest neighbor = getNeighborChest();
		if (neighbor != null) {
			//flag to prevent infinite recursion
			if (getDataValue("neighbor") == null) {
				neighbor.setDataValue("neighbor", Boolean.TRUE);
				if (neighbor.removeItem(type, amount)) {
					update();
					return true;
				}
			}
			else {
				//reset flag
				setDataValue("neighbor", null);
			}
		}
		
			
		//if we fail, reset the inventory back to previous values
		inventory.setContents(backup);
		return false;
	}
	
	/**
	 ** attempts to remove a single item type from this chest. If it fails, it will not alter the chests previous contents.
	 ** @param item type to remove
	 **/
	public boolean removeItem(int type) {
		return removeItem(type, 1);
	}
	
	/**
	 ** Returns true if this chest or it's neighbor chest contains an itemstack of the given material
	 ** @param Material to search for
	 **/
	public boolean contains(Material type) {
		return contains(type.getId());
	}
	
	/**
	 ** Returns true if this chest or it's neighbor chest contains an itemstack of the given item id
	 ** @param item id to search for
	 **/
	public boolean contains(int type) {
		Inventory inventory =  getChest().getInventory();
		for (int i = 0; i < inventory.getSize(); i++) {
			if (inventory.getItem(i) != null) {
				if (inventory.getItem(i).getTypeId() == type) {
					return true;
				}
			}
		}
		
		MinecartManiaChest neighbor = getNeighborChest();
		if (neighbor != null) {
			//flag to prevent infinite recursion
			if (getDataValue("neighbor") == null) {
				neighbor.setDataValue("neighbor", Boolean.TRUE);
				if (neighbor.contains(type)) {
					return true;
				}
			}
			else {
				//reset flag
				setDataValue("neighbor", null);
			}
		}
		
		return false;
	}

	public void setRedstonePower(boolean redstonePower) {
		RedstonePower = redstonePower;
	}

	public boolean isRedstonePower() {
		return RedstonePower;
	}
	
	public void update() {
		 getChest().update();
	}
	
	public String toString() {
		return "[" + getX() + ":" + getY() + ":" + getZ() + "]";
	}
	

}
