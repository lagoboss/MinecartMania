package com.afforess.minecartmaniacore.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * A "struct" for storing Type and Data together, more or less.
 * 
 * @author Rob
 * 
 */
public class SpecificMaterial {
    public SpecificMaterial(int id, int data) {
        this.id = id;
        this.durability = data;
    }
    
    public int id = 0;
    public int durability = 0;
    
    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + id;
        hash = hash * 31 + durability;
        return hash;
    }
    
    public static Collection<? extends SpecificMaterial> convertToSpecific(
            List<Material> asList) {
        ArrayList<SpecificMaterial> mats = new ArrayList<SpecificMaterial>();
        for (Material mat : asList) {
            mats.add(new SpecificMaterial(mat.getId(), (short) 0));
        }
        return mats;
    }
    
    public static SpecificMaterial convertBlock(Block b) {
        return new SpecificMaterial(b.getTypeId(), b.getData());
    }
    
    public int getId() {
        return id;
    }
    
    public int getData() {
        return durability;
    }
    
    public String toString() {
        return String.format("%d:%d", id, durability);
    }
}
