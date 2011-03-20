package com.afforess.minecartmaniacore.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.afforess.minecartmaniacore.Item;

//A hard coded class meant to show users how to create item aliases in the configuration
//Basically, I'm a terrible, terrible person
public class ItemAliasList implements SettingList{
	private static ConcurrentHashMap<String, List<Item>> aliases = new ConcurrentHashMap<String, List<Item>>();
	private int temp = -1;
	private String sTemp = "";
	private List<Item> list= null;
	
	public static boolean isAlias(String alias) {
		Iterator<Entry<String, List<Item>>> i = aliases.entrySet().iterator();
		while(i.hasNext()) {
			Entry<String, List<Item>> e = i.next();
			String key = e.getKey();
			if (key.equalsIgnoreCase(alias)) {
				return true;
			}
		}
		return false;
	}
	
	public static List<Item> getItemsForAlias(String alias) {
		Iterator<Entry<String, List<Item>>> i = aliases.entrySet().iterator();
		while(i.hasNext()) {
			Entry<String, List<Item>> e = i.next();
			String key = e.getKey();
			if (key.equalsIgnoreCase(alias)) {
				return e.getValue();
			}
		}
		return new ArrayList<Item>();
	}
	
	@Override
	public String getTagHeading() {
		return "ItemAliases";
	}

	@Override
	public String getTagName() {
		return "ItemAlias";
	}

	@Override
	public int getNumTags() {
		return 2;
	}

	@Override
	public String getTag(int tag) {
		if (tag == 0) {
			return "AliasName";
		}
		return "ItemType";
	}
	
	@Override
	public boolean isRepeatingTag(int tag) {
		return tag != 0;
	}

	@Override
	public String getTagComment(int tag) {
		if (tag == 0) {
			return "The Custom Alias for the list of items that follows it. \n\t\t\tNot nessecary, but useful for shortening a long list of items into a short phrase.\n\t\t\tAvoid using already taken item names, or taken item id's, as the alias will take priority over the existing name!";
		}
		if (tag == 1) {
			return "The Item Name or Item Id and Data Value"; 
		}
		return null;
	}

	@Override
	public int getNumElements() {
		return 2;
	}

	@Override
	public Object getElementForTag(int tag, int element) {
		if (tag == 0) {
			switch(element) {
			case 0: return "Ores";
			case 1: return Item.GOLD_ORE.toString();
			case 2: return Item.IRON_ORE.toString();
			case 3: return Item.COAL_ORE.toString();
			case 4: return Item.LAPIS_ORE.toString();
			}
		}
		else if (tag == 1) {
			switch(element) {
			case 0: return "Food";
			case 1: return "260";
			case 2: return "297";
			case 3: return "322";
			case 4: return "354";
			case 5: return "350";
			case 6: return "319";
			case 7: return "320";
			}
		}
		return null;
	}

	@Override
	public boolean isItemTag(int tag) {
		return tag != 0;
	}

	@Override
	public void setElementForTag(int tag, int element, Object value) {
		if (temp != element && value != null) {
			if (list != null) {
				aliases.put(sTemp, list);
			}
			sTemp = (String)value;
			list = new ArrayList<Item>();
			temp = element;
		}
		else {
			list.add((Item)value);
			aliases.put(sTemp, list);
		}
		
	}

}
