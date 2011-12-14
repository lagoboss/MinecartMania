package com.afforess.minecartmaniacore.matching;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.utils.StringUtils;

public class MatchNOT implements MatchToken {
    MatchToken token = null;
    
    public MatchNOT(List<MatchToken> tokens) {
        if (tokens.size() > 1) {
            // Assume AND
            MatchAND and = new MatchAND();
            for (MatchToken t : tokens)
                and.addExpression(t);
            this.token = and;
        } else if (tokens.size() == 1) {
            this.token = tokens.get(0);
        }
    }
    
    /**
     * Same as AND, but with booleans inverted.
     */
    public boolean match(ItemStack item) {
        return !token.match(item);
    }
    
    public boolean isComplex() {
        return true;
    }
    
    public String toString(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.indent("NOT:\n", i));
        sb.append(StringUtils.indent("{\n", i));
        sb.append(token.toString(i + 1));
        sb.append("\n");
        sb.append(StringUtils.indent("}", i));
        return sb.toString();
    }
    
}
