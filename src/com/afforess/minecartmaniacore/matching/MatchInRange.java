package com.afforess.minecartmaniacore.matching;

import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.utils.StringUtils;

public class MatchInRange implements MatchToken {
    private final MatchField field;
    private final int start;
    private final int end;
    
    public MatchInRange(final MatchField field, final int start, final int end) {
        this.field = field;
        this.start = start;
        this.end = end;
    }
    
    public boolean match(final ItemStack item) {
        final int value = field.getFieldValue(item);
        return ((value >= start) && (value <= end));
    }
    
    public boolean isComplex() {
        return true;
    }
    
    public String toString(final int i) {
        return StringUtils.indent(String.format("%s IN RANGE %d - %d", field.name(), start, end), i);
    }
    
}
