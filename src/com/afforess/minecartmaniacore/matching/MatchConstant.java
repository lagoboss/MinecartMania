package com.afforess.minecartmaniacore.matching;

import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.utils.ItemUtils.TYPE;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class MatchConstant implements MatchToken {
    private final int value;
    private final MatchField field;
    private int amount;
    
    public MatchConstant(final MatchField field, final int constant) {
        this.field = field;
        value = constant;
    }
    
    public boolean match(final ItemStack item) {
        final int value = field.getFieldValue(item);
        return (this.value == value);
    }
    
    public MatchField getField() {
        return field;
    }
    
    public int getValue() {
        return value;
    }
    
    public boolean isComplex() {
        return false;
    }
    
    public String toString(final int i) {
        return StringUtils.indent(String.format("%s == %d", field.name(), value), i);
    }
    
    public static MatchToken parseAll(final String part) {
        MatchToken expr = null;
        String[] chunks = null;
        switch (TYPE.getType(part)) {
            case DATA:
                chunks = part.split(TYPE.DATA.getTag());
                expr = new MatchAND();
                ((MatchAND) expr).addExpression(parseAll(chunks[0]));
                ((MatchAND) expr).addExpression(new MatchConstant(MatchField.DURABILITY, Integer.parseInt(chunks[1])));
                return expr;
            case BIT:
                chunks = part.split(TYPE.BIT.getTag());
                expr = new MatchAND();
                boolean state = true;
                if (chunks[1].startsWith("~")) {
                    chunks[1] = chunks[1].substring(1);
                    state = false;
                }
                final int bit = Integer.parseInt(chunks[1]);
                ((MatchAND) expr).addExpression(parseAll(chunks[0]));
                ((MatchAND) expr).addExpression(new MatchBit(MatchField.DURABILITY, bit, state));
                return expr;
            case NONE:
                if (part.equalsIgnoreCase("all items")) {
                    return new MatchAll();
                } else {
                    return new MatchConstant(MatchField.TYPE_ID, Integer.parseInt(part));
                }
        }
        return null;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public void setAmount(final int amt) {
        amount = amt;
    }
    
    public boolean parse(final String part) {
        return false;
    }
    
}
