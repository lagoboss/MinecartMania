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
import com.afforess.minecartmaniacore.matching.MatchAND;
import com.afforess.minecartmaniacore.matching.MatchConstant;
import com.afforess.minecartmaniacore.matching.MatchField;
import com.afforess.minecartmaniacore.matching.MatchOR;
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
                    matcher = new ItemMatcher();
                    matcher.parse(part);
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
            if (part.contains(BIT.getTag())) // Parse bit before data.
                return BIT;
            
            if (part.lastIndexOf(DATA.getTag()) > part.lastIndexOf(AMOUNT.getTag()))
                return DATA;
            else {
                if (part.contains(AMOUNT.getTag()))
                    return AMOUNT;
                else
                    return NONE;
            }
        }
    }
    
    private static void saveDebugMap() {
        try {
            final File items = new File(MinecartManiaCore.getDataDirectoryRelativePath() + File.separator + "ItemMatchingTable.txt");
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
    
    public static ItemMatcher[] getItemStringToMatchers(String line, final CompassDirection facing) {
        
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
        
        final ItemMatcher matcher = new ItemMatcher();
        final ArrayList<ItemMatcher> matchers = new ArrayList<ItemMatcher>();
        
        final String[] parts = line.split(":");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = StringUtils.removeBrackets(parts[i]).toLowerCase().trim();
        }
        line = StringUtils.join(parts, 0, ":");
        
        if (preparsed.containsKey(line)) {
            matchers.add(preparsed.get(line));
        }
        if (matcher.parse(line)) {
            preparsed.put(line, matcher);
            saveDebugMap();
            matchers.add(matcher);
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
