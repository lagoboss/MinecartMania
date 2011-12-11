package com.afforess.minecartmaniacore.matching;

import org.bukkit.inventory.ItemStack;


public class MatchConstant implements MatchToken {
    private int value;
    private MatchField field;
    public MatchConstant(MatchField field,int constant) {
        this.field=field;
        this.value=constant;
    }
    public boolean match(ItemStack item) {
        int value = field.getFieldValue(item);
        return (this.value==value);
    }
    public MatchField getField() { return field; }
    public int getValue() { return value; }
    public boolean isComplex() {
        // TODO Auto-generated method stub
        return false;
    }
    
}
