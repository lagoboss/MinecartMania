package com.afforess.minecartmaniacore.inventory;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.yi.acru.bukkit.Lockette.Lockette;

import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.signs.ForceUnlockChestAction;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.utils.SignUtils;
import com.afforess.minecartmaniacore.world.Item;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;
import com.griefcraft.lwc.LWCPlugin;

public class MinecartManiaChest extends MinecartManiaSingleContainer implements MinecartManiaInventory {
    
    public static int SPAWN_DELAY = 1000;
    private long lastSpawnTime = -1;
    private final Location chest;
    private boolean redstonePower;
    private final ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<String, Object>();
    private String failureReason;
    
    public MinecartManiaChest(final Chest chest) {
        super(chest.getInventory());
        this.chest = chest.getBlock().getLocation().clone();
        setRedstonePower(MinecartManiaWorld.isBlockIndirectlyPowered(chest.getWorld(), getX(), getY(), getZ()));
    }
    
    public int getX() {
        return chest.getBlockX();
    }
    
    public int getY() {
        return chest.getBlockY();
    }
    
    public int getZ() {
        return chest.getBlockZ();
    }
    
    public World getWorld() {
        return chest.getWorld();
    }
    
    public Location getLocation() {
        return chest;
    }
    
    public Chest getChest() {
        return (Chest) chest.getBlock().getState();
    }
    
    /**
     * Returns the neighbor chest to this chest, or null if none exists
     * 
     * @return the neighbor chest
     */
    public MinecartManiaChest getNeighborChest() {
        return getNeighborChest(chest.getWorld(), getX(), getY(), getZ());
    }
    
    /**
     * Returns the double chest that this chest is a part of, or null if it is a single chest
     * 
     * @return the double chest
     */
    public MinecartManiaDoubleChest getLargeChest() {
        if (getNeighborChest() != null)
            return new MinecartManiaDoubleChest(this, getNeighborChest());
        return null;
    }
    
    /**
     * Returns the neighbor chest to this chest, or null if none exists
     * 
     * @param w the world to search in
     * @param x coordinate to search
     * @param y coordinate to search
     * @param z coordinate to search
     */
    public static MinecartManiaChest getNeighborChest(final World w, final int x, final int y, final int z) {
        if (MinecartManiaWorld.getBlockAt(w, x - 1, y, z).getTypeId() == Item.CHEST.getId())
            return MinecartManiaWorld.getMinecartManiaChest((Chest) MinecartManiaWorld.getBlockAt(w, x - 1, y, z).getState());
        if (MinecartManiaWorld.getBlockAt(w, x + 1, y, z).getTypeId() == Item.CHEST.getId())
            return MinecartManiaWorld.getMinecartManiaChest((Chest) MinecartManiaWorld.getBlockAt(w, x + 1, y, z).getState());
        if (MinecartManiaWorld.getBlockAt(w, x, y, z - 1).getTypeId() == Item.CHEST.getId())
            return MinecartManiaWorld.getMinecartManiaChest((Chest) MinecartManiaWorld.getBlockAt(w, x, y, z - 1).getState());
        if (MinecartManiaWorld.getBlockAt(w, x, y, z + 1).getTypeId() == Item.CHEST.getId())
            return MinecartManiaWorld.getMinecartManiaChest((Chest) MinecartManiaWorld.getBlockAt(w, x, y, z + 1).getState());
        
        return null;
    }
    
    /**
     * Returns the value from the loaded data
     * 
     * @param key the string key the data value is associated with
     * @return the object stored by the key
     */
    public Object getDataValue(final String key) {
        if (data.containsKey(key))
            return data.get(key);
        return null;
    }
    
    /**
     ** Creates a new data value if it does not already exists, or resets an existing value
     ** 
     * @param key the data value is associated with
     ** @param value to store
     **/
    public void setDataValue(final String key, final Object value) {
        if (value == null) {
            data.remove(key);
        } else {
            data.put(key, value);
        }
    }
    
    public String getOwner() {
        if (MinecartManiaCore.isLocketteEnabled()) {
            if (Lockette.isProtected(getLocation().getBlock()))
                return Lockette.getProtectedOwner(getLocation().getBlock());
        }
        if (MinecartManiaCore.isLWCEnabled()) {
            final LWCPlugin lock = (LWCPlugin) Bukkit.getServer().getPluginManager().getPlugin("LWC");
            if (lock.getLWC().findProtection(getLocation().getBlock()) != null)
                return lock.getLWC().findProtection(getLocation().getBlock()).getOwner();
        }
        return null;
    }
    
    private boolean isIgnoreProtection() {
        final ArrayList<Sign> signs = SignUtils.getAdjacentMinecartManiaSignList(getLocation(), 1);
        for (final Sign sign : signs) {
            if (sign.executeAction(null, ForceUnlockChestAction.class))
                return true;
        }
        return false;
    }
    
