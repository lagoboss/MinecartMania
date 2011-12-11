package com.afforess.minecartmaniacore.matching;

import org.bukkit.inventory.ItemStack;

public enum MatchField {
    TYPE_ID,
    DURABILITY;

    public int getFieldValue(ItemStack item) {
        if(this==TYPE_ID)
            return item.getTypeId();
        else
            return item.getDurability();
    }
}
