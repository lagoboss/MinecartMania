package com.afforess.minecartmaniacore.utils;

import junit.framework.TestCase;

import org.bukkit.inventory.ItemStack;

public class TestMatching extends TestCase {
    
    public void testItemMatcher() {
        // Should match all types of logs.
        ItemMatcher m = new ItemMatcher();
        m.parse("17");
        assertTrue("Failed to match 17;0", m.match(new ItemStack(17, 1, (short) 0)));
        assertTrue("Failed to match 17;1", m.match(new ItemStack(17, 1, (short) 1)));
        assertTrue("Failed to match 17;2", m.match(new ItemStack(17, 1, (short) 2)));
    }
    
}
