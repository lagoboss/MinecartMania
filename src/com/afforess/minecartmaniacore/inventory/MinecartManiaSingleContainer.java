package com.afforess.minecartmaniacore.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.utils.ItemMatcher;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;

/**
 * This class represents a single container (unlike double containers, like a double chest) and provides utility methods for dealing with items inside the container
 * 
 * @author Afforess
 */
public abstract class MinecartManiaSingleContainer implements MinecartManiaInventory {
    private Inventory inventory;
    private String failureReason = "";
    
    public MinecartManiaSingleContainer(final Inventory i) {
        inventory = i;
    }
    
    /**
     * The bukkit inventory that this container represents
     * 
     * @return the inventory
     */
    public Inventory getInventory() {
        return inventory;
    }
    
    /**
     * The bukkit inventory that this container represents
     * 
     * @return the inventory
     */
    public Inventory getBukkitInventory() {
        return getInventory();
    }
    
    /**
     * Forces the inventory to update to the new given inventory. Needed because Bukkit inventories become garbage when the chunks they are in are unloaded, or the player dies, etc...
     * 
     * @param inventory for this container to use
     * 
     */
    public void updateInventory(final Inventory inventory) {
        this.inventory = inventory;
    }
    
    public boolean canAddItem(final ItemStack item, final Player player) {
        if (item == null)
            return true;
        if (item.getTypeId() == Material.AIR.getId())
            return false;
        return true;
    }
    
    public boolean canAddItem(final ItemStack item) {
        return canAddItem(item, null);
    }
    
    /**
     * Attempts to add an itemstack. It adds items in a 'smart' manner, merging with existing itemstacks, until they reach the maximum size (64). If it fails, it will not alter the previous contents.
     * 
     * @param item to add
     * @param player who is adding the item
     * @return true if the item was successfully added
     */
    public boolean addItem(ItemStack item, final Player player) {
        if (!canAddItem(item, player))
            return false;
        
        //Backup contents
        final ItemStack[] backup = getContents().clone();
        final ItemStack backupItem = new ItemStack(item.getTypeId(), item.getAmount(), item.getDurability());
        
        final int max = MinecartManiaWorld.getMaxStackSize(item);
        
        //First attempt to merge the itemstack with existing item stacks that aren't full (< 64)
        for (int i = 0; i < size(); i++) {
            if (getItem(i) != null) {
                if ((getItem(i).getTypeId() == item.getTypeId()) && (getItem(i).getDurability() == item.getDurability())) {
                    if ((getItem(i).getAmount() + item.getAmount()) <= max) {
                        setItem(i, new ItemStack(item.getTypeId(), getItem(i).getAmount() + item.getAmount(), item.getDurability()));
                        return true;
                    } else {
                        final int diff = (getItem(i).getAmount() + item.getAmount()) - max;
                        setItem(i, new ItemStack(item.getTypeId(), max, item.getDurability()));
                        item = new ItemStack(item.getTypeId(), diff, item.getDurability());
                    }
                }
            }
        }
        
        //Attempt to add the item to an empty slot
        final int emptySlot = firstEmpty();
        if (emptySlot > -1) {
            setItem(emptySlot, item);
            return true;
        }
        
        //if we fail, reset the inventory and item back to previous values
        setContents(backup);
        item = backupItem;
        return false;
    }
    
    /**
     * Attempts to add an itemstack. It adds items in a 'smart' manner, merging with existing itemstacks, until they reach the maximum size (64). If it fails, it will not alter the previous contents.
     * 
     * @param item to add
     * @return true if the item was successfully added
     */
    public boolean addItem(final ItemStack item) {
        return addItem(item, null);
    }
    
    /**
     * Attempts to add an itemstack. If it fails, it will not alter the previous contents
     * 
     * @param type to add
     * @param amount to add
     * @return true if the item of the given amount was successfully added
     */
    public boolean addItem(final int type, final int amount) {
        return addItem(new ItemStack(type, amount));
    }
    
    /**
     * Attempts to add a single item. If it fails, it will not alter the previous contents
     * 
     * @param itemtype to add
     * @return true if the item was successfully added
     */
    public boolean addItem(final int type) {
        return addItem(new ItemStack(type, 1));
    }
    
    public boolean canRemoveItem(final int type, final int amount, final short durability, final Player player) {
        return true;
    }
    
    public boolean canRemoveItem(final int type, final int amount, final short durability) {
        return canRemoveItem(type, amount, durability, null);
    }
    
    /**
     * Attempts to remove the specified amount of an item type. If it fails, it will not alter the previous contents. If the durability is -1, it will only match item type id's and ignore durability
     * 
     * @param type to remove
     * @param amount to remove
     * @param durability of the item to remove
     * @param player who is removing the item
     * @return true if the items were successfully removed
     */
    public boolean removeItem(final int type, int amount, final short durability, final Player player) {
        if (!canRemoveItem(type, amount, durability))
            return false;
        //Backup contents
        final ItemStack[] backup = getContents().clone();
        
        for (int i = 0; i < size(); i++) {
            if (getItem(i) != null) {
                if ((getItem(i).getTypeId() == type) && ((durability == -1) || (getItem(i).getDurability() == durability))) {
                    if ((getItem(i).getAmount() - amount) > 0) {
                        setItem(i, new ItemStack(type, getItem(i).getAmount() - amount, durability));
                        return true;
                    } else if ((getItem(i).getAmount() - amount) == 0) {
                        setItem(i, null);
                        return true;
                    } else {
                        amount -= inventory.getItem(i).getAmount();
                        setItem(i, null);
                    }
                }
            }
        }
        
        //if we fail, reset the inventory back to previous values
        setContents(backup);
        setFailureReason("None left");
        return false;
    }
    
