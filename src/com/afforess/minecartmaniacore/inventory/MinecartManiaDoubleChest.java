package com.afforess.minecartmaniacore.inventory;

import net.minecraft.server.InventoryLargeChest;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.utils.ItemMatcher;

/**
 * A temporary object that allows for easy use of accessing and altering the contents of a double chest. References are not safe, do not store them for later retrieval. Create them only as needed.
 * 
 * @author Afforess
 */
public class MinecartManiaDoubleChest implements MinecartManiaInventory {
    
    private final MinecartManiaChest chest1;
    private final MinecartManiaChest chest2;
    
    public MinecartManiaDoubleChest(final MinecartManiaChest left, final MinecartManiaChest right) {
        chest1 = left;
        chest2 = right;
    }
    
    public Inventory getBukkitInventory() {
        final InventoryLargeChest large = new InventoryLargeChest("Large Chest", ((CraftInventory) chest1.getBukkitInventory()).getInventory(), ((CraftInventory) chest2.getBukkitInventory()).getInventory());
        return new CraftInventory(large);
    }
    
    public boolean equals(final Location loc) {
        if (loc.equals(chest1.getLocation()))
            return true;
        if (loc.equals(chest2.getLocation()))
            return true;
        return false;
    }
    
    public boolean canAddItem(final ItemStack item, final Player player) {
        return chest1.canAddItem(item, player) || chest2.canAddItem(item, player);
    }
    
    public boolean canAddItem(final ItemStack item) {
        return canAddItem(item, null);
    }
    
    /**
     * Attempts to add an itemstack to this storage minecart. It adds items in a 'smart' manner, merging with existing itemstacks, until they reach the maximum size (64). If it fails, it will not alter the storage minecart's previous contents.
     * 
     * @param item to add
     * @player who is adding the item
     * @return true if the item was successfully added
     */
    public boolean addItem(final ItemStack item, final Player player) {
        return chest1.addItem(item, player);
    }
    
    /**
     * Attempts to add an itemstack to this storage minecart. It adds items in a 'smart' manner, merging with existing itemstacks, until they reach the maximum size (64). If it fails, it will not alter the storage minecart's previous contents.
     * 
     * @param item to add
     * @return true if the item was successfully added
     */
    public boolean addItem(final ItemStack item) {
        return chest1.addItem(item);
    }
    
    /**
     ** Attempts to add a single item of the given type to this storage minecart. If it fails, it will not alter the storage minecart's previous contents
     ** 
     * @param type to add
     **/
    public boolean addItem(final int type) {
        return chest1.addItem(type);
    }
    
    /**
     ** Attempts to add a given amount of a given type to this storage minecart. If it fails, it will not alter the storage minecart's previous contents
     ** 
     * @param type to add
     ** @param amount to add
     **/
    public boolean addItem(final int type, final int amount) {
        return chest1.addItem(type, amount);
    }
    
    public boolean canRemoveItem(final int type, final int amount, final short durability, final Player player) {
        return chest1.canRemoveItem(type, amount, durability, player) || chest2.canRemoveItem(type, amount, durability, player);
    }
    
    public boolean canRemoveItem(final int type, final int amount, final short durability) {
        return canRemoveItem(type, amount, durability, null);
    }
    
    /**
     * Attempts to remove the specified amount of an item type from this storage minecart. If it fails, it will not alter the storage minecart's previous contents
     * 
     * @param type to remove
     * @param amount to remove
     * @param durability of the item to remove
     * @param player who is removing the item
     * @return true if the items were successfully removed
     */
    public boolean removeItem(final int type, final int amount, final short durability, final Player player) {
        return chest1.removeItem(type, amount, durability, player);
    }
    
    /**
     * Attempts to remove the specified amount of an item type from this storage minecart. If it fails, it will not alter the storage minecart's previous contents
     * 
     * @param type to remove
     * @param amount to remove
     * @param durability of the item to remove
     * @return true if the items were successfully removed
     */
    public boolean removeItem(final int type, final int amount, final short durability) {
        return chest1.removeItem(type, amount, durability);
    }
    
    /**
     * Attempts to remove the specified amount of an item type from this storage minecart. If it fails, it will not alter the storage minecart's previous contents
     * 
     * @param type to remove
     * @param amount to remove
     * @return true if the items were successfully removed
     */
    public boolean removeItem(final int type, final int amount) {
        return chest1.removeItem(type, amount);
    }
    
    /**
     * Attempts to remove a single item type from this storage minecart. If it fails, it will not alter the storage minecart previous contents
     * 
     * @param type to remove
     * @return true if the item was successfully removed
     */
    public boolean removeItem(final int type) {
        return chest1.removeItem(type);
    }
    
    /**
     * Gets the itemstack at the given slot, or null if empty
     * 
     * @param the slot to get
     * @return the itemstack at the given slot
     */
    public ItemStack getItem(final int slot) {
        if (slot < chest1.size())
            return chest1.getItem(slot);
        return chest2.getItem(slot - chest1.size());
    }
    
