package com.afforess.minecartmaniacore.matching;

import org.bukkit.inventory.ItemStack;

/**
 * Comparison providers for single criterions.
 * 
 * ALL MatchTokens must match before ItemMatcher.match() returns true (AND).
 * 
 * @author Rob
 * 
 */
public interface MatchToken {
    boolean match(ItemStack item);
    
    boolean isComplex();
    
    String toString(int i);
}