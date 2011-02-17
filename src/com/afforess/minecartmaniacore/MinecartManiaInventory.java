package com.afforess.minecartmaniacore;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface MinecartManiaInventory {
	
	public boolean contains(Material m);
	
	public boolean contains(int type);
	
	public boolean addItem(ItemStack item);
	
	public boolean addItem(int type);
	
	public boolean addItem(int type, int amount);
	
	public boolean removeItem(int type, int amount);
	
	public boolean removeItem(int type);
	
	public ItemStack getItem(int slot);
	
	public void setItem(int slot, ItemStack item);
	
	public int firstEmpty();
	
	public int size();
	
	public Inventory getInventory();
	
	public ItemStack[] getContents();
}
