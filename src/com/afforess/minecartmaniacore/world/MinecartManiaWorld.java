package com.afforess.minecartmaniacore.world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.minecraft.server.EntityMinecart;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftMinecart;
import org.bukkit.craftbukkit.entity.CraftPoweredMinecart;
import org.bukkit.craftbukkit.entity.CraftStorageMinecart;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.PoweredMinecart;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Button;
import org.bukkit.material.Lever;
import org.bukkit.material.MaterialData;

import com.afforess.minecartmaniacore.debug.DebugTimer;
import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;
import com.afforess.minecartmaniacore.entity.MinecartManiaPlayer;
import com.afforess.minecartmaniacore.inventory.MinecartManiaBrewingStand;
import com.afforess.minecartmaniacore.inventory.MinecartManiaChest;
import com.afforess.minecartmaniacore.inventory.MinecartManiaDispenser;
import com.afforess.minecartmaniacore.inventory.MinecartManiaFurnace;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.minecart.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.utils.ThreadSafe;

public class MinecartManiaWorld {
    private static ConcurrentHashMap<Integer, MinecartManiaMinecart> minecarts = new ConcurrentHashMap<Integer, MinecartManiaMinecart>();
    private static ConcurrentHashMap<UUID, Integer> minecartUUID2EID = new ConcurrentHashMap<UUID, Integer>();
    private static ConcurrentHashMap<Location, MinecartManiaChest> chests = new ConcurrentHashMap<Location, MinecartManiaChest>();
    private static ConcurrentHashMap<Location, MinecartManiaDispenser> dispensers = new ConcurrentHashMap<Location, MinecartManiaDispenser>();
    private static ConcurrentHashMap<Location, MinecartManiaFurnace> furnaces = new ConcurrentHashMap<Location, MinecartManiaFurnace>();
    private static ConcurrentHashMap<Location, MinecartManiaBrewingStand> brewingStands = new ConcurrentHashMap<Location, MinecartManiaBrewingStand>();
    private static ConcurrentHashMap<String, MinecartManiaPlayer> players = new ConcurrentHashMap<String, MinecartManiaPlayer>();
    private static ConcurrentHashMap<String, Object> configuration = new ConcurrentHashMap<String, Object>();
    private static int counter = 0;
    private static Lock pruneLock = new ReentrantLock();
    
    /**
     * Returns a new MinecartManiaMinecart from storage if it already exists, or creates and stores a new MinecartManiaMinecart object, and returns it
     * 
     * @param the minecart to wrap
     */
    @ThreadSafe
    public static MinecartManiaMinecart getMinecartManiaMinecart(Minecart minecart) {
        prune();
        final int id = minecart.getEntityId();
        final MinecartManiaMinecart testMinecart = minecarts.get(id);
        if (testMinecart == null) {
            synchronized (minecart) {
                //may have been created while waiting for the lock
                if (minecarts.get(id) != null)
                    return minecarts.get(id);
                //Special handling because bukkit fails at creating the right type of minecart entity
                final CraftMinecart cm = (CraftMinecart) minecart;
                final EntityMinecart em = cm.getHandle();
                final CraftServer server = (CraftServer) Bukkit.getServer();
                if (em.type == 1) {
                    final CraftStorageMinecart csm = new CraftStorageMinecart(server, em);
                    minecart = csm;
                } else if (em.type == 2) {
                    final CraftPoweredMinecart csm = new CraftPoweredMinecart(server, em);
                    minecart = csm;
                }
                //End workaround
                MinecartManiaMinecart newCart;
                if (minecart instanceof StorageMinecart) {
                    newCart = new MinecartManiaStorageCart(minecart);
                } else {
                    newCart = new MinecartManiaMinecart(minecart);
                }
                minecarts.put(id, newCart);
                minecartUUID2EID.put(newCart.minecart.getUniqueId(), id);
                return newCart;
            }
        }
        return testMinecart;
    }
    
    /**
     * Returns a new MinecartManiaMinecart from storage if it already exists, or creates and stores a new MinecartManiaMinecart object, and returns it
     * 
     * @param the minecart to wrap
     */
    @ThreadSafe
    public static MinecartManiaMinecart getMinecartManiaMinecart(final UUID uuid) {
        prune();
        final Integer id = minecartUUID2EID.get(uuid);
        if (id == null)
            return null;
        return minecarts.get(id);
    }
    
