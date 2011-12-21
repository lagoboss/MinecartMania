package com.afforess.minecartmaniacore.matching;

import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.utils.StringUtils;

public class MatchBit implements MatchToken {
    
    private final boolean state;
    private final int bit;
    private final MatchField field;
    
    public MatchBit(final MatchField field, final int bit, final boolean state) {
        this.field = field;
        this.bit = bit;
        this.state = state;
    }
    
    public boolean match(final ItemStack item) {
        final int value = field.getFieldValue(item);
        final int flag = 1 >> bit;
        return (value & flag) == ((state) ? flag : 0);
    }
    
    public boolean isComplex() {
        return true;
    }
    
    public String toString(final int i) {
        return StringUtils.indent(String.format("BIT %s IS %s", bit, (state) ? "on" : "off"), i);
    }
    
}
