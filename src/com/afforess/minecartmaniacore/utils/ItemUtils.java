package com.afforess.minecartmaniacore.utils;

import java.util.ArrayList;
import java.util.Iterator;

import com.afforess.minecartmaniacore.Item;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;

public class ItemUtils {
	
	/**
	 * Returns the first item name or id found in the given string, or null if there was no item name or id.
	 * If the item name is a partial name, it will match the name with the shortest number of letter.
	 * Ex ("reds" will match "redstone" (the wire item) and not redstone ore.
	 * @return material found, or null
	 */
	public static Item getFirstItemStringToMaterial(String str) {
		String[] list = {str};
		Item items[] = getItemStringListToMaterial(list);
		return items.length == 0 ? null : items[0];
	}
	
	/**
	 * Returns the list of material for each item name or id found in the given string, or an empty array if there was no item name or id.
	 * If the item name is a partial name, it will match the name with the shortest number of letter.
	 * Ex ("reds" will match "redstone" (the wire item) and not redstone ore.
	 * @return materials found, or an empty array
	 */
	public static Item[] getItemStringToMaterial(String str) {
		String[] list = {str};
		return getItemStringListToMaterial(list);
	}
	
	public static Item[] getItemStringListToMaterial(String[] list) {
		return getItemStringListToMaterial(list, CompassDirection.NO_DIRECTION);
	}

	/**
	 * Returns the list of material for each item name or id found in the given array of strings, or an empty array if there was no item names or ids.
	 * If the item name is a partial name, it will match the name with the shortest number of letter.
	 * If there is a '!' next to a id or item name it will be removed from the list
	 * Ex ("reds" will match "redstone" (the wire item) and not redstone ore.
	 * @return materials found, or an empty array
	 */
	public static Item[] getItemStringListToMaterial(String[] list, CompassDirection facing) {
		ArrayList<Item> items = new ArrayList<Item>();
		for (int line = 0; line < list.length; line++) {
			String str = StringUtils.removeBrackets(list[line].toLowerCase());
			str = str.trim();
			if (str.isEmpty()) {
				continue;
			}

			//Check the given direction and intended direction from the sign
			CompassDirection direction = CompassDirection.NO_DIRECTION;
			if (str.contains("+")) {
				String[] data = str.split("\\+");
				str = data[1];
				String dir = data[0].toLowerCase().trim();
				if (dir.contains("n")) direction = CompassDirection.NORTH;
				if (dir.contains("s")) direction = CompassDirection.SOUTH;
				if (dir.contains("e")) direction = CompassDirection.EAST;
				if (dir.contains("w")) direction = CompassDirection.WEST;
			}
			if (direction != facing && direction != CompassDirection.NO_DIRECTION) {
				continue;
			}
			
			//short circuit if it's everything
			if (str.contains("all items")) {
				for (Item m : Item.values()) {
					if (!items.contains(m)) {
						items.add(m);
					}
				}
				continue;
			}
			
			String[] keys = str.split(":");
			for (int i = 0; i < keys.length; i++) {
				if (keys[i] == null || keys[i].isEmpty()) continue;
				
				//List of items to process
				ArrayList<Item> toAdd = new ArrayList<Item>();
				
				//check if we need to remove this item
				boolean remove = keys[i].contains("!");
				if (remove) {
					keys[i] = keys[i].replace('!', ' ');
				}
				keys[i] = keys[i].trim();
				
				//Check if this is a set of items
				if (keys[i].contains("-")) {
					String[] set = keys[i].split("-");
					Item start = getFirstItemStringToMaterial(set[0]);
					Item end = getFirstItemStringToMaterial(set[1]);
					if (start != null && end != null) {
						for (int item = start.getId(); item <= end.getId(); item++) {
							toAdd.addAll(Item.getItem(item));
						}
						continue; //skip to the next item
					}
				}
				
				//Parse the numbers first. Can be separated by ":"
				try {
					//item with specific data value ("17;2" - item id, data value [redwood log])
					int data = -1;
					if (keys[i].contains(";")){
						String[] info = keys[i].split(";");
						String num = StringUtils.getNumber(info[1]);
						data = Integer.parseInt(num);
					}
					String num = StringUtils.getNumber(keys[i]);
					int id = Integer.parseInt(num);
					if (data != -1)
						toAdd.add(Item.getItem(id, data));
					else
						toAdd.addAll(Item.getItem(id));
				}
				catch (Exception exception) {
					//Now try string names
					keys[i] = keys[i].replace(' ','_');
					Item best = null;
					for (Item e : Item.values()) {
						if (e != null) {
							String item = e.toString().toLowerCase();
							if (item.contains(keys[i])) {
								//If two items have the same partial string in them (e.g diamond and diamond shovel) the shorter name wins
								if (best == null || item.length() < best.toString().length()) {
									best = e;
								}
							}
						}
					}
					toAdd.add(best);
				}
				
				//Now add or remove the items we processed
				for (Item type : toAdd) {
					if (type != null) {
						if (!remove) {
							items.add(type);
						}
						else {
							items.remove(type);
						}
					}
				}
			}
		}
		
		
		//Remove Air from the list
		Iterator<Item> i = items.iterator();
		while (i.hasNext()) {
			Item type = i.next();
			if (type == null || type.equals(Item.AIR)) {
				i.remove();
			}
		}
		
		Item itemList[] = new Item[items.size()];
		return items.toArray(itemList);
	}

}
