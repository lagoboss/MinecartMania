package com.afforess.minecartmaniacore;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class MinecartManiaSingleContainer implements MinecartManiaInventory{
	private Inventory inventory;
	public MinecartManiaSingleContainer(Inventory i) {
		inventory = i;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public void updateInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	@Override
	public boolean contains(Material m) {
		return contains(m.getId(), (short) -1);
	}
	
	public boolean contains(Item i){
		return contains(i.getId(), (short) (i.hasData() ? i.getData() : -1));
	}

	@Override
	public boolean contains(int type) {
		return contains(type, (short) -1);
	}
	
	@Override
	public boolean contains(int type, short durability) {
		for (int i = 0; i < size(); i++) {
			if (getItem(i) != null) {
				if (getItem(i).getTypeId() == type && ((durability == -1 || getItem(i).getDurability() == -1) || (getItem(i).getDurability() == durability))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 ** attempts to add an itemstack to this container. It adds items in a 'smart' manner, merging with existing itemstacks, until they
	 ** reach the maximum size (64). If it fails, it will not alter the container's previous contents.
	 ** Causes of failure: Full container
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
						inventory.setItem(i, new ItemStack(item.getTypeId(), 64, item.getDurability()));
						item = new ItemStack(item.getTypeId(), diff, item.getDurability());
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
	 ** attempts to add a single item to this container. If it fails, it will not alter the container's previous contents
	 ** @param itemtype to add
	 **/
	public boolean addItem(int type) {
		return addItem(new ItemStack(type, 1));
	}
	
	/**
	 ** attempts to add an itemstack to this container. If it fails, it will not alter the container's previous contents
	 ** @param itemtype to add
	 ** @param the amount to add
	 **/
	public boolean addItem(int type, int amount) {
		return addItem(new ItemStack(type, amount));
	}
	
	/**
	 ** attempts to remove the specified amount of an item type from this container. If it fails, it will not alter the containers previous contents.
	 ** @param itemtype to remove
	 ** @param the amount to remove
	 ** @param the durability of the item to remove (-1 for generic durability)
	 **/
	public boolean removeItem(int type, int amount, short durability) {
		Inventory inventory = getInventory();
		//Backup contents
		ItemStack[] backup = inventory.getContents().clone();
		
		for (int i = 0; i < inventory.getSize(); i++) {
			if (inventory.getItem(i) != null) {
				if (inventory.getItem(i).getTypeId() == type && (durability == -1 || (getItem(i).getDurability() == durability))) {
					if (inventory.getItem(i).getAmount() - amount > 0) {
						inventory.setItem(i, new ItemStack(type, inventory.getItem(i).getAmount() - amount, durability));
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
	 ** attempts to remove the specified amount of an item type from this container. If it fails, it will not alter the containers previous contents.
	 ** @param itemtype to remove
	 ** @param the amount to remove
	 **/
	public boolean removeItem(int type, int amount) {
		return removeItem(type, amount, (short) -1);
	}
	
	/**
	 ** attempts to remove a single item type from this container. If it fails, it will not alter the containers previous contents.
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

	@Override
	public ItemStack[] getContents() {
		return getInventory().getContents();
	}

	@Override
	public int first(Material m) {
		return getInventory().first(m);
	}
	
	public int first(Item item) {
		for (int i = 0; i < inventory.getSize(); i++) {
			if (inventory.getItem(i) != null) {
				if (inventory.getItem(i).getTypeId() == item.getId() && inventory.getItem(i).getDurability() == item.getData()) {
					return i;
				}
			}
		}
		return -1;
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