    /**
     * Returns true if the Minecart with the given entityID was deleted, false if not.
     * 
     * @param the id of the minecart to delete
     */
    @ThreadSafe
    public static boolean delMinecartManiaMinecart(final int entityID) {
        if (minecarts.containsKey(new Integer(entityID))) {
            minecarts.remove(new Integer(entityID));
            return true;
        }
        return false;
    }
    
    @ThreadSafe
    public static void prune() {
        if (pruneLock.tryLock()) {
            try {
                counter++;
                if ((counter % 100000) == 0) {
                    counter = 0;
                    final DebugTimer time = new DebugTimer("Pruning");
                    int minecart = minecarts.size();
                    int chest = chests.size();
                    int dispenser = dispensers.size();
                    int furnace = furnaces.size();
                    int brewingStand = brewingStands.size();
                    pruneFurnaces();
                    pruneDispensers();
                    pruneChests();
                    pruneMinecarts();
                    pruneBrewingStands();
                    minecart -= minecarts.size();
                    chest -= chests.size();
                    dispenser -= dispensers.size();
                    furnace -= furnaces.size();
                    brewingStand -= brewingStands.size();
                    MinecartManiaLogger.getInstance().debug(String.format("Finished Pruning. Removed %d minecarts, %d chests, %d dispensers, %d brewing stands, and %d furnaces from memory", minecart, chest, dispenser, brewingStand, furnace));
                    time.logProcessTime();
                }
            } finally {
                pruneLock.unlock();
            }
        }
    }
    
    public static void pruneFurnaces() {
        final Iterator<Entry<Location, MinecartManiaFurnace>> i = furnaces.entrySet().iterator();
        while (i.hasNext()) {
            final Entry<Location, MinecartManiaFurnace> e = i.next();
            if ((e.getKey().getBlock().getTypeId() != Material.FURNACE.getId()) && (e.getKey().getBlock().getTypeId() != Material.BURNING_FURNACE.getId())) {
                i.remove();
            }
        }
    }
    
    public static void pruneBrewingStands() {
        final Iterator<Entry<Location, MinecartManiaBrewingStand>> i = brewingStands.entrySet().iterator();
        while (i.hasNext()) {
            final Entry<Location, MinecartManiaBrewingStand> e = i.next();
            if (e.getKey().getBlock().getTypeId() != Material.BREWING_STAND.getId()) {
                i.remove();
            }
        }
    }
    
    public static void pruneDispensers() {
        final Iterator<Entry<Location, MinecartManiaDispenser>> i = dispensers.entrySet().iterator();
        while (i.hasNext()) {
            final Entry<Location, MinecartManiaDispenser> e = i.next();
            if (e.getKey().getBlock().getTypeId() != Material.DISPENSER.getId()) {
                i.remove();
            }
        }
    }
    
    public static void pruneChests() {
        final Iterator<Entry<Location, MinecartManiaChest>> i = chests.entrySet().iterator();
        while (i.hasNext()) {
            final Entry<Location, MinecartManiaChest> e = i.next();
            if (e.getKey().getBlock().getTypeId() != Material.CHEST.getId()) {
                i.remove();
            }
        }
    }
    
    public static void pruneMinecarts() {
        final Iterator<Entry<Integer, MinecartManiaMinecart>> i = minecarts.entrySet().iterator();
        final HashSet<Integer> idList = new HashSet<Integer>();
        while (i.hasNext()) {
            final Entry<Integer, MinecartManiaMinecart> e = i.next();
            if (e.getValue().isDead() || e.getValue().minecart.isDead()) {
                i.remove();
            } else {
                if (idList.contains(e.getValue().minecart.getEntityId())) {
                    MinecartManiaLogger.getInstance().severe("Warning! Duplicate minecart's detected! Deleting duplicate. Minecart ID: " + e.getValue().minecart.getEntityId());
                    i.remove();
                } else {
                    idList.add(e.getValue().minecart.getEntityId());
                }
            }
        }
    }
    
