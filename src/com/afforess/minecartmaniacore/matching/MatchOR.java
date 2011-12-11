package com.afforess.minecartmaniacore.matching;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;


public class MatchOR implements MatchToken {
    ArrayList<MatchToken> tokens = new ArrayList<MatchToken>();
    
    public void addExpression(MatchToken token) {
        tokens.add(token);
    }
    
    public boolean match(ItemStack item) {
        // TODO Auto-generated method stub
        return false;
    }
    
    public boolean isComplex() {
        // TODO Auto-generated method stub
        return true;
    }
    
}
