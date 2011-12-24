package com.afforess.minecartmaniacore.matching;

import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.utils.ItemUtils.TYPE;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class MatchInRange implements MatchToken {
    private final MatchField field;
    private final int start;
    private final int end;
    private int amount = -1;
    
    public MatchInRange(final MatchField field, final int start, final int end) {
        this.field = field;
        this.start = start;
        this.end = end;
    }
    
    public MatchInRange(final String part) {
        // Assume there's no symbols, just a dash and two numbers.  If anything's out of line, it'll throw an error.
        final String[] parts = part.split(TYPE.RANGE.getTag());
        
        //TODO:No way to tell if it's a data value or a type, assume type for now.
        field = MatchField.TYPE_ID;
        start = Integer.parseInt(parts[0]);
        end = Integer.parseInt(parts[1]);
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
    
    public int getAmount() {
        return amount;
    }
    
    public void setAmount(final int amt) {
        amount = amt;
    }
    
}
