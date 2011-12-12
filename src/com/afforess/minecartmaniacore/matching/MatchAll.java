package com.afforess.minecartmaniacore.matching;

import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.utils.StringUtils;

public class MatchAll implements MatchToken {
    
    public boolean match(ItemStack item) {
        return true;
    }
    
    public boolean isComplex() {
        return true;
    }

    public String toString(int i) {
        return StringUtils.indent("ALL ITEMS",i);
    }
    
}
