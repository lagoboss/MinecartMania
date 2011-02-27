package com.afforess.minecartmaniacore;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MinecartManiaChest extends MinecartManiaSingleContainer implements MinecartManiaInventory{

	private final Location chest;
	private boolean redstonePower;
	private ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<String,Object>();
	public MinecartManiaChest(Chest chest) {
		super(chest.getInventory());
		this.chest = chest.getBlock().getLocation().clone();
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
		return (Chest)chest.getBlock().getState();
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
		//WTF is it with air
		if (item.getTypeId() == Material.AIR.getId()) {
			return false;
		}
		Inventory inventory =  getChest().getInventory();
		//Backup contents
		ItemStack[] backup = inventory.getContents().clone();
		ItemStack backupItem = new ItemStack(item.getTypeId(), item.getAmount(), item.getDurability());
		
		//First attempt to merge the itemstack with existing item stacks that aren't full (< 64)
		for (int i = 0; i < inventory.getSize(); i++) {
			if (inventory.getItem(i) != null) {
				if (inventory.getItem(i).getTypeId() == item.getTypeId() && inventory.getItem(i).getDurability() == item.getDurability()) {
					if (inventory.getItem(i).getAmount() + item.getAmount() <= 64) {
						inventory.setItem(i, new ItemStack(item.getTypeId(), inventory.getItem(i).getAmount() + item.getAmount(), item.getDurability()));
						return true;
					}
					else {
						int diff = inventory.getItem(i).getAmount() + item.getAmount() - 64;
						inventory.setItem(i, new ItemStack(item.getTypeId(), inventory.getItem(i).getAmount() + item.getAmount(), item.getDurability()));
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
	 ** attempts to remove the specified amount of an item type from this chest. If it fails, it will not alter the chests previous contents.
	 ** @param itemtype to remove
	 ** @param the amount to remove
	 ** @param the durability of the item to remove (-1 for generic durability)
	 **/
	public boolean removeItem(int type, int amount, short durability) {
		Inventory inventory =  getChest().getInventory();
		//Backup contents
		ItemStack[] backup = inventory.getContents().clone();
		
		for (int i = 0; i < inventory.getSize(); i++) {
			if (inventory.getItem(i) != null) {
				if (inventory.getItem(i).getTypeId() == type && (durability == -1 || (getItem(i).getDurability() == durability))) {
					if (inventory.getItem(i).getAmount() - amount > 0) {
						inventory.setItem(i, new ItemStack(type, inventory.getItem(i).getAmount() - amount, durability));
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
	 ** Returns true if this chest or it's neighbor chest contains an itemstack of the given item id
	 ** @param item id to search for
	 ** @param the durability of the item to remove (-1 for generic durability)
	 **/
	public boolean contains(int type, short durability) {
		for (int i = 0; i < size(); i++) {
			if (getItem(i) != null) {
				if (getItem(i).getTypeId() == type && (durability == -1 || (getItem(i).getDurability() == durability))) {
					return true;
				}
			}
		}
		
		MinecartManiaChest neighbor = getNeighborChest();
		if (neighbor != null) {
			//flag to prevent infinite recursion
			if (getDataValue("neighbor") == null) {
				neighbor.setDataValue("neighbor", Boolean.TRUE);
				if (neighbor.contains(type, durability)) {
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
		this.redstonePower = redstonePower;
	}

	public boolean isRedstonePower() {
		return redstonePower;
	}
	
	public void update() {
		 getChest().update();
	}
	
	public String toString() {
		return "[" + getX() + ":" + getY() + ":" + getZ() + "]";
	}

	public Inventory getInventory() {
		return getChest().getInventory();
	}
}
