package com.afforess.minecartmaniacore.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.afforess.minecartmaniacore.world.SpecificMaterial;

public class ItemAliasList {
    public static ConcurrentHashMap<String, List<SpecificMaterial>> aliases = new ConcurrentHashMap<String, List<SpecificMaterial>>();
    
    public static boolean isAlias(String alias) {
        Iterator<Entry<String, List<SpecificMaterial>>> i = aliases.entrySet().iterator();
        while (i.hasNext()) {
            Entry<String, List<com.afforess.minecartmaniacore.world.SpecificMaterial>> e = i.next();
            String key = e.getKey();
            if (key.equalsIgnoreCase(alias)) {
                return true;
            }
        }
        return false;
    }
    
    public static List<SpecificMaterial> getItemsForAlias(String alias) {
        Iterator<Entry<String, List<SpecificMaterial>>> i = aliases.entrySet().iterator();
        while (i.hasNext()) {
            Entry<String, List<SpecificMaterial>> e = i.next();
            String key = e.getKey();
            if (key.equalsIgnoreCase(alias)) {
                return e.getValue();
            }
        }
        return new ArrayList<SpecificMaterial>();
    }
}
