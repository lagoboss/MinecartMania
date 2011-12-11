package com.afforess.minecartmaniacore.matching;

import org.bukkit.inventory.ItemStack;

public class MatchAll implements MatchToken {
    
    public boolean match(ItemStack item) {
        return true;
    }
    
    public boolean isComplex() {
        return true;
    }
    
}
