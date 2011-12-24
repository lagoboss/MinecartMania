package com.afforess.minecartmaniacore.tests;

import junit.framework.TestCase;

import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.utils.ItemMatcher;

public class TestMatching extends TestCase {
    
    public void testParserInsensitiveDurability() {
        // Should match all types of logs.
        final ItemMatcher m = new ItemMatcher();
        m.parse("17");
        System.out.println(m.toString());
        assertTrue("Failed to match 17;0", m.match(new ItemStack(17, 1, (short) 0)));
        assertTrue("Failed to match 17;1", m.match(new ItemStack(17, 1, (short) 1)));
        assertTrue("Failed to match 17;2", m.match(new ItemStack(17, 1, (short) 2)));
    }
    
    public void testParserDurabilitySensitivity() {
        // Should match only match 17;0.
        final ItemMatcher m = new ItemMatcher();
        m.parse("17;0");
        System.out.println(m.toString());
        assertTrue("Failed to match 17;0", m.match(new ItemStack(17, 1, (short) 0)));
        assertFalse("Failed to NOT match 17;1", m.match(new ItemStack(17, 1, (short) 1)));
        assertFalse("Failed to NOT match 17;2", m.match(new ItemStack(17, 1, (short) 2)));
    }
    
    public void testAllItemsDirective() {
        // Should match everything.
        final ItemMatcher m = new ItemMatcher();
        m.parse("all items");
        System.out.println(m.toString());
        assertTrue("Failed to match 17;0", m.match(new ItemStack(17, 1, (short) 0)));
        assertTrue("Failed to match 373;8196", m.match(new ItemStack(373, 1, (short) 8196)));
    }
    
    public void testAllItemsPlusNOTDirective() {
        // Should match everything BUT logs.
        final ItemMatcher m = new ItemMatcher();
        m.parse("all items:!17;0");
        System.out.println(m.toString());
        assertFalse("Failed to NOT match 17;0", m.match(new ItemStack(17, 1, (short) 0)));
        assertTrue("Failed to match 373;8196", m.match(new ItemStack(373, 1, (short) 8196)));
    }
    
    public void testRange() {
        // Should match 1 through 5, but NOT 2 through 3.
        final ItemMatcher m = new ItemMatcher();
        m.parse("1-5:!2-3");
        System.out.println(m.toString());
        assertTrue("Failed to match 1;0", m.match(new ItemStack(1, 1, (short) 0)));
        assertFalse("Failed to NOT match 2;0", m.match(new ItemStack(2, 1, (short) 0)));
        assertFalse("Failed to NOT match 3;0", m.match(new ItemStack(3, 1, (short) 0)));
        assertTrue("Failed to match 4;0", m.match(new ItemStack(4, 1, (short) 0)));
        assertTrue("Failed to match 5;0", m.match(new ItemStack(5, 1, (short) 0)));
    }
    
    public void testBitOperators() {
        // Bit 1 must be set.  Bit 2 must NOT be set. (Therefore it must be 1 and not 3.)
        final ItemMatcher m = new ItemMatcher();
        m.parse("373&1,~2");
        System.out.println(m.toString());
        assertTrue("Failed to match 373;1", m.match(new ItemStack(373, 1, (short) 1)));
        assertFalse("Failed to NOT match 373;3", m.match(new ItemStack(373, 1, (short) 3)));
    }
    
    public void testAmount() {
        // Ensure we're getting the correct amount out of this.
        final ItemMatcher m = new ItemMatcher();
        m.parse("373;0@64");
        System.out.println(m.toString());
        assertTrue("Failed to match 373;0", m.match(new ItemStack(373, 1, (short) 0)));
        assertEquals("Failed to get an amount of 64", 64, m.getAmount(-1));
    }
    
    public void testComplex() {
        // What I generally use for my autominer setup.
        // Rock, cobble, all ores + gravel and sand
        final ItemMatcher m = new ItemMatcher();
        m.parse("1:4:12-16:21:56:73-74");
        System.out.println(m.toString());
        assertTrue("Failed to match 1;0", m.match(new ItemStack(1, 1, (short) 0)));
        assertTrue("Failed to match 4;0", m.match(new ItemStack(4, 1, (short) 0)));
        assertTrue("Failed to match 12;0", m.match(new ItemStack(12, 1, (short) 0)));
        assertTrue("Failed to match 13;0", m.match(new ItemStack(13, 1, (short) 0)));
        assertTrue("Failed to match 16;0", m.match(new ItemStack(16, 1, (short) 0)));
        assertTrue("Failed to match 21;0", m.match(new ItemStack(21, 1, (short) 0)));
        assertTrue("Failed to match 56;0", m.match(new ItemStack(56, 1, (short) 0)));
        assertTrue("Failed to match 73;0", m.match(new ItemStack(73, 1, (short) 0)));
        assertTrue("Failed to match 74;0", m.match(new ItemStack(74, 1, (short) 0)));
        assertFalse("Failed to NOT match 373;0", m.match(new ItemStack(373, 1, (short) 0)));
    }
    
    public void testDefaultAmount() {
        // Ensure we're getting the correct amount out of this.
        final ItemMatcher m = new ItemMatcher();
        m.parse("373;0");
        System.out.println(m.toString());
        assertTrue("Failed to match 373;0", m.match(new ItemStack(373, 1, (short) 0)));
        assertEquals("Failed to get an amount of -1", -1, m.getAmount(-1));
    }
}