    /**
     * Attempts to remove the specified amount of an item type. If it fails, it will not alter the previous contents. If the durability is -1, it will only match item type id's and ignore durability
     * 
     * @param type to remove
     * @param amount to remove
     * @param durability of the item to remove
     * @return true if the items were successfully removed
     */
    public boolean removeItem(final int type, final int amount, final short durability) {
        return removeItem(type, amount, durability, null);
    }
    
    /**
     * Attempts to remove the specified amount of an item type. If it fails, it will not alter the previous contents.
     * 
     * @param type to remove
     * @param amount to remove
     * @return true if the items were successfully removed
     */
    public boolean removeItem(final int type, final int amount) {
        return removeItem(type, amount, (short) -1);
    }
    
    /**
     * attempts to remove a single item type. If it fails, it will not alter the previous contents.
     * 
     * @param type to remove
     * @return true if the item was successfully removed
     */
    public boolean removeItem(final int type) {
        return removeItem(type, 1);
    }
    
    /**
     * Gets the itemstack at the given slot, or null if empty
     * 
     * @param slot to get
     * @return the itemstack at the given slot
     */
    public ItemStack getItem(final int slot) {
        final ItemStack i = getInventory().getItem(slot);
        if (i.getAmount() < -1) {
            i.setAmount(64);
            getInventory().setItem(slot, i);
        }
        //WTF is it with bukkit and returning air instead of null?
        return i == null ? null : (i.getTypeId() == Material.AIR.getId() ? null : i);
    }
    
    /**
     * Sets the itemstack at the given slot. If the itemstack is null, it will clear the slot.
     * 
     * @param slot to set
     * @param item to set at given slot
     */
    public void setItem(final int slot, final ItemStack item) {
        if (item == null) {
            getInventory().clear(slot);
        } else {
            getInventory().setItem(slot, item);
        }
    }
    
    /**
     * Gets the first empty slot in this inventory, or -1 if it is full
     * 
     * @return the first empty slot
     */
    public int firstEmpty() {
        for (int i = 0; i < size(); i++) {
            if (getItem(i) == null)
                return i;
        }
        return -1;
    }
    
    /**
     * Gets the size of this inventory
     * 
     * @return the size of this inventory
     */
    public int size() {
        return getInventory().getSize();
    }
    
    /**
     * Get's an array containing all the contents of this inventory. Empty slots are represented by air.
     * 
     * @return An array containing the contents of this inventory
     */
    public ItemStack[] getContents() {
        return getInventory().getContents();
    }
    
    /**
     * Set's the contents of this inventory with an array of items.
     * 
     * @param contents to set as the inventory
     */
    public void setContents(final ItemStack[] contents) {
        getInventory().setContents(contents);
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
        for (int i = 0; i < size(); i++) {
            if (getItem(i) != null) {
                if ((getItem(i).getTypeId() == type) && (((durability == -1) || (getItem(i).getDurability() == -1)) || (getItem(i).getDurability() == durability)))
                    return i;
            }
        }
        return -1;
    }
    
    public int amount(final ItemMatcher matcher) {
        int count = 0;
        for (final ItemStack i : getContents()) {
            if (i != null) {
                if (matcher.match(i)) {
                    count += i.getAmount();
                }
            }
        }
        return count;
    }
    
    public int amount(final int type, final short durability) {
        int count = 0;
        for (int i = 0; i < size(); i++) {
            if (getItem(i) != null) {
                if ((getItem(i).getTypeId() == type) && ((durability == -1) || (getItem(i).getDurability() == durability))) {
                    count += getItem(i).getAmount();
                }
            }
        }
        return count;
    }
    
    /**
     * Searches the inventory for any items that match the given Material
     * 
     * @param material to search for
     * @return true if the material is found
     */
    public boolean contains(final Material material) {
        return first(material) != -1;
    }
    
    /**
     * Searches the inventory for any items that match the given type id
     * 
     * @param type id to search for
     * @return true if an item matching the type id is found
     */
    public boolean contains(final int type) {
        return first(type) != -1;
    }
    
    /**
     * Searches the inventory for any items that match the given type id and durability
     * 
     * @param type id to search for
     * @param durability to search for
     * @return true if an item matching the type id and durability is found
     */
    public boolean contains(final int type, final short durability) {
        return first(type, durability) != -1;
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
    
    public String getFailureReason() {
        return failureReason;
    }
    
    private void setFailureReason(final String failureReason) {
        this.failureReason = failureReason;
    }
    
    public boolean contains(final ItemMatcher matcher) {
        return first(matcher) != -1;
    }
    
    public int first(final ItemMatcher matcher) {
        for (int i = 0; i < size(); i++) {
            if (getItem(i) != null) {
                if (matcher.match(getItem(i)))
                    return i;
            }
        }
        return -1;
    }
}