    /**
     ** Returns any minecart at the given location, or null if none is present
     ** 
     * @param the x - coordinate to check
     ** @param the y - coordinate to check
     ** @param the z - coordinate to check
     **/
    @ThreadSafe
    public static MinecartManiaMinecart getMinecartManiaMinecartAt(final int x, final int y, final int z) {
        final Iterator<Entry<Integer, MinecartManiaMinecart>> i = minecarts.entrySet().iterator();
        while (i.hasNext()) {
            final Entry<Integer, MinecartManiaMinecart> e = i.next();
            if (e.getValue().minecart.getLocation().getBlockX() == x) {
                if (e.getValue().minecart.getLocation().getBlockY() == y) {
                    if (e.getValue().minecart.getLocation().getBlockZ() == z)
                        return e.getValue();
                }
            }
        }
        
        return null;
    }
    
    /**
     * Returns an arraylist of all the MinecartManiaMinecarts stored by this class
     * 
     * @return arraylist of all MinecartManiaMinecarts
     */
    @ThreadSafe
    public static ArrayList<MinecartManiaMinecart> getMinecartManiaMinecartList() {
        final Iterator<Entry<Integer, MinecartManiaMinecart>> i = minecarts.entrySet().iterator();
        final ArrayList<MinecartManiaMinecart> minecartList = new ArrayList<MinecartManiaMinecart>(minecarts.size());
        while (i.hasNext()) {
            minecartList.add(i.next().getValue());
        }
        return minecartList;
    }
    
    /**
     * Returns a new MinecartManiaChest from storage if it already exists, or creates and stores a new MinecartManiaChest object, and returns it
     * 
     * @param the chest to wrap
     */
    public static MinecartManiaChest getMinecartManiaChest(final Chest chest) {
        final MinecartManiaChest testChest = chests.get(new Location(chest.getWorld(), chest.getX(), chest.getY(), chest.getZ()));
        if (testChest == null) {
            final MinecartManiaChest newChest = new MinecartManiaChest(chest);
            chests.put(new Location(chest.getWorld(), chest.getX(), chest.getY(), chest.getZ()), newChest);
            return newChest;
        } else {
            //Verify that this block is still a chest (could have been changed)
            if (MinecartManiaWorld.getBlockIdAt(testChest.getWorld(), testChest.getX(), testChest.getY(), testChest.getZ()) == Material.CHEST.getId()) {
                testChest.updateInventory(testChest.getInventory());
                return testChest;
            } else {
                chests.remove(new Location(chest.getWorld(), chest.getX(), chest.getY(), chest.getZ()));
                return null;
            }
        }
    }
    
    /**
     * Returns true if the chest with the given location was deleted, false if not.
     * 
     * @param the location of the chest to delete
     */
    public static boolean delMinecartManiaChest(final Location v) {
        if (chests.containsKey(v)) {
            chests.remove(v);
            return true;
        }
        return false;
    }
    
    /**
     * Returns an arraylist of all the MinecartManiaChests stored by this class
     * 
     * @return arraylist of all MinecartManiaChest
     */
    public static ArrayList<MinecartManiaChest> getMinecartManiaChestList() {
        final Iterator<Entry<Location, MinecartManiaChest>> i = chests.entrySet().iterator();
        final ArrayList<MinecartManiaChest> chestList = new ArrayList<MinecartManiaChest>(chests.size());
        while (i.hasNext()) {
            chestList.add(i.next().getValue());
        }
        return chestList;
    }
    
    /**
     ** Returns a new MinecartManiaDispenser from storage if it already exists, or creates and stores a new MinecartManiaDispenser object, and returns it
     ** 
     * @param the dispenser to wrap
     **/
    public static MinecartManiaDispenser getMinecartManiaDispenser(final Dispenser dispenser) {
        final MinecartManiaDispenser testDispenser = dispensers.get(new Location(dispenser.getWorld(), dispenser.getX(), dispenser.getY(), dispenser.getZ()));
        if (testDispenser == null) {
            final MinecartManiaDispenser newDispenser = new MinecartManiaDispenser(dispenser);
            dispensers.put(new Location(dispenser.getWorld(), dispenser.getX(), dispenser.getY(), dispenser.getZ()), newDispenser);
            return newDispenser;
        } else {
            //Verify that this block is still a dispenser (could have been changed)
            if (MinecartManiaWorld.getBlockIdAt(testDispenser.getWorld(), testDispenser.getX(), testDispenser.getY(), testDispenser.getZ()) == Material.DISPENSER.getId()) {
                testDispenser.updateInventory(testDispenser.getInventory());
                return testDispenser;
            } else {
                dispensers.remove(new Location(dispenser.getWorld(), dispenser.getX(), dispenser.getY(), dispenser.getZ()));
                return null;
            }
        }
    }
    
