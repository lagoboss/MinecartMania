package com.afforess.minecartmaniacore.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.matching.MatchConstant;
import com.afforess.minecartmaniacore.matching.MatchField;
import com.afforess.minecartmaniacore.matching.MatchInRange;
import com.afforess.minecartmaniacore.matching.MatchToken;

/**
 * An efficient system for matching items based on criteria provided by signs.
 * 
 * Each ItemMatcher is used to match against a single "line" on a sign.  If the ItemMatcher 
 * cannot see a match against the current item, the item is passed to the next ItemMatcher.
 * 
 * A line that reads: 373;8192-8197@3
 * Would turn into the following:
 *  * ITEM ID IS EQUAL TO 373
 *  * DATA IS IN RANGE 8192 TO 8197
 * (amount is not compared, only returned via getAmount() to systems that need it) 
 * @author Rob
 *
 */
public class ItemMatcher {
    // List of matching tokens
    protected List<MatchToken> matchTokens = new ArrayList<MatchToken>();
    
    // Number of things requested (-1 = N/A)
    protected int amount=-1;
    
    public ItemMatcher() {}

    public ItemMatcher(int typeId, short durability) {
        addConstant(MatchField.TYPE_ID,typeId);
        if(durability!=-1) {
            addConstant(MatchField.DURABILITY,durability);
        }
    }
    public void addRange(MatchField field,int start, int end) {
        matchTokens.add(new MatchInRange(field,start,end));
    }
    public void addConstant(MatchField field,int value) {
        matchTokens.add(new MatchConstant(field,value));
    }

    public void addExpression(MatchToken token) {
        matchTokens.add(token);
    }
    
    /**
     * See if them item fits ALL of the criteria listed in matchTokens.
     * @param item
     * @return
     */
    public boolean match(ItemStack item) {
        for(MatchToken matcher : matchTokens) {
            if(!matcher.match(item)) return false;
        }
        return true;
    }

    /**
     * Used in the sign parser to get an itemstack from the (simple) criteria.
     * @return
     */
    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(0,1,(short)-1);
        boolean foundID=false;
        for(MatchToken matcher : matchTokens) {
            if(matcher.isComplex()) return null;
            if(matcher instanceof MatchConstant) {
                MatchConstant constant = (MatchConstant)matcher;
                int v = constant.getValue();
                switch(constant.getField()) {
                    case TYPE_ID:
                        item.setTypeId(v);
                        break;
                    case DURABILITY:
                        item.setDurability((short) v);
                        break;
                }
            }
        }
        
        if(!foundID) return null;
        
        item.setAmount(amount);
        
        return item;
    }

    public void setAmount(int amount) {
        this.amount=amount;
    }
}