    /**
     * Sets the given slot to the given itemstack. If the itemstack is null, the slot's contents will be cleared.
     * 
     * @param slot to set.
     * @param item to set in the slot
     */
    public void setItem(final int slot, final ItemStack item) {
        if (slot < chest1.size()) {
            chest1.setItem(slot, item);
        } else {
            chest2.setItem(slot - chest1.size(), item);
        }
    }
    
    /**
     * Get's the first empty slot in this storage minecart
     * 
     * @return the first empty slot in this storage minecart
     */
    public int firstEmpty() {
        if (chest1.firstEmpty() != -1)
            return chest1.firstEmpty();
        if (chest2.firstEmpty() != -1)
            return chest2.firstEmpty() + chest1.size();
        return -1;
    }
    
    /**
     * Gets the size of the inventory of this storage minecart
     * 
     * @return the size of the inventory
     */
    public int size() {
        return chest1.size() + chest2.size();
    }
    
    /**
     * Gets an array of the Itemstack's inside this storage minecart. Empty slots are represented by air stacks
     * 
     * @return the contents of this inventory
     */
    public ItemStack[] getContents() {
        final ItemStack[] contents = new ItemStack[size()];
        for (int i = 0; i < chest1.size(); i++) {
            contents[i] = chest1.getItem(i);
        }
        for (int i = 0; i < chest2.size(); i++) {
            contents[i + chest1.size()] = chest2.getItem(i);
        }
        return contents;
    }
    
    /**
     * Set's the contents of this inventory with an array of items.
     * 
     * @param contents to set as the inventory
     */
    public void setContents(final ItemStack[] contents) {
        final ItemStack[] side1 = new ItemStack[chest1.size()];
        final ItemStack[] side2 = new ItemStack[chest2.size()];
        for (int i = 0; i < contents.length; i++) {
            if (i < side1.length) {
                side1[i] = contents[i];
            } else {
                side2[i - side1.length] = contents[i];
            }
        }
        chest1.setContents(side1);
        chest2.setContents(side2);
    }
    
    /**
     * Get's the first slot containing the given material, or -1 if none contain it
     * 
     * @param material to search for
     * @return the first slot with the given material
     */
    public int first(final Material material) {
        return first(material.getId(), (short) -1);
    }
    
    /**
     * Get's the first slot containing the given type id, or -1 if none contain it
     * 
     * @param type id to search for
     * @return the first slot with the given item
     */
    public int first(final int type) {
        return first(type, (short) -1);
    }
    
    /**
     * Get's the first slot containing the given type id and matching durability, or -1 if none contain it. If the durability is -1, it get's the first slot with the matching type id, and ignores durability
     * 
     * @param type id to search for
     * @param durability of the type id to search for
     * @return the first slot with the given type id and durability
     */
    public int first(final int type, final short durability) {
        if (chest1.first(type, durability) != -1)
            return chest1.first(type);
        if (chest2.first(type, durability) != -1)
            return chest2.first(type) + chest1.size();
        return -1;
    }
    
    /**
     * Searches the inventory for any items
     * 
     * @return true if the inventory contains no items
     */
    public boolean isEmpty() {
        for (final ItemStack i : getContents()) {
            //I hate you too, air.
            if ((i != null) && (i.getType() != Material.AIR))
                return false;
        }
        return true;
    }
    
    /**
     * Searches the inventory for any items that match the given Material
     * 
     * @param material to search for
     * @return true if the material is found
     */
    public boolean contains(final Material material) {
        return (chest1.contains(material) || chest2.contains(material));
    }
    
    /**
     * Searches the inventory for any items that match the given type id
     * 
     * @param type id to search for
     * @return true if an item matching the type id is found
     */
    public boolean contains(final int type) {
        return chest1.contains(type) || chest2.contains(type);
    }
    
    /**
     * Searches the inventory for any items that match the given type id and durability
     * 
     * @param type id to search for
     * @param durability to search for
     * @return true if an item matching the type id and durability is found
     */
    public boolean contains(final int type, final short durability) {
        return chest1.contains(type, durability) || chest2.contains(type, durability);
    }
    
    public int amount(final int type, final short durability) {
        return chest1.amount(type, durability) + chest2.amount(type, durability);
    }
    
    public int amount(final ItemMatcher matcher) {
        return chest1.amount(matcher) + chest2.amount(matcher);
    }
    
    public String getFailureReason() {
        return chest1.getFailureReason() + chest2.getFailureReason();
    }
    
    public boolean contains(final ItemMatcher matcher) {
        return first(matcher) != -1;
    }
    
    public int first(final ItemMatcher matcher) {
        int buf = chest1.first(matcher);
        if (buf != -1)
            return buf;
        buf = chest2.first(matcher);
        if (buf != -1)
            return buf + chest1.size();
        return -1;
    }
    
}
