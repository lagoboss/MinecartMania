package com.afforess.minecartmaniacore;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * A temporary object that allows for easy use of accessing and altering the contents of a double chest. 
 * References are not safe, do not store them for later retrieval. Create them only as needed.
 * @author Cameron
 *
 */
public class MinecartManiaDoubleChest implements MinecartManiaInventory{

	private final MinecartManiaChest chest1;
	private final MinecartManiaChest chest2;
	public MinecartManiaDoubleChest(MinecartManiaChest left, MinecartManiaChest right) {
		this.chest1 = left;
		this.chest2 = right;
	}
	
	public boolean equals(Location loc) {
		if (loc.equals(chest1.getLocation())) {
			return true;
		}
		if (loc.equals(chest2.getLocation())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean contains(Material m) {
		return (chest1.contains(m) || chest2.contains(m));
	}

	@Override
	public boolean contains(int type) {
		return chest1.contains(type) || chest2.contains(type);
	}

	@Override
	public boolean addItem(ItemStack item) {
		return chest1.addItem(item);
	}

	@Override
	public boolean addItem(int type) {
		return chest1.addItem(type);
	}

	@Override
	public boolean addItem(int type, int amount) {
		return chest1.addItem(type, amount);
	}

	@Override
	public boolean removeItem(int type, int amount) {
		return chest1.removeItem(type, amount);
	}

	@Override
	public boolean removeItem(int type) {
		return chest1.removeItem(type);
	}

	@Override
	public ItemStack getItem(int slot) {
		if (slot < chest1.size()) {
			return chest1.getItem(slot);
		}
		return chest2.getItem(slot-chest1.size());
	}

	@Override
	public void setItem(int slot, ItemStack item) {
		if (slot < chest1.size()) {
			chest1.setItem(slot, item);
		}else {
			chest2.setItem(slot-chest1.size(), item);
		}
	}

	@Override
	public int firstEmpty() {
		if (chest1.firstEmpty() != -1) {
			return chest1.firstEmpty();
		}
		if (chest2.firstEmpty() != -1) {
			return chest2.firstEmpty()+chest1.size();
		}
		return -1;
	}

	@Override
	public int size() {
		return chest1.size()+chest2.size();
	}

	@Override
	public ItemStack[] getContents() {
		ItemStack[] contents = new ItemStack[size()];
		for (int i = 0; i < chest1.size(); i++) {
			contents[i] = chest1.getItem(i);
		}
		for (int i = 0; i < chest2.size(); i++) {
			contents[i+chest1.size()] = chest2.getItem(i);
		}
		return contents;
	}
	
	@Override
	public int first(Material m) {
		if (chest1.first(m) != -1) {
			return chest1.first(m);
		}
		if (chest2.first(m) != -1) {
			return chest2.first(m)+chest1.size();
		}
		return -1;
	}

	@Override
	public int first(int type) {
		if (chest1.first(type) != -1) {
			return chest1.first(type);
		}
		if (chest2.first(type) != -1) {
			return chest2.first(type)+chest1.size();
		}
		return -1;
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
