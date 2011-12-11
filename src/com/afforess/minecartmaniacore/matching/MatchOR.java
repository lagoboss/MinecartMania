package com.afforess.minecartmaniacore.matching;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;


public class MatchOR implements MatchToken {
    ArrayList<MatchToken> tokens = new ArrayList<MatchToken>();
    
    public void addExpression(MatchToken token) {
        tokens.add(token);
    }
    
    /**
     * Same deal as MatchItems.match, except OR
     */
    public boolean match(ItemStack item) {
        for(MatchToken match : tokens) {
            if(match.match(item)) return true;
        }
        return false;
    }
    
    public boolean isComplex() {
        return true;
    }
    
}
