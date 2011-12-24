package com.afforess.minecartmaniacore.utils;

import junit.framework.TestCase;

import org.bukkit.inventory.ItemStack;

public class TestMatching extends TestCase {
    
    public void testParserInsensitiveDurability() {
        // Should match all types of logs.
        ItemMatcher m = new ItemMatcher();
        m.parse("17");
        assertTrue("Failed to match 17;0", m.match(new ItemStack(17, 1, (short) 0)));
        assertTrue("Failed to match 17;1", m.match(new ItemStack(17, 1, (short) 1)));
        assertTrue("Failed to match 17;2", m.match(new ItemStack(17, 1, (short) 2)));
    }
    
    public void testParserDurabilitySensitivity() {
        // Should match only match 17;0.
        ItemMatcher m = new ItemMatcher();
        m.parse("17;0");
        assertTrue("Failed to match 17;0", m.match(new ItemStack(17, 1, (short) 0)));
        assertFalse("Failed to NOT match 17;1", m.match(new ItemStack(17, 1, (short) 1)));
        assertFalse("Failed to NOT match 17;2", m.match(new ItemStack(17, 1, (short) 2)));
    }
    
    public void testAllItemsDirective() {
        // Should match everything.
        ItemMatcher m = new ItemMatcher();
        m.parse("all items");
        assertTrue("Failed to match 17;0", m.match(new ItemStack(17, 1, (short) 0)));
        assertTrue("Failed to match 373;8196", m.match(new ItemStack(373, 1, (short) 8196)));
    }
    
    public void testAllItemsPlusNOTDirective() {
        // Should match everything BUT logs.
        ItemMatcher m = new ItemMatcher();
        m.parse("all items:!17;0");
        assertFalse("Failed to NOT match 17;0", m.match(new ItemStack(17, 1, (short) 0)));
        assertTrue("Failed to match 373;8196", m.match(new ItemStack(373, 1, (short) 8196)));
    }
}
