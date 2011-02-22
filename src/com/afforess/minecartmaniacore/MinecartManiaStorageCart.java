package com.afforess.minecartmaniacore;

import org.bukkit.Material;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MinecartManiaStorageCart extends MinecartManiaMinecart implements MinecartManiaInventory{

	public MinecartManiaStorageCart(Minecart cart) {
		super(cart);
	}
	
	public MinecartManiaStorageCart(Minecart cart, String owner) {
		super(cart, owner);
	}
	
	public Inventory getInventory() {
		return ((StorageMinecart)minecart).getInventory();
	}
	
	/**
	 ** attempts to add an itemstack to this storage chest. It adds items in a 'smart' manner, merging with existing itemstacks, until they
	 ** reach the maximum size (64). If it fails, it will not alter the storage chest's previous contents.
	 ** Causes of failure: Full storage chest
	 ** @param item to add
	 **/
	public boolean addItem(ItemStack item) {
		if (item == null) {
			return true;
		}
		if (item.getTypeId() == Material.AIR.getId()) {
			return true;
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
	 ** attempts to add a single item to this storage chest. If it fails, it will not alter the storage chest's previous contents
	 ** @param itemtype to add
	 **/
	public boolean addItem(int type) {
		return addItem(new ItemStack(type, 1));
	}
	
	/**
	 ** attempts to add an itemstack to this storage chest. If it fails, it will not alter the storage chest's previous contents
	 ** @param itemtype to add
	 ** @param the amount to add
	 **/
	public boolean addItem(int type, int amount) {
		return addItem(new ItemStack(type, amount));
	}
	
	/**
	 ** attempts to remove the specified amount of an item type from this storage chest. If it fails, it will not alter the storage chests previous contents.
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
	 ** attempts to remove a single item type from this storage chest. If it fails, it will not alter the storage chests previous contents.
	 ** @param item type to remove
	 **/
	public boolean removeItem(int type) {
		return removeItem(type, 1);
	}
	
	/**
	 ** Returns true if this storage chest or it's neighbor storage chest contains an itemstack of the given material
	 ** @param Material to search for
	 **/
	public boolean contains(Material type) {
		return contains(type.getId());
	}
	
	/**
	 ** Returns true if this storage chest or it's neighbor storage chest contains an itemstack of the given item id
	 ** @param item id to search for
	 **/
	public boolean contains(int type) {
		Inventory inventory = getInventory();
		for (int i = 0; i < inventory.getSize(); i++) {
			if (inventory.getItem(i) != null) {
				if (inventory.getItem(i).getTypeId() == type) {
					return true;
				}
			}
		}
		
		return false;
	}

	public int size() {
		return getInventory().getSize();
	}

	public ItemStack[] getContents() {
		return getInventory().getContents();
	}
	
	public ItemStack getItem(int slot) {
		return getInventory().getItem(slot);
	}

	public void setItem(int slot, ItemStack item) {
		if (item == null) {
			getInventory().clear(slot);
		}
		else {
			getInventory().setItem(slot, item);
		}
	}
	
	public int firstEmpty() {
		return getInventory().firstEmpty();
	}
	
	@Override
	public int first(Material m) {
		return getInventory().first(m);
	}

	@Override
	public int first(int type) {
		return getInventory().first(type);
	}
}
