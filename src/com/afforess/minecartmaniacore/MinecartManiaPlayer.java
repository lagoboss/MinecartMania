package com.afforess.minecartmaniacore;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MinecartManiaPlayer implements MinecartManiaInventory{
	private final String name;
	private String lastStation = "";
	private ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<String,Object>();
	public MinecartManiaPlayer(String name) {
		this.name = name;
	}
	
	/** 
	 * Returns the player that this MinecartManiaPlayer represents
	 * MinecartManiaPlayer' represent a player in whatever state they are in, online or offline. Because of this, getPlayer will return null when offline.
	 * @return player
	 */
	public Player getPlayer() {
		return MinecartManiaCore.server.getPlayer(name);
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isOnline() {
		return getPlayer() != null;
	}
	
	public String getLastStation() {
		return lastStation;
	}
	
	public void setLastStation(String s) {
		lastStation = s;
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
	 
	 @Override
		public boolean contains(Material m) {
			return contains(m.getId());
		}

		@Override
		public boolean contains(int type) {
			for (int i = 0; i < size(); i++) {
				if (getItem(i) != null) {
					if (getItem(i).getTypeId() == type) {
						return true;
					}
				}
			}
			return false;
		}

		/**
		 ** attempts to add an itemstack to this furnace. It adds items in a 'smart' manner, merging with existing itemstacks, until they
		 ** reach the maximum size (64). If it fails, it will not alter the furnace's previous contents.
		 ** Causes of failure: Full furnace
		 ** @param item to add
		 **/
		public boolean addItem(ItemStack item) {
			if (item == null) {
				return true;
			}
			if (item.getTypeId() == Material.AIR.getId()) {
				return false;
			}
			Inventory inventory = getInventory();
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
				return true;
			}
				
			//if we fail, reset the inventory and item back to previous values
			inventory.setContents(backup);
			item = backupItem;
			return false;
		}
		
		/**
		 ** attempts to add a single item to this furnace. If it fails, it will not alter the furnace's previous contents
		 ** @param itemtype to add
		 **/
		public boolean addItem(int type) {
			return addItem(new ItemStack(type, 1));
		}
		
		/**
		 ** attempts to add an itemstack to this furnace. If it fails, it will not alter the furnace's previous contents
		 ** @param itemtype to add
		 ** @param the amount to add
		 **/
		public boolean addItem(int type, int amount) {
			return addItem(new ItemStack(type, amount));
		}
		
		/**
		 ** attempts to remove the specified amount of an item type from this furnace. If it fails, it will not alter the furnaces previous contents.
		 ** @param itemtype to remove
		 ** @param the amount to remove
		 **/
		public boolean removeItem(int type, int amount) {
			Inventory inventory = getInventory();
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
							inventory.clear(i);
							return true;
						}
						else{
							amount -=  inventory.getItem(i).getAmount();
							inventory.clear(i);
						}
					}
				}
			}

			//if we fail, reset the inventory back to previous values
			inventory.setContents(backup);
			return false;
		}
		
		/**
		 ** attempts to remove a single item type from this furnace. If it fails, it will not alter the furnaces previous contents.
		 ** @param item type to remove
		 **/
		public boolean removeItem(int type) {
			return removeItem(type, 1);
		}

		@Override
		public ItemStack getItem(int slot) {
			ItemStack i = getInventory().getItem(slot);
			//WTF is it with bukkit and returning air instead of null?
			return i == null ? null : (i.getTypeId() == Material.AIR.getId() ? null : i);
		}

		@Override
		public void setItem(int slot, ItemStack item) {
			getInventory().setItem(slot, item);
		}

		@Override
		public int firstEmpty() {
			return getInventory().firstEmpty();
		}

		@Override
		public int size() {
			return getInventory().getSize();
		}

		public Inventory getInventory() {
			return getPlayer().getInventory();
		}

		@Override
		public ItemStack[] getContents() {
			return getInventory().getContents();
		}

		@Override
		public int first(Material m) {
			return getInventory().first(m);
		}

		@Override
		public int first(int type) {
			return getInventory().first(type);
		}

		@Override
		public boolean isEmpty() {
			for (ItemStack i : getContents()) {
				//I hate you too, air.
				if (i != null && i.getType() != Material.AIR) {
					return false;
				}
			}
			return true;
		}

}
