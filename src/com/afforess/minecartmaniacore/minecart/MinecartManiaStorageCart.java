package com.afforess.minecartmaniacore.minecart;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;
import com.afforess.minecartmaniacore.inventory.MinecartManiaInventory;
import com.afforess.minecartmaniacore.matching.MatchField;
import com.afforess.minecartmaniacore.utils.ItemMatcher;
import com.afforess.minecartmaniacore.world.Item;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;

/**
 * This class represents a Minecart Mania Storage Minecart, that wraps a bukkit minecart (which in turn, wraps a Minecraft EntityMinecart)
 * 
 * @author Afforess
 */
public class MinecartManiaStorageCart extends MinecartManiaMinecart implements MinecartManiaInventory {
    private final ConcurrentHashMap<String, ItemMatcher> maximumContents = new ConcurrentHashMap<String, ItemMatcher>();
    private final ConcurrentHashMap<String, ItemMatcher> minimumContents = new ConcurrentHashMap<String, ItemMatcher>();
    
    /**
     * Creates a storage minecart from the given bukkit minecart
     * 
     * @param cart to create from
     */
    public MinecartManiaStorageCart(final Minecart cart) {
        super(cart);
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
     * Creates a storage minecart from the given bukkit minecart, with the given owner
     * 
     * @param cart to create from
     * @param owner that created the minecart
     */
    public MinecartManiaStorageCart(final Minecart cart, final String owner) {
        super(cart, owner);
    }
    
    public int getItemRange() {
        if (getDataValue("ItemCollectionRange") != null)
            return (Integer) getDataValue("ItemCollectionRange");
        return (Integer) MinecartManiaWorld.getConfigurationValue("ItemCollectionRange");
    }
    
    public void setItemRange(final int range) {
        setDataValue("ItemCollectionRange", range);
    }
    
    /**
     * Gets the bukkit inventory for this storage minecart
     * 
     * @return bukkit inventory
     */
    public Inventory getInventory() {
        return ((StorageMinecart) minecart).getInventory();
    }
    
    /*
     * public int getMaximumItem(Item item) { return getMaximumItem(item.getId(),(short)item.getData()); }
     */
    
    public int getMaximumItem(final Material item, final short data) {
        return getMaximumItem(item.getId(), data);
    }
    
    public int getMaximumItem(final int id, final short data) {
        final ItemStack item = new ItemStack(id, 1, data);
        if (maximumContents != null) {
            for (final ItemMatcher match : maximumContents.values()) {
                if (match.match(item))
                    return match.getAmount(-1);
            }
        }
        return -1;
    }
    
    private ItemMatcher getMatchingMaxRule(final int id, final short data) {
        final ItemStack item = new ItemStack(id, 1, data);
        if (maximumContents != null) {
            for (final ItemMatcher match : maximumContents.values()) {
                if (match.match(item))
                    return match;
            }
        }
        return null;
    }
    
    private ItemMatcher getMatchingMinRule(final int id, final short data) {
        final ItemStack item = new ItemStack(id, 1, data);
        if (minimumContents != null) {
            for (final ItemMatcher match : minimumContents.values()) {
                if (match.match(item))
                    return match;
            }
        }
        return null;
    }
    
    /*
     * public void setMaximumItem(Item item, int amount) { setMaximumItem(item.getId(),(short) item.getData(),amount); }
     */
    
    public void setMaximumItem(final Material item, final int amount) {
        setMaximumItem(item.getId(), -1, amount);
    }
    
    public void setMaximumItem(final int item, final int durability, final int amount) {
        if (maximumContents != null) {
            final ItemMatcher matcher = new ItemMatcher();
            matcher.addConstant(MatchField.TYPE_ID, item);
            if (durability != -1) {
                matcher.addConstant(MatchField.DURABILITY, durability);
            }
            matcher.setAmount(amount);
            maximumContents.put(matcher.toString(), matcher);
        }
    }
    
    public int getMinimumItem(final Material item) {
        return getMinimumItem(item.getId(), (short) -1);
    }
    
    public int getMinimumItem(final int id, final short data) {
        final ItemStack item = new ItemStack(id, 1, data);
        if (minimumContents != null) {
            for (final ItemMatcher match : minimumContents.values()) {
                if (match.match(item))
                    return match.getAmount(-1);
            }
        }
        return -1;
    }
    
    public void setMinimumItem(final Material item, final int amount) {
        setMinimumItem(item.getId(), (short) -1, amount);
    }
    
    public void setMinimumItem(final int id, final short data, final int amount) {
        if (minimumContents != null) {
            final ItemMatcher matcher = new ItemMatcher();
            matcher.addConstant(MatchField.TYPE_ID, id);
            if (data != -1) {
                matcher.addConstant(MatchField.DURABILITY, data);
            }
            matcher.setAmount(amount);
            minimumContents.put(matcher.toString(), matcher);
        }
    }
    
    public boolean canAddItem(final ItemStack item, final Player player) {
        if (item.getTypeId() == Material.AIR.getId())
            return false;
        
        final ItemMatcher matcher = getMatchingMaxRule(item.getTypeId(), item.getDurability());
        if (matcher != null) {
            final int found = amount(matcher);
            MinecartManiaLogger.getInstance().info(String.format("canAddItem: max %s = %d, %d found", item.toString(), matcher.getAmount(-1), found));
            if ((found + item.getAmount()) > matcher.getAmount(-1))
                return false;
        }
        return true;
    }
    
    private int amount(final ItemMatcher matcher) {
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
    
    public boolean canAddItem(final ItemStack item) {
        return canAddItem(item, null);
    }
    
    /**
     * attempts to add an itemstack to this storage minecart. It adds items in a 'smart' manner, merging with existing itemstacks, until they reach the maximum size (64). If it fails, it will not alter the storage minecart's previous contents.
     * 
     * @param item to add
     * @param player who is adding the item
     * @return true if the item was successfully added
     * @throws Exception
     */
    public boolean addItem(ItemStack item, final Player player) throws Exception {
        if (item == null)
            return true;
        if (!canAddItem(item))
            return false;
        if (item.getAmount() == -1)
            throw new Exception("Cannot set a minecart slot to -1!");
        
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
     * attempts to add an itemstack to this storage minecart. It adds items in a 'smart' manner, merging with existing itemstacks, until they reach the maximum size (64). If it fails, it will not alter the storage minecart's previous contents.
     * 
     * @param item to add
     * @param player who is adding the item
     * @return true if the item was successfully added
     */
    public boolean addItem(final ItemStack item) {
        try {
            return addItem(item, null);
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     ** attempts to add a single item of the given type to this storage minecart. If it fails, it will not alter the storage minecart's previous contents
     ** 
     * @param type to add
     **/
    public boolean addItem(final int type) {
        return addItem(new ItemStack(type, 1));
    }
    
    /**
     ** attempts to add a given amount of a given type to this storage minecart. If it fails, it will not alter the storage minecart's previous contents
     ** 
     * @param type to add
     ** @param amount to add
     **/
    public boolean addItem(final int type, final int amount) {
        return addItem(new ItemStack(type, amount));
    }
    
    public boolean canRemoveItem(final int type, final int amount, final short durability, final Player player) {
        
        final ItemStack item = new ItemStack(type, amount, durability);
        final ItemMatcher matcher = getMatchingMinRule(item.getTypeId(), item.getDurability());
        if (matcher != null) {
            final int found = amount(matcher);
            MinecartManiaLogger.getInstance().info(String.format("canRemoveItem: min %s = %d, %d found", item.toString(), matcher.getAmount(-1), found));
            if ((found - item.getAmount()) < matcher.getAmount(-1))
                return false;
        }
        return true;
    }
    
    public boolean canRemoveItem(final int type, final int amount, final short durability) {
        return canRemoveItem(type, amount, durability, null);
    }
    
    /**
     * attempts to remove the specified amount of an item type from this storage minecart. If it fails, it will not alter the storage minecart's previous contents.
     * 
     * @param type to remove
     * @param amount to remove
     * @param durability of the item to remove (-1 for generic durability)
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
                        //System.out.println(getItem(i).getAmount()+" - "+amount+" == 0");
                        amount -= getItem(i).getAmount();
                        setItem(i, null);
                    }
                }
            } else {
                //System.out.println("Can't find item "+type+";"+durability+"...");
            }
        }
        
        //if we fail, reset the inventory back to previous values
        setContents(backup);
        return false;
    }
    
    /**
     * attempts to remove the specified amount of an item type from this storage minecart. If it fails, it will not alter the storage minecart's previous contents.
     * 
     * @param type to remove
     * @param amount to remove
     * @param durability of the item to remove (-1 for generic durability)
     * @return true if the items were successfully removed
     */
    public boolean removeItem(final int type, final int amount, final short durability) {
        return removeItem(type, amount, durability, null);
    }
    
    /**
     * attempts to remove the specified amount of an item type from this storage minecart. If it fails, it will not alter the storage minecart's previous contents.
     * 
     * @param type to remove
     * @param amount to remove
     * @return true if the items were successfully removed
     */
    public boolean removeItem(final int type, final int amount) {
        return removeItem(type, amount, (short) -1);
    }
    
    /**
     * attempts to remove a single item type from this storage minecart. If it fails, it will not alter the storage minecart previous contents.
     * 
     * @param type to remove
     * @return true if the item was successfully removed
     */
    public boolean removeItem(final int type) {
        return removeItem(type, 1);
    }
    
    /**
     * Gets the size of the inventory of this storage minecart
     * 
     * @return the size of the inventory
     */
    public int size() {
        return getInventory().getSize();
    }
    
    /**
     * Gets an array of the Itemstack's inside this storage minecart. Empty slots are represented by air stacks
     * 
     * @return the contents of this inventory
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
     * Gets the itemstack at the given slot, or null if empty
     * 
     * @param slot to get
     * @return the itemstack at the given slot
     */
    public ItemStack getItem(final int slot) {
        final ItemStack i = getInventory().getItem(slot);
        //WTF is it with bukkit and returning air instead of null?
        return i == null ? null : (i.getTypeId() == Material.AIR.getId() ? null : i);
    }
    
    /**
     * Sets the given slot to the given itemstack. If the itemstack is null, the slot's contents will be cleared.
     * 
     * @param slot to set.
     * @param item to set in the slot
     */
    public void setItem(final int slot, final ItemStack item) {
        if (item == null) {
            getInventory().clear(slot);
        } else {
            getInventory().setItem(slot, item);
        }
    }
    
    /**
     * Get's the first empty slot in this storage minecart
     * 
     * @return the first empty slot in this storage minecart
     */
    public int firstEmpty() {
        for (int i = 0; i < size(); i++) {
            if (getItem(i) == null)
                return i;
        }
        return -1;
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
     * Get's the first slot containing the given item, or -1 if none contain it
     * 
     * @param item to search for
     * @return the first slot with the given item
     */
    public int first(final Item item) {
        return first(item.getId(), (short) (item.hasData() ? item.getData() : -1));
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
     * Searches the inventory for any items that match the given Item
     * 
     * @param item to search for
     * @return true if the Item is found
     */
    public boolean contains(final Item item) {
        return first(item) != -1;
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
    
    public int amount(final int item, final short durability) {
        int count = 0;
        for (final ItemStack i : getContents()) {
            if ((i != null) && (i.getTypeId() == item)) {
                if ((durability == -1) || (durability == i.getDurability())) {
                    count += i.getAmount();
                }
            }
        }
        return count;
    }
    
    public void setMaximumItem(final ItemMatcher matcher) {
        maximumContents.put(matcher.toString(), matcher);
    }
    
    public void setMinimumItem(final ItemMatcher matcher) {
        minimumContents.put(matcher.toString(), matcher);
    }
}
