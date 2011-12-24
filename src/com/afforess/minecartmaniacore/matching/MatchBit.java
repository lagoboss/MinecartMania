package com.afforess.minecartmaniacore.matching;

import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.utils.StringUtils;

public class MatchBit implements MatchToken {
    
    private final boolean state;
    private final int bit;
    private final MatchField field;
    private int amount;
    
    public MatchBit(final MatchField field, final int bit, final boolean state) {
        this.field = field;
        this.bit = bit;
        this.state = state;
    }
    
    public MatchBit(final String part) {
        field = MatchField.DURABILITY;
        state = !part.startsWith("~");
        bit = Integer.parseInt(part.substring(state ? 0 : 1));
    }
    
    public boolean match(final ItemStack item) {
        final int value = field.getFieldValue(item);
        final int flag = 1 << bit;
        return (value & flag) == ((state) ? flag : 0);
    }
    
    public boolean isComplex() {
        return true;
    }
    
    public String toString(final int i) {
        return StringUtils.indent(String.format("BIT %d (%d) IS %s", bit, 1 << bit, (state) ? "ON" : "OFF"), i);
    }
    
    public static MatchToken parseAll(final String expression) {
        final MatchAND and = new MatchAND();
        String[] split = expression.split("&");
        for (final String part : split[1].split(",")) {
            and.addExpression(new MatchBit(part));
        }
        if (and.tokens.size() == 1)
            return and.tokens.get(0);
        return and;
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
