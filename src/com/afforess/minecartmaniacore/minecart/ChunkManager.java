package com.afforess.minecartmaniacore.minecart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.ChunkCoordIntPair;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Entity;

import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;

public class ChunkManager {
    protected ConcurrentHashMap<ChunkCoordIntPair, List<UUID>> loaded = new ConcurrentHashMap<ChunkCoordIntPair, List<UUID>>();
    protected static ConcurrentHashMap<UUID, ChunkManager> worlds = new ConcurrentHashMap<UUID, ChunkManager>();
    private final CraftWorld world;
    private static final int range = 4;
    
    public static void init(final World w) {
        if (!worlds.contains(w.getUID())) {
            worlds.put(w.getUID(), new ChunkManager(w));
        }
    }
    
    public static int chunksLoaded() {
        int numLoaded = 0;
        for (UUID wid : worlds.keySet()) {
            numLoaded += worlds.get(wid).loaded.size();
        }
        return numLoaded;
    }
    
    public ChunkManager(final World world) {
        this.world = (CraftWorld) world;
    }
    
    public static void updateChunks(final Entity ent) {
        worlds.get(ent.getWorld().getUID()).updateChunks(ent.getUniqueId(), ent.getLocation());
    }
    
    public static void unloadChunks(final Entity ent) {
        worlds.get(ent.getWorld().getUID()).unloadChunks(ent.getUniqueId());
    }
    
    public void updateChunks(final UUID entityID, final Location location) {
        final Chunk center = location.getBlock().getChunk();
        for (int dx = -(range); dx <= range; dx++) {
            for (int dz = -(range); dz <= range; dz++) {
                final ChunkCoordIntPair cpos = new ChunkCoordIntPair(center.getX() + dx, center.getZ() + dz);
                if (!loaded.containsKey(cpos)) {
                    final Chunk chunk = world.getChunkAt(center.getX() + dx, center.getZ() + dz);
                    world.loadChunk(chunk);
                    loaded.put(cpos, new ArrayList<UUID>());
                }
                if (!loaded.get(cpos).contains(entityID)) {
                    loaded.get(cpos).add(entityID);
                }
            }
        }
        trimLoadedChunks(null);
    }
    
    private void trimLoadedChunks(final UUID unloadByOwner) {
        final Iterator<Entry<ChunkCoordIntPair, List<UUID>>> iterate = loaded.entrySet().iterator();
        
        int unloadedChunks = 0;
        while (iterate.hasNext()) {
            final Entry<ChunkCoordIntPair, List<UUID>> e = iterate.next();
            // Remove ourselves from the list of owners, if applicable.
            if ((unloadByOwner != null) && e.getValue().contains(unloadByOwner)) {
                e.getValue().remove(unloadByOwner);
            }
            
            boolean unload = false;
            
            // If no cart owns the chunk, unload it.
            if (e.getValue().size() == 0) {
                unload = true;
            }
            
            // If at least one cart owns the chunk,
            if (!unload && (e.getValue().size() > 0)) {
                Iterator<UUID> i = e.getValue().iterator();
                while (i.hasNext()) {
                    // Determine who owns it
                    final UUID owner = i.next();
                    final Entity ent = MinecartManiaCore.findEntity(owner);
                    if (ent == null) {
                        MinecartManiaLogger.getInstance().severe("[ChunkManager] Can't find owner " + owner.toString() + " of loaded chunk " + e.getKey().x + "," + e.getKey().z, true, new Object[] {});
                        i.remove();
                        continue;
                    }
                    
                    // Now see if this chunk is within range of the cart
                    final int chunkX = e.getKey().x;
                    final int chunkZ = e.getKey().z;
                    final int ownerX = ent.getLocation().getChunk().getX();
                    final int ownerZ = ent.getLocation().getChunk().getZ();
                    if ((Math.abs(chunkX - ownerX) > range) || (Math.abs(chunkZ - ownerZ) > range)) {
                        unload = true;
                        break;
                    }
                }
            }
            
            // If we don't own it, unload it.
            // If the chunk's not in range of the cart, also unload it.
            if (unload) {
                if (unloadChunk(e.getKey().x, e.getKey().z)) {
                    unloadedChunks++;
                    iterate.remove();
                }
            }
        }
        //        if (unloadedChunks > 0) {
        //            MinecartManiaLogger.getInstance().info("[ChunkManager] Unloaded " + unloadedChunks + ".");
        //        }
    }
    
    public void unloadChunks(final UUID entityID) {
        trimLoadedChunks(entityID);
    }
    
    private boolean spawnChunk(final int x, final int z) {
        //copied from MC Server code
        final int k = (x * 16) + 8;
        final int l = (z * 16) + 8;
        final short short1 = 128;
        if ((k < -short1) || (k > short1) || (l < -short1) || (l > short1))
            return false;
        return true;
    }
    
    private boolean unloadChunk(final int x, final int z) {
        //Spawn must never be unloaded
        if (spawnChunk(x, z))
            return false;
        if (world.getChunkAt(x, z) != null) {
            if (!world.isChunkInUse(x, z)) {
                if (world.unloadChunk(x, z, true, false))
                    return true;
            }
        }
        return false;
    }
}
