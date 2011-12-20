package com.afforess.minecartmaniacore.matching;

import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.utils.StringUtils;

public class MatchConstant implements MatchToken {
    private final int value;
    private final MatchField field;
    
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
    
}
