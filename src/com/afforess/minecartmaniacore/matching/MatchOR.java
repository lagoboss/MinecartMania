package com.afforess.minecartmaniacore.matching;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.utils.StringUtils;

public class MatchOR implements MatchToken {
    ArrayList<MatchToken> tokens = new ArrayList<MatchToken>();
    
    public void addExpression(MatchToken token) {
        tokens.add(token);
    }
    
    /**
     * Same deal as MatchItems.match, except OR
     */
    public boolean match(ItemStack item) {
        for (MatchToken match : tokens) {
            if (match.match(item))
                return true;
        }
        return false;
    }
    
    public boolean isComplex() {
        return true;
    }
    
    public String toString(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.indent("OR:\n{", i));
        for (MatchToken mt : tokens) {
            sb.append(mt.toString(i + 1));
        }
        sb.append(StringUtils.indent("\n}", i));
        return sb.toString();
    }
}
