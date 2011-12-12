package com.afforess.minecartmaniacore.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.config.ItemAliasList;
import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;
import com.afforess.minecartmaniacore.matching.MatchAll;
import com.afforess.minecartmaniacore.matching.MatchConstant;
import com.afforess.minecartmaniacore.matching.MatchField;
import com.afforess.minecartmaniacore.matching.MatchOR;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;
import com.afforess.minecartmaniacore.world.SpecificMaterial;

/**
 * Authors: Afforess, Meaglin, N3X15
 */
public class ItemUtils {
    
    /**
     * Returns the first item name or id found in the given string, or null if there was no item name or id. If the item name is a partial name, it will match the name with the shortest number of letter. Ex ("reds" will match "redstone" (the wire item) and not redstone ore.
     * 
     * @return material found, or null
     */
    public static SpecificMaterial getFirstItemStringToMaterial(String str) {
        String[] list = { str };
        SpecificMaterial items[] = getItemStringListToMaterial(list);
        return items.length == 0 ? null : items[0];
    }
    
    /**
     * Returns the list of material for each item name or id found in the given string, or an empty array if there was no item name or id. If the item name is a partial name, it will match the name with the shortest number of letter. Ex ("reds" will match "redstone" (the wire item) and not redstone ore.
     * 
     * @return materials found, or an empty array
     */
    public static SpecificMaterial[] getItemStringToMaterial(String str) {
        String[] list = { str };
        return getItemStringListToMaterial(list);
    }
    
    public static SpecificMaterial[] getItemStringListToMaterial(String[] list) {
        return getItemStringListToMaterial(list, null);
    }
    
    public static CompassDirection getLineItemDirection(String str) {
        CompassDirection direction = CompassDirection.NO_DIRECTION;
        int index = str.indexOf("+");
        if (index == 1) {
            String dir = str.substring(0, 1);
            if (dir.equalsIgnoreCase("n"))
                direction = CompassDirection.NORTH;
            if (dir.equalsIgnoreCase("s"))
                direction = CompassDirection.SOUTH;
            if (dir.equalsIgnoreCase("e"))
                direction = CompassDirection.EAST;
            if (dir.equalsIgnoreCase("w"))
                direction = CompassDirection.WEST;
        }
        return direction;
    }
    
    /**
     * Returns the list of material for each item name or id found in the given array of strings, or an empty array if there was no item names or ids. If the item name is a partial name, it will match the name with the shortest number of letter. If there is a '!' next to a id or item name it will be removed from the list Ex ("reds" will match "redstone" (the wire item) and not redstone ore.
     * 
     * @return materials found, or an empty array
     */
    public static SpecificMaterial[] getItemStringListToMaterial(String[] list,
            CompassDirection facing) {
        ArrayList<SpecificMaterial> items = new ArrayList<SpecificMaterial>();
        for (int line = 0; line < list.length; line++) {
            String str = StringUtils.removeBrackets(list[line].toLowerCase());
            str = str.trim();
            if (str.isEmpty()) {
                continue;
            }
            
            //Check the given direction and intended direction from the sign
            CompassDirection direction = getLineItemDirection(str);
            if (direction != CompassDirection.NO_DIRECTION) {
                str = str.substring(2, str.length()); // remove the direction for further parsing.
            }
            if (facing != null && direction != facing && direction != CompassDirection.NO_DIRECTION) {
                continue;
            }
            
            //short circuit if it's everything
            if (str.contains("all items")) {
                for (Material m : Material.values()) {
                    if (!items.contains(m)) {
                        items.add(new SpecificMaterial(m.getId(), (short) 0));
                    }
                }
            }
            
            String[] keys = str.split(":");
            for (int i = 0; i < keys.length; i++) {
                String part = keys[i].trim();
                ItemMatcher matcher = parsePart(part);
                
                if (matcher == null)
                    continue;
                
                for (Material mat : Material.values()) {
                    if (matcher.match(new ItemStack(mat)))
                        items.add(new SpecificMaterial(mat.getId(), (short) 0));
                }
            }
        }
        
        //Remove Air from the list
        Iterator<SpecificMaterial> i = items.iterator();
        while (i.hasNext()) {
            SpecificMaterial type = i.next();
            if (type == null || type.equals(Material.AIR)) {
                i.remove();
            }
        }
        
        SpecificMaterial itemList[] = new SpecificMaterial[items.size()];
        return items.toArray(itemList);
    }
    
    protected enum TYPE {
        AMOUNT("@"),
        REMOVE("!"),
        RANGE("-"),
        DATA(";"),
        NONE("");
        
        private final String tag;
        
        TYPE(String tag) {
            this.tag = tag;
        }
        
        public String getTag() {
            return tag;
        }
        
        @Override
        public String toString() {
            return tag;
        }
        
        public static TYPE getType(String part) {
            if (part.contains(RANGE.getTag())) // Range is parsed first Always!
                return RANGE;
            if (part.contains(REMOVE.getTag())) // since this 1 doesn't need special priority handling
                return REMOVE;
            
            return (part.lastIndexOf(DATA.getTag()) > part.lastIndexOf(AMOUNT.getTag()) ? DATA : (part.contains(AMOUNT.getTag()) ? AMOUNT : NONE));
        }
    }
    
    private static ItemMatcher parsePart(String part) {
        if(part.contains("[")){
            part=StringUtils.removeBrackets(part);
        }
        try {
            switch (TYPE.getType(part)) {
                case RANGE:
                    return parseRange(part);
                case DATA:
                    return parseData(part);
                case REMOVE:
                    return parseNegative(part);
                case AMOUNT:
                    return parseAmount(part);
                default:
                    return parseNormal(part);
            }
        } catch (Exception e) {
            MinecartManiaLogger.getInstance().severe("Error when generating ItemMatcher for \"%s\":\n" + e.toString(), true, part);
            return null;
        }
    }
    