    /**
     ** Returns true if the dispenser with the given location was deleted, false if not.
     ** 
     * @param the location of the dispenser to delete
     **/
    public static boolean delMinecartManiaDispenser(final Location v) {
        if (dispensers.containsKey(v)) {
            dispensers.remove(v);
            return true;
        }
        return false;
    }
    
    /**
     * Returns an arraylist of all the MinecartManiaDispensers stored by this class
     * 
     * @return arraylist of all MinecartManiaDispensers
     */
    public static ArrayList<MinecartManiaDispenser> getMinecartManiaDispenserList() {
        final Iterator<Entry<Location, MinecartManiaDispenser>> i = dispensers.entrySet().iterator();
        final ArrayList<MinecartManiaDispenser> dispenserList = new ArrayList<MinecartManiaDispenser>(dispensers.size());
        while (i.hasNext()) {
            dispenserList.add(i.next().getValue());
        }
        return dispenserList;
    }
    
    /**
     ** Returns a new MinecartManiaFurnace from storage if it already exists, or creates and stores a new MinecartManiaFurnace object, and returns it
     ** 
     * @param the furnace to wrap
     **/
    public static MinecartManiaFurnace getMinecartManiaFurnace(final Furnace furnace) {
        final MinecartManiaFurnace testFurnace = furnaces.get(new Location(furnace.getWorld(), furnace.getX(), furnace.getY(), furnace.getZ()));
        if (testFurnace == null) {
            final MinecartManiaFurnace newFurnace = new MinecartManiaFurnace(furnace);
            furnaces.put(new Location(furnace.getWorld(), furnace.getX(), furnace.getY(), furnace.getZ()), newFurnace);
            return newFurnace;
        } else {
            //Verify that this block is still a furnace (could have been changed)
            if ((MinecartManiaWorld.getBlockIdAt(testFurnace.getWorld(), testFurnace.getX(), testFurnace.getY(), testFurnace.getZ()) == Material.FURNACE.getId()) || (MinecartManiaWorld.getBlockIdAt(testFurnace.getWorld(), testFurnace.getX(), testFurnace.getY(), testFurnace.getZ()) == Material.BURNING_FURNACE.getId())) {
                testFurnace.updateInventory(testFurnace.getInventory());
                return testFurnace;
            } else {
                furnaces.remove(new Location(furnace.getWorld(), furnace.getX(), furnace.getY(), furnace.getZ()));
                return null;
            }
        }
    }
    
    /**
     ** Returns a new MinecartManiaBrewingStand from storage if it already exists, or creates and stores a new MinecartManiaFurnace object, and returns it
     ** 
     * @param the furnace to wrap
     **/
    public static MinecartManiaBrewingStand getMinecartManiaBrewingStand(final BrewingStand brewingStand) {
        final MinecartManiaBrewingStand testStand = brewingStands.get(new Location(brewingStand.getWorld(), brewingStand.getX(), brewingStand.getY(), brewingStand.getZ()));
        if (testStand == null) {
            final MinecartManiaBrewingStand newStand = new MinecartManiaBrewingStand(brewingStand);
            brewingStands.put(new Location(brewingStand.getWorld(), brewingStand.getX(), brewingStand.getY(), brewingStand.getZ()), newStand);
            return newStand;
        } else {
            //Verify that this block is still a furnace (could have been changed)
            if (MinecartManiaWorld.getBlockIdAt(testStand.getWorld(), testStand.getX(), testStand.getY(), testStand.getZ()) == Material.BREWING_STAND.getId()) {
                testStand.updateInventory(testStand.getInventory());
                return testStand;
            } else {
                brewingStands.remove(new Location(brewingStand.getWorld(), brewingStand.getX(), brewingStand.getY(), brewingStand.getZ()));
                return null;
            }
        }
    }
    
    /**
     ** Returns true if the furnaces with the given location was deleted, false if not.
     ** 
     * @param the location of the furnaces to delete
     **/
    public static boolean delMinecartManiaFurnace(final Location v) {
        if (furnaces.containsKey(v)) {
            furnaces.remove(v);
            return true;
        }
        return false;
    }
    
    /**
     ** Returns true if the furnaces with the given location was deleted, false if not.
     ** 
     * @param the location of the furnaces to delete
     **/
    public static boolean delMinecartManiaBrewingStand(final Location v) {
        if (brewingStands.containsKey(v)) {
            brewingStands.remove(v);
            return true;
        }
        return false;
    }
    
