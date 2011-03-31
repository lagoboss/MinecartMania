package com.afforess.minecartmaniacore.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.afforess.minecartmaniacore.Item;
import com.afforess.minecartmaniacore.config.ItemAliasList;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;
/**
 * Authors: Afforess, Meaglin
 */
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
			int index = str.indexOf("+");
			if (index == 1) {
				String dir = str.substring(0, 1);
				if (dir.equals("n")) direction = CompassDirection.NORTH;
				if (dir.equals("s")) direction = CompassDirection.SOUTH;
				if (dir.equals("e")) direction = CompassDirection.EAST;
				if (dir.equals("w")) direction = CompassDirection.WEST;
				str = str.substring(2,str.length()); // remove the direction for further parsing.
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
				String part = keys[i].trim();
				List<Item> parsedset = parsePart(part);
				
				if(parsedset == null || parsedset.size() < 1)
				    continue;
				
				for(Item item : parsedset){
					if (item == null) continue;
				    if(item.getAmount() == -2)
				        items.remove(item);
				    else if(item.getAmount() != -1) {
				        if(items.contains(item))
				            items.remove(item);
				        
				        items.add(item);
				    } else
				        items.add(item);
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
	
	private static final String AMOUNT = "@";
	private static final String RANGE = "-";
	private static final String REMOVE = "!";
	private static final String DATA = ";";
	
	/**
	 * Please don't change this order as it might screw up certain priorities!
	 * 
	 * @param part
	 * @return
	 */
	private static List<Item> parsePart(String part) {
	    try {
    	    if(part.contains(AMOUNT)) {
    	        return parseAmount(part);
    	    } else if(part.contains(REMOVE)) {
    	        return parseNegative(part);
    	    } else if(part.contains(DATA)) {
    	        return Arrays.asList(new Item[] {parseData(part)});
    	    } else if(part.contains(RANGE)) {
    	        return parseRange(part);
    	    } else {
    	        return parseNormal(part);
    	    }
	    } catch(Exception e) {
	        return null;
	    }
	}
	private static List<Item> parseAmount(String part){
	    String[] split   = part.split(AMOUNT);
	    List<Item> items = parsePart(split[0]);
	    
	    int amount = Integer.parseInt(split[1]);
	    for(Item item : items)
            item.setAmount(amount);
	    
	    return items;
	}
	private static List<Item> parseNegative(String part){
	    part = part.replace(REMOVE, "");
	    List<Item> items = parsePart(part);
	    
	    for(Item item : items)
	        item.setAmount(-2);
	    
	    return items;
	}
	private static List<Item> parseRange(String part){
	    String[] split   = part.split(RANGE);
	    List<Item> start = parseNormal(split[0]);
	    List<Item> end = parseNormal(split[1]);
        List<Item> items = new ArrayList<Item>();
        for(int item = start.get(0).getId();item <= end.get(0).getId();item++) {
            items.addAll(Item.getItem(item));
        }
        return items;
	}
	private static Item parseData(String part){
	    String[] split   = part.split(DATA);
	    List<Item> items = parsePart(split[0]);
	    int data = Integer.parseInt(split[1]);
	    for(Item item : items)
	        if(item.getData() == data)
	            return item;
	    
	    return null;
	}
	private static List<Item> parseNormal(String part){
	    try {
	        return Item.getItem(Integer.parseInt(part));
	    } catch(NumberFormatException exception) {
	        List<Item> alias = ItemAliasList.getItemsForAlias(part);
	        if(alias.size() > 0)
	            return alias;
	        
	        Item best = null;
            for (Item e : Item.values()) {
                if (e != null) {
                    String item = e.toString().toLowerCase();
                    if (item.contains(part)) {
                        //If two items have the same partial string in them (e.g diamond and diamond shovel) the shorter name wins
                        if (best == null || item.length() < best.toString().length()) {
                            best = e;
                        }
                    }
                }
            }
            return Arrays.asList(new Item[] {best});
	    }
	}
}
