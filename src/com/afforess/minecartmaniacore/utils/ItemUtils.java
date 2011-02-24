package com.afforess.minecartmaniacore.utils;

import java.util.ArrayList;

import org.bukkit.Material;

public class ItemUtils {
	
	/**
	 * Returns the first item name or id found in the given string, or null if there was no item name or id.
	 * If the item name is a partial name, it will match the name with the shortest number of letter.
	 * Ex ("reds" will match "redstone" (the wire item) and not redstone ore.
	 * @return material found, or null
	 */
	public static Material getFirstItemStringToMaterial(String str) {
		Material items[] = getItemStringToMaterial(str);
		return items == null ? null : items[0];
	}

	/**
	 * Returns the list of material for each item name or id found in the given string, or null if there was no item name or id.
	 * If the item name is a partial name, it will match the name with the shortest number of letter.
	 * Ex ("reds" will match "redstone" (the wire item) and not redstone ore.
	 * @return materials found, or null
	 */
	public static Material[] getItemStringToMaterial(String str) {
		ArrayList<Material> items = new ArrayList<Material>();
		str = StringUtils.removeBrackets(str.toLowerCase());
		
		String[] keys = str.split("-| ?: ?");
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] == null || keys[i].isEmpty()) continue;
			Material type = null;
			//Parse the numbers first. Can be separated by "-" or ":"
			try {
				String num = StringUtils.getNumber(keys[i]);
				int id = Integer.parseInt(num);
				type = Material.getMaterial(id);
			}
			catch (Exception exception) {
				//Now try string names
				keys[i] = keys[i].replace(' ','_');
				Material bestMaterial = null;
				for (Material e : Material.values()) {
					if (e != null) {
						String item = e.toString().toLowerCase();
						if (item.contains(keys[i])) {
							//If two items have the same partial string in them (e.g diamond and diamond shovel) the shorter name wins
							if (bestMaterial == null || item.length() < bestMaterial.toString().length()) {
								bestMaterial = e;
							}
						}
					}
				}
				type = bestMaterial;
			}
			if (type != null) {
				items.add(type);
			}
		}

		Material itemList[] = new Material[items.size()];
		return items.toArray(itemList);
	}
}