    /**
     * Returns an arraylist of all the MinecartManiaFurnaces stored by this class
     * 
     * @return arraylist of all MinecartManiaFurnaces
     */
    public static ArrayList<MinecartManiaFurnace> getMinecartManiaFurnaceList() {
        final Iterator<Entry<Location, MinecartManiaFurnace>> i = furnaces.entrySet().iterator();
        final ArrayList<MinecartManiaFurnace> furnaceList = new ArrayList<MinecartManiaFurnace>(furnaces.size());
        while (i.hasNext()) {
            furnaceList.add(i.next().getValue());
        }
        return furnaceList;
    }
    
    /**
     * Returns an arraylist of all the MinecartManiaBrewingStands stored by this class
     * 
     * @return arraylist of all MinecartManiaBrewingStands
     */
    public static ArrayList<MinecartManiaBrewingStand> getMinecartManiaBrewingStandList() {
        final Iterator<Entry<Location, MinecartManiaBrewingStand>> i = brewingStands.entrySet().iterator();
        final ArrayList<MinecartManiaBrewingStand> furnaceList = new ArrayList<MinecartManiaBrewingStand>(brewingStands.size());
        while (i.hasNext()) {
            furnaceList.add(i.next().getValue());
        }
        return furnaceList;
    }
    
    /**
     ** Returns a new MinecartManiaPlayer from storage if it already exists, or creates and stores a new MinecartManiaPlayer object, and returns it
     ** 
     * @param the player to wrap
     **/
    public static MinecartManiaPlayer getMinecartManiaPlayer(final Player player) {
        return getMinecartManiaPlayer(player.getName());
    }
    
    /**
     ** Returns a new MinecartManiaPlayer from storage if it already exists, or creates and stores a new MinecartManiaPlayer object, and returns it
     ** 
     * @param the name of the player to wrap
     **/
    public static MinecartManiaPlayer getMinecartManiaPlayer(final String player) {
        MinecartManiaPlayer testPlayer = players.get(player);
        if (testPlayer == null) {
            testPlayer = new MinecartManiaPlayer(player);
            players.put(player, testPlayer);
        }
        if (testPlayer.isOnline()) {
            testPlayer.updateInventory(testPlayer.getPlayer().getInventory());
        }
        return testPlayer;
    }
    
    public static void setMinecartManiaPlayer(final MinecartManiaPlayer player, final String name) {
        players.put(name, player);
    }
    
    /**
     * Returns an arraylist of all the MinecartManiaPlayers stored by this class. These players may not be online.
     * 
     * @return arraylist of all MinecartManiaPlayers
     */
    public static ArrayList<MinecartManiaPlayer> getMinecartManiaPlayerList() {
        final Iterator<Entry<String, MinecartManiaPlayer>> i = players.entrySet().iterator();
        final ArrayList<MinecartManiaPlayer> playerList = new ArrayList<MinecartManiaPlayer>(players.size());
        while (i.hasNext()) {
            playerList.add(i.next().getValue());
        }
        return playerList;
    }
    
    /**
     ** Returns the value from the loaded configuration
     ** 
     * @param the string key the configuration value is associated with
     **/
    public static Object getConfigurationValue(final String key) {
        if (configuration.containsKey(key))
            return configuration.get(key);
        return null;
    }
    
    /**
     ** Creates a new configuration value if it does not already exists, or resets an existing value
     ** 
     * @param the string key the configuration value is associated with
     ** @param the value to store
     **/
    public static void setConfigurationValue(final String key, final Object value) {
        if (value == null) {
            configuration.remove(key);
        } else {
            configuration.put(key, value);
        }
    }
    
    public static ConcurrentHashMap<String, Object> getConfiguration() {
        return configuration;
    }
    
    /**
     ** Returns an integer value from the given object, if it exists
     ** 
     * @param the object containing the value
     **/
    @Deprecated
    public static int getIntValue(final Object o) {
        if (o != null) {
            if (o instanceof Integer)
                return ((Integer) o).intValue();
        }
        return 0;
    }
    
    @Deprecated
    public static double getDoubleValue(final Object o) {
        if (o != null) {
            if (o instanceof Double)
                return ((Double) o).doubleValue();
            //Attempt integer value
            return getIntValue(o);
        }
        return 0;
    }
    
