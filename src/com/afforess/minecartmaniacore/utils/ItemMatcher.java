package com.afforess.minecartmaniacore.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.matching.MatchBit;
import com.afforess.minecartmaniacore.matching.MatchConstant;
import com.afforess.minecartmaniacore.matching.MatchField;
import com.afforess.minecartmaniacore.matching.MatchInRange;
import com.afforess.minecartmaniacore.matching.MatchNOT;
import com.afforess.minecartmaniacore.matching.MatchOR;
import com.afforess.minecartmaniacore.matching.MatchToken;
import com.afforess.minecartmaniacore.utils.ItemUtils.TYPE;
import com.afforess.minecartmaniacore.world.SpecificMaterial;

/**
 * An efficient system for matching items based on criteria provided by signs.
 * 
 * Each ItemMatcher is used to match against a single "line" on a sign. If the ItemMatcher cannot see a match against the current item, the item is passed to the next ItemMatcher.
 * 
 * A line that reads: 373;8192-8197@3 Would turn into the following: * ITEM ID IS EQUAL TO 373 * DATA IS IN RANGE 8192 TO 8197 (amount is not compared, only returned via getAmount() to systems that need it)
 * 
 * @author Rob
 * 
 */
public class ItemMatcher {
    // List of matching tokens
    protected List<MatchToken> matchTokens = new ArrayList<MatchToken>();
    
    // Number of things requested (-1 = N/A)
    protected int amount = -1;
    
    public ItemMatcher() {
    }
    
    public ItemMatcher(final int typeId, final short durability) {
        addConstant(MatchField.TYPE_ID, typeId);
        if (durability != -1) {
            addConstant(MatchField.DURABILITY, durability);
        }
    }
    
    public void addRange(final MatchField field, final int start, final int end) {
        matchTokens.add(new MatchInRange(field, start, end));
    }
    
    public void addConstant(final MatchField field, final int value) {
        matchTokens.add(new MatchConstant(field, value));
    }
    
    public void addExpression(final MatchToken token) {
        matchTokens.add(token);
    }
    
    public boolean parse(final String expression) {
        final MatchOR or = new MatchOR();
        for (String part : expression.split(":")) {
            MatchToken expr = null;
            int amt = -1;
            if (part.contains(TYPE.AMOUNT.getTag())) {
                final String[] parts = part.split(TYPE.AMOUNT.getTag());
                part = parts[0];
                if (parts.length > 1) amt = Integer.parseInt(parts[1]);
            }
            boolean NOT = false;
            if (part.startsWith(TYPE.REMOVE.getTag())) {
                part = part.substring(1);
                NOT = true;
            }
            switch (TYPE.getType(part)) {
                case RANGE:
                    try {
                        expr = new MatchInRange(part);
                    } catch (final NumberFormatException e) {
                        // It doesn't like negative numbers.
                        continue;
                    }
                    break;
                case BIT:
                    expr = MatchBit.parseAll(part);
                    break;
                default:
                    expr = MatchConstant.parseAll(part);
                    break;
            }
            if (expr != null) {
                expr.setAmount(amt);
                if (NOT) {
                    addExpression(new MatchNOT(expr));
                } else {
                    or.addExpression(expr);
                }
            }
        }
        addExpression(or);
        return true;
    }
    
    /**
     * See if them item fits ALL of the criteria listed in matchTokens.
     * 
     * @param item
     * @return
     */
    public boolean match(final ItemStack item) {
        if (item == null)
            return false;
        for (final MatchToken matcher : matchTokens) {
            if (!matcher.match(item))
                return false;
            else {
                internalSetAmount(matcher, matcher.getAmount());
            }
        }
        return true;
    }
    
    // Performs some sanity checks
    private void internalSetAmount(final MatchToken responsibleParty, final int amount2) {
        if (amount2 == 0) {
            final Exception e = new Exception("amount == 0 FROM " + responsibleParty.toString(0));
            e.printStackTrace();
        }
        amount = amount2;
    }
    
    /**
     * Used in the sign parser to get an itemstack from the (simple) criteria.
     * 
     * @return
     */
    public ItemStack toItemStack() {
        final ItemStack item = new ItemStack(0, 1, (short) -1);
        boolean foundID = false;
        for (final MatchToken matcher : matchTokens) {
            if (matcher.isComplex())
                return null;
            if (matcher instanceof MatchConstant) {
                final MatchConstant constant = (MatchConstant) matcher;
                final int v = constant.getValue();
                switch (constant.getField()) {
                    case TYPE_ID:
                        item.setTypeId(v);
                        foundID = true;
                        break;
                    case DURABILITY:
                        item.setDurability((short) v);
                        break;
                }
            }
        }
        
        if (!foundID)
            return null;
        
        item.setAmount(amount);
        
        return item;
    }
    
    public void setAmount(final int amount) {
        this.amount = amount;
    }
    
    public int getAmount(final int ifdefault) {
        return amountIsSet() ? amount : ifdefault;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("MATCH (AND):\n{\n");
        for (final MatchToken mt : matchTokens) {
            sb.append(mt.toString(1));
            sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
    
    public boolean amountIsSet() {
        return amount > -1;
    }
    
    public List<MatchToken> getTokens() {
        return matchTokens;
    }
    
    public SpecificMaterial toSpecificMaterial() {
        final ItemStack is = toItemStack();
        return new SpecificMaterial(is.getTypeId(), is.getDurability());
    }
}