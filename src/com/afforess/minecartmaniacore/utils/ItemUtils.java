package com.afforess.minecartmaniacore.utils;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.config.CoreSettingParser;
import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;
import com.afforess.minecartmaniacore.matching.MatchAND;
import com.afforess.minecartmaniacore.matching.MatchAll;
import com.afforess.minecartmaniacore.matching.MatchBit;
import com.afforess.minecartmaniacore.matching.MatchConstant;
import com.afforess.minecartmaniacore.matching.MatchField;
import com.afforess.minecartmaniacore.matching.MatchNOT;
import com.afforess.minecartmaniacore.matching.MatchOR;
import com.afforess.minecartmaniacore.matching.MatchToken;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;
import com.afforess.minecartmaniacore.world.Item;
import com.afforess.minecartmaniacore.world.SpecificMaterial;

/**
 * Authors: Afforess, Meaglin, N3X15
 */
public class ItemUtils {
    
    private static HashMap<String, ItemMatcher> preparsed = new HashMap<String, ItemMatcher>();
    
    /**
     * Returns the first item name or id found in the given string, or null if there was no item name or id. If the item name is a partial name, it will match the name with the shortest number of letter. Ex ("reds" will match "redstone" (the wire item) and not redstone ore.
     * 
     * @return material found, or null
     */
    public static SpecificMaterial getFirstItemStringToMaterial(final String str) {
        final String[] list = { str };
        final SpecificMaterial items[] = getItemStringListToMaterial(list);
        return items.length == 0 ? null : items[0];
    }
    
    /**
     * Returns the list of material for each item name or id found in the given string, or an empty array if there was no item name or id. If the item name is a partial name, it will match the name with the shortest number of letter. Ex ("reds" will match "redstone" (the wire item) and not redstone ore.
     * 
     * @return materials found, or an empty array
     */
    public static SpecificMaterial[] getItemStringToMaterial(final String str) {
        final String[] list = { str };
        return getItemStringListToMaterial(list);
    }
    
    public static SpecificMaterial[] getItemStringListToMaterial(final String[] list) {
        return getItemStringListToMaterial(list, null);
    }
    
    public static CompassDirection getLineItemDirection(final String str) {
        CompassDirection direction = CompassDirection.NO_DIRECTION;
        final int index = str.indexOf("+");
        if (index == 1) {
            final String dir = str.substring(0, 1);
            if (dir.equalsIgnoreCase("n")) {
                direction = CompassDirection.NORTH;
            }
            if (dir.equalsIgnoreCase("s")) {
                direction = CompassDirection.SOUTH;
            }
            if (dir.equalsIgnoreCase("e")) {
                direction = CompassDirection.EAST;
            }
            if (dir.equalsIgnoreCase("w")) {
                direction = CompassDirection.WEST;
            }
        }
        return direction;
    }
    
    /**
     * Returns the list of material for each item name or id found in the given array of strings, or an empty array if there was no item names or ids. If the item name is a partial name, it will match the name with the shortest number of letter. If there is a '!' next to a id or item name it will be removed from the list Ex ("reds" will match "redstone" (the wire item) and not redstone ore.
     * 
     * @return materials found, or an empty array
     */
    public static SpecificMaterial[] getItemStringListToMaterial(final String[] list, final CompassDirection facing) {
        final ArrayList<SpecificMaterial> items = new ArrayList<SpecificMaterial>();
        for (final String element : list) {
            String str = StringUtils.removeBrackets(element.toLowerCase());
            str = str.trim();
            if (str.isEmpty()) {
                continue;
            }
            
            //Check the given direction and intended direction from the sign
            final CompassDirection direction = getLineItemDirection(str);
            if (direction != CompassDirection.NO_DIRECTION) {
                str = str.substring(2, str.length()); // remove the direction for further parsing.
            }
            if ((facing != null) && (direction != facing) && (direction != CompassDirection.NO_DIRECTION)) {
                continue;
            }
            
            //short circuit if it's everything
            if (str.contains("all items")) {
                for (final Material m : Material.values()) {
                    if (!items.contains(m)) {
                        items.add(new SpecificMaterial(m.getId(), (short) 0));
                    }
                }
            }
            final String[] keys = str.split(":");
            
            for (final String key : keys) {
                final String part = key.trim();
                ItemMatcher matcher = null;
                // Cache parsed strings
                if (preparsed.containsKey(part.toLowerCase())) {
                    matcher = preparsed.get(part.toLowerCase());
                }
                if (matcher == null) {
                    matcher = parsePart(part);
                }
                
                if (matcher == null) {
                    continue;
                }
                
                preparsed.put(part.toLowerCase(), matcher);
                saveDebugMap();
                
                for (final Material mat : Material.values()) {
                    if (matcher.match(new ItemStack(mat))) {
                        items.add(new SpecificMaterial(mat.getId(), (short) 0));
                    }
                }
            }
        }
        
        //Remove Air from the list
        final Iterator<SpecificMaterial> i = items.iterator();
        while (i.hasNext()) {
            final SpecificMaterial type = i.next();
            if ((type == null) || type.equals(Material.AIR)) {
                i.remove();
            }
        }
        
        final SpecificMaterial itemList[] = new SpecificMaterial[items.size()];
        return items.toArray(itemList);
    }
    
