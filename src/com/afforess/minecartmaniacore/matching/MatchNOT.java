package com.afforess.minecartmaniacore.matching;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.utils.StringUtils;

public class MatchNOT implements MatchToken {
    MatchToken token = null;
    private int amount = -1;
    
    public MatchNOT(final List<MatchToken> tokens) {
        if (tokens.size() > 1) {
            // Assume AND
            final MatchAND and = new MatchAND();
            for (final MatchToken t : tokens) {
                and.addExpression(t);
            }
            token = and;
        } else if (tokens.size() == 1) {
            token = tokens.get(0);
        }
    }
    
    public MatchNOT(final MatchToken expr) {
        token = expr;
    }
    
    /**
     * Same as AND, but with booleans inverted.
     */
    public boolean match(final ItemStack item) {
        return !token.match(item);
    }
    
    public boolean isComplex() {
        return true;
    }
    
    public String toString(final int i) {
        final StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.indent("NOT:\n", i));
        sb.append(StringUtils.indent("{\n", i));
        sb.append(token.toString(i + 1));
        sb.append("\n");
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
