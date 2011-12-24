package com.afforess.minecartmaniacore.matching;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.utils.StringUtils;

public class MatchAND implements MatchToken {
    ArrayList<MatchToken> tokens = new ArrayList<MatchToken>();
    private int amount;
    
    public void addExpression(final MatchToken token) {
        tokens.add(token);
    }
    
    /**
     * Same deal as MatchItems.match, except OR
     */
    public boolean match(final ItemStack item) {
        for (final MatchToken match : tokens) {
            if (!match.match(item))
                return false;
        }
        return true;
    }
    
    public boolean isComplex() {
        return true;
    }
    
    public String toString(final int i) {
        final StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.indent("AND:\n", i));
        sb.append(StringUtils.indent("{\n", i));
        for (final MatchToken mt : tokens) {
            sb.append(mt.toString(i + 1));
            sb.append("\n");
        }
        sb.append(StringUtils.indent("}", i));
        return sb.toString();
    }
    
    public int getAmount() {
        return amount;
    }
    
    public void setAmount(final int amt) {
        amount = amt;
    }
    
}