    public enum TYPE {
        AMOUNT("@"),
        REMOVE("!"),
        RANGE("-"),
        DATA(";"),
        BIT("&"),
        NONE("");
        
        private final String tag;
        
        TYPE(final String tag) {
            this.tag = tag;
        }
        
        public String getTag() {
            return tag;
        }
        
        @Override
        public String toString() {
            return tag;
        }
        
        public static TYPE getType(final String part) {
            if (part.contains(RANGE.getTag())) // Range is parsed first Always!
                return RANGE;
            if (part.contains(REMOVE.getTag())) // since this 1 doesn't need special priority handling
                return REMOVE;
            
            return (part.lastIndexOf(DATA.getTag()) > part.lastIndexOf(AMOUNT.getTag()) ? DATA : (part.contains(AMOUNT.getTag()) ? AMOUNT : NONE));
        }
    }
    
    public static ItemMatcher parsePart(String part) {
        
        part = StringUtils.removeBrackets(part);
        try {
            ItemMatcher itemMatcher = null;
            switch (TYPE.getType(part)) {
                case RANGE:
                    itemMatcher = parseRange(part);
                    break;
                case BIT:
                    itemMatcher = parseBit(part);
                    break;
                case DATA:
                    itemMatcher = parseData(part);
                    break;
                case REMOVE:
                    itemMatcher = parseNegative(part);
                    break;
                case AMOUNT:
                    itemMatcher = parseAmount(part);
                    break;
                default:
                    itemMatcher = parseNormal(part);
                    break;
            }
            return itemMatcher;
        } catch (final Exception e) {
            MinecartManiaLogger.getInstance().severe("Error when generating ItemMatcher for \"%s\":\n" + e.toString(), true, part);
            e.printStackTrace();
            return null;
        }
    }
    