    @Deprecated
    public static int getMaximumMinecartSpeedPercent() {
        return getIntValue(getConfigurationValue("MaximumMinecartSpeedPercent"));
    }
    
    @Deprecated
    public static int getDefaultMinecartSpeedPercent() {
        return getIntValue(getConfigurationValue("DefaultMinecartSpeedPercent"));
    }
    
    @Deprecated
    public static int getMinecartsClearRailsSetting() {
        return getIntValue(getConfigurationValue("MinecartsClearRails"));
    }
    
    @Deprecated
    public static boolean isKeepMinecartsLoaded() {
        final Object o = getConfigurationValue("KeepMinecartsLoaded");
        if (o != null) {
            final Boolean value = (Boolean) o;
            return value.booleanValue();
        }
        return false;
    }
    
    @Deprecated
    public static boolean isMinecartsKillMobs() {
        final Object o = getConfigurationValue("MinecartsKillMobs");
        if (o != null) {
            final Boolean value = (Boolean) o;
            return value.booleanValue();
        }
        return true;
    }
    
    @Deprecated
    public static boolean isReturnMinecartToOwner() {
        final Object o = getConfigurationValue("MinecartsReturnToOwner");
        if (o != null) {
            final Boolean value = (Boolean) o;
            return value.booleanValue();
        }
        return true;
    }
    
    /**
     ** Returns the block at the given x, y, z coordinates
     ** 
     * @param w World to take effect in
     ** @param x coordinate
     ** @param y coordinate
     ** @param z coordinate
     **/
    public static Block getBlockAt(final World w, final int x, final int y, final int z) {
        return w.getBlockAt(x, y, z);
    }
    
    /**
     ** Returns the block type id at the given x, y, z coordinates
     ** 
     * @param w World to take effect in
     ** @param x coordinate
     ** @param y coordinate
     ** @param z coordinate
     **/
    public static int getBlockIdAt(final World w, final int x, final int y, final int z) {
        return w.getBlockTypeIdAt(x, y, z);
    }
    
    /**
     ** Returns the block at the given x, y, z coordinates
     ** 
     * @param w World to take effect in
     ** @param new block type id
     ** @param x coordinate
     ** @param y coordinate
     ** @param z coordinate
     **/
    public static void setBlockAt(final World w, final int type, final int x, final int y, final int z) {
        w.getBlockAt(x, y, z).setTypeId(type);
    }
    
    /**
     ** Returns the block data at the given x, y, z coordinates
     ** 
     * @param w World to take effect in
     ** @param x coordinate
     ** @param y coordinate
     ** @param z coordinate
     **/
    public static byte getBlockData(final World w, final int x, final int y, final int z) {
        return w.getBlockAt(x, y, z).getData();
    }
    
    /**
     ** sets the block data at the given x, y, z coordinates
     ** 
     * @param w World to take effect in
     ** @param x coordinate
     ** @param y coordinate
     ** @param z coordinate
     ** @param new data to set
     **/
    public static void setBlockData(final World w, final int x, final int y, final int z, final int data) {
        w.getBlockAt(x, y, z).setData((byte) (data));
    }
    
    /**
     ** Returns true if the block at the given x, y, z coordinates is indirectly powered
     ** 
     * @param w World to take effect in
     ** @param x coordinate
     ** @param y coordinate
     ** @param z coordinate
     **/
    public static boolean isBlockIndirectlyPowered(final World w, final int x, final int y, final int z) {
        return getBlockAt(w, x, y, z).isBlockIndirectlyPowered();
    }
    
    /**
     ** Returns true if the block at the given x, y, z coordinates is directly powered
     ** 
     * @param w World to take effect in
     ** @param x coordinate
     ** @param y coordinate
     ** @param z coordinate
     **/
    public static boolean isBlockPowered(final World w, final int x, final int y, final int z) {
        return getBlockAt(w, x, y, z).isBlockPowered();
    }
    