    public boolean canAccess(final String player) {
        if (isIgnoreProtection() || (player == null))
            return true;
        if (MinecartManiaCore.isLocketteEnabled()) {
            if (Lockette.isProtected(getLocation().getBlock())) {
                if (player != null)
                    return Lockette.getProtectedOwner(getLocation().getBlock()).equals(player);
                return false;
            }
        }
        if (MinecartManiaCore.isLWCEnabled()) {
            final LWCPlugin lock = (LWCPlugin) Bukkit.getServer().getPluginManager().getPlugin("LWC");
            if (lock.getLWC().findProtection(getLocation().getBlock()) != null) {
                if (player != null) {
                    final Player ply = Bukkit.getServer().getPlayer(player);
                    if (ply != null)
                        return lock.getLWC().canAccessProtection(ply, getLocation().getBlock());
                }
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean canAddItem(final ItemStack item, final Player player) {
        if (!canAccess(player != null ? player.getName() : null))
            return false;
        
        return super.canAddItem(item, player);
    }
    
    @Override
    public boolean canRemoveItem(final int type, final int amount, final short durability, final Player player) {
        if (player != null) {
            if (!canAccess(player.getName())) {
                failureReason=player.getName()+" cannot access this chest";
                return false;
            }
        }
        
        return super.canRemoveItem(type, amount, durability, player);
    }
    
    /**
     * Attempts to add an itemstack to this chest. It adds items in a 'smart' manner, merging with existing itemstacks, until they reach the maximum size (64). If it fails, it will not alter the chest's previous contents.
     * 
     * @param item to add
     */
    @Override
    public boolean addItem(ItemStack item, final Player player) {
        if (!canAddItem(item, player))
            return false;
        if (item == null)
            return true;
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
            update();
            return true;
        }
        
        //Try to merge the itemstack with the neighbor chest, if we have one
        final MinecartManiaChest neighbor = getNeighborChest();
        if (neighbor != null) {
            //flag to prevent infinite recursion
            if (getDataValue("neighbor") == null) {
                neighbor.setDataValue("neighbor", Boolean.TRUE);
                if (getNeighborChest().addItem(item)) {
                    update();
                    return true;
                }
            } else {
                //reset flag
                setDataValue("neighbor", null);
            }
        }
        
        //if we fail, reset the inventory and item back to previous values
        getChest().getInventory().setContents(backup);
        item = backupItem;
        return false;
    }
    
    /**
     * Attempts to remove the specified amount of an item type from this chest. If it fails, it will not alter the chests previous contents.
     * 
     * @param type to remove
     * @param amount to remove
     * @param durability of the item to remove
     */
    @Override
    public boolean removeItem(final int type, int amount, final short durability, final Player player) {
        if (!canRemoveItem(type, amount, durability, player))
            return false;
        //Backup contents
        final ItemStack[] backup = getContents().clone();
        
        for (int i = 0; i < size(); i++) {
            if (getItem(i) != null) {
                if ((getItem(i).getTypeId() == type) && ((durability == -1) || (getItem(i).getDurability() == durability))) {
                    if ((getItem(i).getAmount() - amount) > 0) {
                        setItem(i, new ItemStack(type, getItem(i).getAmount() - amount, durability));
                        update();
                        return true;
                    } else if ((getItem(i).getAmount() - amount) == 0) {
                        setItem(i, null);
                        update();
                        return true;
                    } else {
                        amount -= getItem(i).getAmount();
                        setItem(i, null);
                    }
                }
            }
        }
        
        final MinecartManiaChest neighbor = getNeighborChest();
        if (neighbor != null) {
            //flag to prevent infinite recursion
            if (getDataValue("neighbor") == null) {
                neighbor.setDataValue("neighbor", Boolean.TRUE);
                if (neighbor.removeItem(type, amount)) {
                    update();
                    return true;
                }
            } else {
                //reset flag
                setDataValue("neighbor", null);
            }
        }
        
        //if we fail, reset the inventory back to previous values
        getChest().getInventory().setContents(backup);
        return false;
    }
    
    public void setRedstonePower(final boolean redstonePower) {
        this.redstonePower = redstonePower;
    }
    
    public boolean isRedstonePower() {
        return redstonePower;
    }
    
    public void update() {
        getChest().update();
    }
    
    @Override
    public String toString() {
        return "[" + getX() + ":" + getY() + ":" + getZ() + "]";
    }
    
    @Override
    public Inventory getInventory() {
        return getChest().getInventory();
    }
    
    public boolean canSpawnMinecart() {
        if ((lastSpawnTime == -1) || (Math.abs(System.currentTimeMillis() - lastSpawnTime) > SPAWN_DELAY)) {
            lastSpawnTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}