    private static ItemMatcher parseAmount(String part) {
        String[] split = part.split(TYPE.AMOUNT.getTag());
        ItemMatcher matcher = parsePart(split[0]);
        
        int amount = Integer.parseInt(split[1]);
        if (amount > 0) {
            matcher.setAmount(amount);
        }
        
        return matcher;
    }
    
    private static ItemMatcher parseNegative(String part) {
        part = part.replace(TYPE.REMOVE.getTag(), "");
        ItemMatcher items = parsePart(part);
        items.setAmount(-2);
        //MinecartManiaLogger.getInstance().debug("Removing Item: " + item.type());
        return items;
    }
    
    /**
     * Get list of item matchers, given a range.
     * 
     * @param part
     * @return
     */
    private static ItemMatcher parseRange(String part) {
        // Split into components
        String[] split = part.split(TYPE.RANGE.getTag());
        ItemMatcher matcher = new ItemMatcher();
        ItemMatcher start = parsePart(split[0]);
        ItemMatcher end = parsePart(split[1]);
        ItemStack startitem = start.toItemStack();
        if (startitem == null)
            return null;
        ItemStack enditem = end.toItemStack();
        if (enditem == null)
            return null;
        
        // If the ID is the same on both...
        if (startitem.getTypeId() == enditem.getTypeId()) {
            // Add a constant matcher.
            matcher.addConstant(MatchField.TYPE_ID, startitem.getTypeId());
        } else {
            // Add a range matcher.
            matcher.addRange(MatchField.TYPE_ID, startitem.getTypeId(), enditem.getTypeId());
        }
        
        // If the DATA value is the same on both...
        if (startitem.getDurability() == enditem.getDurability()) {
            // Add a constant matcher.
            matcher.addConstant(MatchField.DURABILITY, startitem.getDurability());
        } else {
            // Add a range matcher.
            matcher.addRange(MatchField.DURABILITY, startitem.getDurability(), enditem.getDurability());
        }
        
        if (startitem.getAmount() != -1) {
            matcher.setAmount(startitem.getAmount());
        } else if (enditem.getAmount() != -1) {
            matcher.setAmount(enditem.getAmount());
        }
        return matcher;
    }
    
    private static ItemMatcher parseData(String part) {
        String[] split = part.split(TYPE.DATA.getTag());
        ItemMatcher item = parsePart(split[0]);
        int data = Integer.parseInt(split[1]);
        item.addConstant(MatchField.DURABILITY, data);
        return item;
    }
    
    private static ItemMatcher parseNormal(String part) {
        ItemMatcher matcher = new ItemMatcher();
        try {
            matcher.addConstant(MatchField.TYPE_ID, Integer.parseInt(part));
            return matcher;
        } catch (NumberFormatException exception) {
            List<SpecificMaterial> alias = ItemAliasList.getItemsForAlias(part);
            if (alias.size() > 0)
                return materialListToItemMatcher(alias);
            /*
             * int best = -1; int bestLength = -1; for (Material e : Material.values()) { if (e != null) { String item = e.name().toLowerCase(); if (item.contains(part)) { //If two items have the same partial string in them (e.g diamond and diamond shovel) the shorter name wins if (best == -1 || item.length() < bestLength) { best = e.getId(); bestLength = e.name().length(); } } } }
             */
            Material mat = Material.matchMaterial(part);
            if (mat == null) // Can't find the material
                matcher.addConstant(MatchField.TYPE_ID, -1); // Force the match to fail every time.
            else
                matcher.addConstant(MatchField.TYPE_ID, mat.getId());
            return matcher;
        }
    }
    
    private static ItemMatcher materialListToItemMatcher(
            List<SpecificMaterial> materials) {
        MatchOR or = new MatchOR();
        for (SpecificMaterial m : materials) {
            or.addExpression(new MatchConstant(MatchField.TYPE_ID, m.id));
        }
        ItemMatcher match = new ItemMatcher();
        match.addExpression(or);
        return match;
    }
    
    public static ItemMatcher[] getItemStringToMatchers(String line,
            CompassDirection facing) {
        
        String str = StringUtils.removeBrackets(line).toLowerCase();
        str = str.trim();
        if (str.isEmpty()) {
            return new ItemMatcher[0];
        }
        
        //Check the given direction and intended direction from the sign
        CompassDirection direction = getLineItemDirection(str);
        if (direction != CompassDirection.NO_DIRECTION) {
            str = str.substring(2, str.length()); // remove the direction for further parsing.
        }
        if (facing != null && direction != facing && direction != CompassDirection.NO_DIRECTION) {
            return new ItemMatcher[0];
        }
        ItemMatcher matcher = new ItemMatcher();
        if (str == "all items") {
            matcher.addExpression(new MatchAll());
            return new ItemMatcher[] { matcher };
        }
        
        matcher = parsePart(line);
        if (matcher == null)
            return new ItemMatcher[0];
        return new ItemMatcher[] { matcher };
    }
    
    public static ItemMatcher[] getItemStringListToMatchers(String[] lines,
            CompassDirection facing) {
        ArrayList<ItemMatcher> matchers = new ArrayList<ItemMatcher>();
        for (String line : lines) {
            for (ItemMatcher matcher : getItemStringToMatchers(line, facing))
                if (matcher != null)
                    matchers.add(matcher);
        }
        
        ItemMatcher[] ret = new ItemMatcher[matchers.size()];
        matchers.toArray(ret);
        return ret;
    }
    
    public static ItemMatcher[] getItemStringListToMatchers(String[] lines) {
        return getItemStringListToMatchers(lines, CompassDirection.NO_DIRECTION);
    }
}