    /**
     ** Sets the block at the given x, y, z coordinates to the given power state, if possible
     ** 
     * @param w World to take effect in
     ** @param x coordinate
     ** @param y coordinate
     ** @param z coordinate
     ** @param power state
     **/
    public static void setBlockPowered(final World w, final int x, final int y, final int z, final boolean power) {
        final MaterialData md = getBlockAt(w, x, y, z).getState().getData();
        final int data = getBlockData(w, x, y, z);
        if ((getBlockAt(w, x, y, z).getTypeId() == (Material.DIODE_BLOCK_OFF.getId())) && power) {
            setBlockAt(w, Material.DIODE_BLOCK_ON.getId(), x, y, z);
            setBlockData(w, x, y, z, (byte) data);
        } else if ((getBlockAt(w, x, y, z).getTypeId() == (Material.DIODE_BLOCK_ON.getId())) && !power) {
            setBlockAt(w, Material.DIODE_BLOCK_OFF.getId(), x, y, z);
            setBlockData(w, x, y, z, (byte) data);
        } else if ((md instanceof Lever) || (md instanceof Button)) {
            setBlockData(w, x, y, z, ((byte) (power ? data | 0x8 : data & 0x7)));
        }
    }
    
    /**
     ** Sets the block at the given x, y, z coordinates, as well as any block directly touch the given block to the given power state, if possible
     ** 
     * @param w World to take effect in
     ** @param x coordinate
     ** @param y coordinate
     ** @param z coordinate
     ** @param power state
     **/
    public static void setBlockIndirectlyPowered(final World w, final int x, final int y, final int z, final boolean power) {
        setBlockPowered(w, x, y, z, power);
        setBlockPowered(w, x - 1, y, z, power);
        setBlockPowered(w, x + 1, y, z, power);
        setBlockPowered(w, x, y - 1, z, power);
        setBlockPowered(w, x, y + 1, z, power);
        setBlockPowered(w, x, y, z - 1, power);
        setBlockPowered(w, x, y, z + 1, power);
    }
    
    /**
     * Spawns a minecart at the given coordinates. Includes a "fudge factor" to get the minecart to properly line up with minecart tracks.
     ** 
     * @param Location to spawn the minecart at
     ** @param Material type of minecart to spawn
     ** @param Owner of this minecart (player or chest). Can be null
     **/
    public static MinecartManiaMinecart spawnMinecart(final Location l, final Material type, final Object owner) {
        return spawnMinecart(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ(), type, owner);
    }
    
    /**
     * Spawns a minecart at the given coordinates. Includes a "fudge factor" to get the minecart to properly line up with minecart tracks.
     ** 
     * @param w World to take effect in
     ** @param x coordinate
     ** @param y coordinate
     ** @param z coordinate
     ** @param Material type of minecart to spawn
     ** @param Owner of this minecart (player or chest). Can be null
     **/
    public static MinecartManiaMinecart spawnMinecart(final World w, final int x, final int y, final int z, final Material type, final Object owner) {
        final Location loc = new Location(w, x + 0.5D, y, z + 0.5D);
        Minecart m;
        if ((type == null) || (type.getId() == Material.MINECART.getId())) {
            m = w.spawn(loc, Minecart.class);
        } else if (type.getId() == Material.POWERED_MINECART.getId()) {
            m = w.spawn(loc, PoweredMinecart.class);
        } else {
            m = w.spawn(loc, StorageMinecart.class);
        }
        MinecartManiaMinecart minecart = null;
        String ownerName = "none";
        if (owner != null) {
            if (owner instanceof Player) {
                ownerName = ((Player) owner).getName();
            } else if (owner instanceof MinecartManiaPlayer) {
                ownerName = ((MinecartManiaPlayer) owner).getName();
            } else if (owner instanceof MinecartManiaChest) {
                ownerName = ((MinecartManiaChest) owner).toString();
            }
        }
        if (m instanceof StorageMinecart) {
            minecart = new MinecartManiaStorageCart(m, ownerName);
        } else {
            minecart = new MinecartManiaMinecart(m, ownerName);
        }
        minecarts.put(m.getEntityId(), minecart);
        return minecart;
    }
    
    public static int getMaxStackSize(final ItemStack item) {
        if (item == null)
            return 64;
        final CraftItemStack stack = new CraftItemStack(item.getTypeId(), item.getAmount(), item.getDurability());
        if ((stack.getMaxStackSize() != -1) && !(Boolean) MinecartManiaWorld.getConfigurationValue("StackAllItems"))
            return stack.getMaxStackSize();
        return 64;
    }
    
    public static void spawnDrop(final World w, final int x, final int y, final int z, final ItemStack stack) {
        w.dropItemNaturally(new Location(w, x + 0.5, y + 0.5, z + 0.5), stack);
    }
}