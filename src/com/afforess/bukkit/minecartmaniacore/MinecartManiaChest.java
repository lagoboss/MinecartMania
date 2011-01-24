package com.afforess.bukkit.minecartmaniacore;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MinecartManiaChest {

	private Chest chest;
	private boolean RedstonePower;
	private ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<String,Object>();
	public MinecartManiaChest(Chest chest) {
		this.chest = chest;
		setRedstonePower(MinecartManiaWorld.isBlockIndirectlyPowered(getX(), getY(), getZ()));
	}
	
	/**
	 ** returns the chest we are wrapping
	 **/
	public Chest getChest() {
		return chest;
	}
	
	public int getX() {
		return this.chest.getX();
	}
	
	public int getY() {
		return this.chest.getY();
	}
	
	public int getZ() {
		return this.chest.getZ();
	}
	
	
	/**
	 ** Returns the neighbor chest to this chest, or null if none exists
	 **/
	public MinecartManiaChest getNeighborChest() {
		return getNeighborChest(getX(), getY(), getZ());
	}
	
	/**
	 ** Returns the neighbor chest to this chest, or null if none exists
	 ** @param x coordinate to search
	 ** @param y coordinate to search
	 ** @param z coordinate to search
	 **/
	 public static MinecartManiaChest getNeighborChest(int x, int y, int z)
	 {
    	if (MinecartManiaWorld.getBlockAt(x - 1, y, z).getType().equals(Material.CHEST)) {
            return MinecartManiaWorld.getMinecartManiaChest((Chest)MinecartManiaWorld.getBlockAt(x - 1, y, z).getState());
        }
        if(MinecartManiaWorld.getBlockAt(x + 1, y, z).getType().equals(Material.CHEST)) {
        	return MinecartManiaWorld.getMinecartManiaChest((Chest)MinecartManiaWorld.getBlockAt(x + 1, y, z).getState());
        }
        if(MinecartManiaWorld.getBlockAt(x, y, z - 1).getType().equals(Material.CHEST)) {
        	return MinecartManiaWorld.getMinecartManiaChest((Chest)MinecartManiaWorld.getBlockAt(x, y, z - 1).getState());
        }
        if (MinecartManiaWorld.getBlockAt(x, y, z + 1).getType().equals(Material.CHEST)) {
        	return MinecartManiaWorld.getMinecartManiaChest((Chest)MinecartManiaWorld.getBlockAt(x, y, z + 1).getState());
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
		Inventory inventory = chest.getInventory();
		//Backup contents
		ItemStack[] backup = inventory.getContents().clone();
		ItemStack backupItem = new ItemStack(item.getTypeId(), item.getAmount(), item.getDamage());
		
		for (int i = 0; i < inventory.getSize(); i++) {
			if (inventory.getItem(i) != null) {
				if (inventory.getItem(i).getTypeId() == item.getTypeId()) {
					if (inventory.getItem(i).getAmount() + item.getAmount() <= 64) {
						inventory.setItem(i, new ItemStack(item.getTypeId(), inventory.getItem(i).getAmount() + item.getAmount()));
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
		
		if (getNeighborChest() != null && getDataValue("neighbor") == null) {
			//prevent infinite recursion
			getNeighborChest().setDataValue("neighbor", Boolean.TRUE);
			if (getNeighborChest().addItem(item)) {
				return true;
			}
		}
		else {
			setDataValue("neighbor", null);
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
		return addItem(new ItemStack(type));
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
		Inventory inventory = chest.getInventory();
		//Backup contents
		ItemStack[] backup = inventory.getContents().clone();
		
		for (int i = 0; i < inventory.getSize(); i++) {
			if (inventory.getItem(i) != null) {
				if (inventory.getItem(i).getTypeId() == type) {
					if (inventory.getItem(i).getAmount() - amount > 0) {
						inventory.setItem(i, new ItemStack(type, inventory.getItem(i).getAmount() - amount));
						return true;
					}
					else if (inventory.getItem(i).getAmount() - amount == 0) {
						inventory.remove(i);
						return true;
					}
					else{
						amount -=  inventory.getItem(i).getAmount();
						inventory.remove(i);
					}
				}
			}
		}
		
		if (getNeighborChest() != null && getDataValue("neighbor") == null) {
			//prevent infinite recursion
			getNeighborChest().setDataValue("neighbor", Boolean.TRUE);
			if (getNeighborChest().removeItem(type, amount)) {
				return true;
			}
		}
		else {
			setDataValue("neighbor", null);
		}
			
		//if we fail, reset the inventory back to previous values
		inventory.setContents(backup);
		return false;
	}
	
	/**
	 ** attempts to remove a single item type from this chest. If it fails, it will not alter the chests previous contents.
	 **/
	public boolean removeItem(int type) {
		return removeItem(type, 1);
	}

	public void setRedstonePower(boolean redstonePower) {
		RedstonePower = redstonePower;
	}

	public boolean isRedstonePower() {
		return RedstonePower;
	}

}