    private static void saveDebugMap() {
        try {
            final File items = new File(MinecartManiaCore.dataDirectory + File.separator + "ItemMatchingTable.txt");
            final PrintWriter out = new PrintWriter(items);
            // Create file 
            out.write("This simply craps out the structure of Minecart Mania's internal Item Matching criteria.  It's not read by MM, and it's going to be overwritten as minecarts find new stuff.");
            for (final Entry<String, ItemMatcher> matcher : preparsed.entrySet()) {
                if (matcher.getValue() == null) {
                    out.write(String.format("\n\n%s:\n(null)", matcher.getKey()));
                } else {
                    out.write(String.format("\n\n%s:\n%s", matcher.getKey(), matcher.getValue().toString()));
                }
            }
            //Close the output stream
            out.close();
        } catch (final Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static ItemMatcher parseAmount(final String part) {
        final String[] split = part.split(TYPE.AMOUNT.getTag());
        final ItemMatcher matcher = parsePart(split[0]);
        
        final int amount = Integer.parseInt(split[1]);
        if (amount > 0) {
            matcher.setAmount(amount);
        }
        
        return matcher;
    }
    
    private static ItemMatcher parseNegative(String part) {
        part = part.replace(TYPE.REMOVE.getTag(), "");
        final ItemMatcher items = parsePart(part);
        items.addExpression(new MatchNOT(items.getTokens()));
        //MinecartManiaLogger.getInstance().debug("Removing Item: " + item.type());
        return items;
    }
    
    /**
     * Get list of item matchers, given a range.
     * 
     * @param part
     * @return
     */
    private static ItemMatcher parseRange(final String part) {
        // Split into components
        final String[] split = part.split(TYPE.RANGE.getTag());
        final ItemMatcher matcher = new ItemMatcher();
        final ItemMatcher start = parsePart(split[0]);
        final ItemMatcher end = parsePart(split[1]);
        final ItemStack startitem = start.toItemStack();
        if (startitem == null)
            return null;
        final ItemStack enditem = end.toItemStack();
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
    
    private static ItemMatcher parseData(final String part) {
        final String[] split = part.split(TYPE.DATA.getTag());
        final ItemMatcher item = parsePart(split[0]);
        final int data = Integer.parseInt(split[1]);
        item.addConstant(MatchField.DURABILITY, data);
        return item;
    }
    
    private static ItemMatcher parseBit(final String part) {
        final String[] split = part.split(TYPE.BIT.getTag());
        boolean bitState = true;
        if (split[1].startsWith("~")) {
            split[1] = split[1].substring(1);
            bitState = false;
        }
        final ItemMatcher item = parsePart(split[0]);
        final int data = Integer.parseInt(split[1]);
        item.addExpression(new MatchBit(MatchField.DURABILITY, data, bitState));
        return item;
    }
    
    private static ItemMatcher parseNormal(final String part) {
        final ItemMatcher matcher = new ItemMatcher();
        try {
            matcher.addConstant(MatchField.TYPE_ID, Integer.parseInt(part));
            return matcher;
        } catch (final NumberFormatException exception) {
            final Material mat = Material.matchMaterial(part);
            if (mat == null) {
                matcher.addConstant(MatchField.TYPE_ID, -1); // Force the match to fail every time.
            } else {
                matcher.addConstant(MatchField.TYPE_ID, mat.getId());
            }
            return matcher;
        }
    }
    
    public static ItemMatcher[] getItemStringToMatchers(final String line, final CompassDirection facing) {
        
        String str = StringUtils.removeBrackets(line).toLowerCase();
        str = str.trim();
        if (str.isEmpty())
            return new ItemMatcher[0];
        
        //Check the given direction and intended direction from the sign
        final CompassDirection direction = getLineItemDirection(str);
        if (direction != CompassDirection.NO_DIRECTION) {
            str = str.substring(2, str.length()); // remove the direction for further parsing.
        }
        if ((facing != null) && (direction != facing) && (direction != CompassDirection.NO_DIRECTION))
            return new ItemMatcher[0];
        ItemMatcher matcher = new ItemMatcher();
        if (str.equalsIgnoreCase("all items")) {
            matcher.addExpression(new MatchAll());
        }
        
        final String[] lines = str.split(":");
        final ArrayList<ItemMatcher> matchers = new ArrayList<ItemMatcher>();
        final ArrayList<MatchNOT> not = new ArrayList<MatchNOT>();
        for (final String part : lines) {
            
            if (preparsed.containsKey(part.trim().toLowerCase())) {
                matchers.add(preparsed.get(part.trim().toLowerCase()));
            }
            matcher = parsePart(part.trim());
            if (matcher != null) {
                preparsed.put(part.trim().toLowerCase(), matcher);
                saveDebugMap();
                matchers.add(matcher);
            }
        }
        
        for (final ItemMatcher m : matchers) {
            for (final MatchToken mt : m.getTokens()) {
                if (mt instanceof MatchNOT) {
                    not.add((MatchNOT) mt);
                }
            }
        }
        final Iterator<ItemMatcher> it = matchers.iterator();
        while (it.hasNext()) {
            final ItemMatcher m = it.next();
            boolean isNot = false;
            
            for (final MatchToken mt : m.getTokens()) {
                if (mt instanceof MatchNOT) {
                    isNot = true;
                    break;
                }
            }
            if (isNot) {
                it.remove();
                continue;
            }
            
            for (final MatchNOT mn : not) {
                m.addExpression(mn);
            }
        }
        final ItemMatcher[] ret = new ItemMatcher[matchers.size()];
        matchers.toArray(ret);
        return ret;
    }
    
    public static ItemMatcher[] getItemStringListToMatchers(final String[] lines, final CompassDirection facing) {
        final ArrayList<ItemMatcher> matchers = new ArrayList<ItemMatcher>();
        for (final String line : lines) {
            for (final ItemMatcher matcher : getItemStringToMatchers(line, facing))
                if (matcher != null) {
                    matchers.add(matcher);
                }
        }
        
        final ItemMatcher[] ret = new ItemMatcher[matchers.size()];
        matchers.toArray(ret);
        return ret;
    }
    
    public static ItemMatcher[] getItemStringListToMatchers(final String[] lines) {
        return getItemStringListToMatchers(lines, CompassDirection.NO_DIRECTION);
    }
    
    public static void prefillAliases(final CoreSettingParser csp) {
        for (final Item item : Item.values()) {
            if (item.hasData()) {
                final ItemMatcher matcher = new ItemMatcher();
                matcher.addConstant(MatchField.TYPE_ID, item.getId());
                matcher.addConstant(MatchField.DURABILITY, item.getData());
                preparsed.put(item.name().toLowerCase(), matcher);
                final ArrayList<SpecificMaterial> mats = new ArrayList<SpecificMaterial>();
                mats.add(new SpecificMaterial(item.getId(), (short) item.getData()));
                csp.aliases.put(item.name(), mats);
            }
        }
    }
    
    public static void addParserAlias(final String aliasName, final ItemMatcher matcher) {
        preparsed.put(aliasName.toLowerCase(), matcher);
    }
    
    public static void addParserAlias(final String aliasName, final ArrayList<SpecificMaterial> values) {
        // Create a new ItemMatcher
        final ItemMatcher matcher = new ItemMatcher();
        // Make a new OR statement
        final MatchOR or = new MatchOR();
        for (final SpecificMaterial mat : values) {
            final MatchConstant type = new MatchConstant(MatchField.TYPE_ID, mat.id);
            if (mat.durability != -1) {
                // Create a new constant token and add it to an AND
                // This basically becomes "... || (typeID=={whatever} && data=={whatever}
                final MatchAND and = new MatchAND();
                and.addExpression(type);
                and.addExpression(new MatchConstant(MatchField.DURABILITY, mat.durability));
                or.addExpression(and);
            } else {
                or.addExpression(type);
            }
        }
        matcher.addExpression(or);
        ItemUtils.addParserAlias(aliasName, matcher);
    }
}
