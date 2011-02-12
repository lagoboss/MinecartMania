package com.afforess.minecartmaniacore;

import org.bukkit.Material;

public class ItemUtils {

	public static Material itemStringToMaterial(String str) {
		int wildcard = str.indexOf('*');
		if (wildcard > -1) {
			str = str.substring(0, wildcard-1);
		}
		if (str.indexOf('[') > -1) {
			str = str.substring(str.indexOf('[') + 1);
		}
		str = str.toLowerCase();
		//try and parse it as a number first
		try {
			String num = StringUtils.getNumber(str);
			int id = Integer.parseInt(num);
			Material type = Material.getMaterial(id);
			if (type != null) {
				return type;
			}
		}
		catch (Exception exception) {
			//ignore
		}
		
		Material bestMaterial = null;
		for (Material e : Material.values()) {
			if (e != null) {
				String item = e.toString().toLowerCase();
				if (item.contains(str)) {
					//If two items have the same partial string in them (e.g diamond and diamond shovel) the shorter name wins
					if (bestMaterial == null || e.toString().length() < bestMaterial.toString().length()) {
						bestMaterial = e;
					}
				}
			}
		}
		return bestMaterial;
	}
}
