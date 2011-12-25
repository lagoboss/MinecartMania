package com.afforess.minecartmaniacore.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * A "struct" for storing Type and Data together, more or less.
 * 
 * @author Rob
 * 
 */
public class SpecificMaterial {
    public SpecificMaterial(final int id, final int data) {
        this.id = id;
        durability = data;
    }
    
    public int id = 0;
    public int durability = 0;
    
    @Override
    public int hashCode() {
        int hash = 1;
        hash = (hash * 31) + id;
        hash = (hash * 31) + durability;
        return hash;
    }
    
    public static Collection<? extends SpecificMaterial> convertToSpecific(final List<Material> asList) {
        final ArrayList<SpecificMaterial> mats = new ArrayList<SpecificMaterial>();
        for (final Material mat : asList) {
            mats.add(new SpecificMaterial(mat.getId(), (short) 0));
        }
        return mats;
    }
    
    public static SpecificMaterial convertBlock(final Block b) {
        return new SpecificMaterial(b.getTypeId(), b.getData());
    }
    
    public int getId() {
        return id;
    }
    
    public int getData() {
        return durability;
    }
    
    @Override
    public String toString() {
        return String.format("%d:%d", id, durability);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof SpecificMaterial)
            // Just compare HashCodes.
            return hashCode() == o.hashCode();
        else if (o instanceof ItemStack) {
            final ItemStack item = (ItemStack) o;
            return ((item.getTypeId() == id) && ((durability == -1) || (item.getDurability() == durability)));
        }
        return false;
    }
}
